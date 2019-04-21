package twistedgate.immersiveposts.common.blocks;

import java.util.Optional;
import java.util.Random;

import com.google.common.collect.ImmutableMap;

import blusunrize.immersiveengineering.api.IPostBlock;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IUnlistedProperty;
import twistedgate.immersiveposts.common.blocks.state.PostState;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.enums.EnumPostType;
import twistedgate.immersiveposts.utils.BlockUtilities;

/**
 * All-in-one package. Containing everything into one neat class is the best.
 * @author TwistedGate
 */
public class BlockPost extends IPOBlockBase implements IPostBlock,ITileEntityProvider{
	public static final PropertyBool LPARM_NORTH=PropertyBool.create("parm_north");
	public static final PropertyBool LPARM_EAST=PropertyBool.create("parm_east");
	public static final PropertyBool LPARM_SOUTH=PropertyBool.create("parm_south");
	public static final PropertyBool LPARM_WEST=PropertyBool.create("parm_west");
	
	public static final PropertyDirection DIRECTION=PropertyDirection.create("facing");
	public static final PropertyBool FLIP=PropertyBool.create("flip");
	public static final PropertyEnum<EnumPostType> TYPE=PropertyEnum.create("type", EnumPostType.class);
	
	protected EnumPostMaterial postMaterial;
	public BlockPost(Material blockMaterial, EnumPostMaterial postMaterial){
		super(blockMaterial, postMaterial.getName());
		this.postMaterial=postMaterial;
		
		setResistance(3.0F);
		setHardness(3.0F);
		if(this.postMaterial==EnumPostMaterial.WOOD){
			setHarvestLevel("axe", 0);
		}else{
			setHarvestLevel("pickaxe", 1);
		}
		
		if(this.postMaterial==EnumPostMaterial.URANIUM)
			setLightLevel(8);
		
		setDefaultState(this.blockState.getBaseState()
				.withProperty(DIRECTION, EnumFacing.NORTH)
				.withProperty(FLIP, false)
				.withProperty(TYPE, EnumPostType.POST)
				.withProperty(LPARM_NORTH, false)
				.withProperty(LPARM_EAST, false)
				.withProperty(LPARM_SOUTH, false)
				.withProperty(LPARM_WEST, false)
				);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta){
		//if(this.postMaterial==EnumPostMaterial.URANIUM)
			//return new TileEntityGlowy();
		return null;
	}
	
	public final EnumPostMaterial getPostMaterial(){
		return this.postMaterial;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer(){
//		if(this.postMaterial==EnumPostMaterial.URANIUM)
//			return BlockRenderLayer.TRANSLUCENT;
		
		return BlockRenderLayer.SOLID;
	}
	
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, new IProperty<?>[]{
			DIRECTION, FLIP, TYPE,
			LPARM_NORTH, LPARM_EAST, LPARM_SOUTH, LPARM_WEST,
		}){
			@Override
			protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties){
				return new PostState(block, properties);
			}
		};
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
		if(state.getValue(TYPE)!=EnumPostType.ARM)
			drops.add(this.postMaterial.getFenceItem());
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		switch(state.getValue(TYPE)){
			case POST: return 0;
			case POST_TOP: return 1;
			case ARM:{
				int rot;
				switch(state.getValue(DIRECTION)){
					case EAST:rot=1;break;
					case SOUTH:rot=2;break;
					case WEST:rot=3;break;
					default:rot=0; // Aka North, Up and Down
				}
				
				return (state.getValue(FLIP)?6:2)+rot;
			}
			default: return 0;
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		IBlockState state=getDefaultState();
		switch(meta){
			case 0: return state;
			case 1: return state.withProperty(TYPE, EnumPostType.POST_TOP);
			
			case 2: return state.withProperty(TYPE, EnumPostType.ARM).withProperty(DIRECTION, EnumFacing.NORTH);
			case 3: return state.withProperty(TYPE, EnumPostType.ARM).withProperty(DIRECTION, EnumFacing.EAST);
			case 4: return state.withProperty(TYPE, EnumPostType.ARM).withProperty(DIRECTION, EnumFacing.SOUTH);
			case 5: return state.withProperty(TYPE, EnumPostType.ARM).withProperty(DIRECTION, EnumFacing.WEST);
			
			case 6: return state.withProperty(TYPE, EnumPostType.ARM).withProperty(FLIP, true).withProperty(DIRECTION, EnumFacing.NORTH);
			case 7: return state.withProperty(TYPE, EnumPostType.ARM).withProperty(FLIP, true).withProperty(DIRECTION, EnumFacing.EAST);
			case 8: return state.withProperty(TYPE, EnumPostType.ARM).withProperty(FLIP, true).withProperty(DIRECTION, EnumFacing.SOUTH);
			case 9: return state.withProperty(TYPE, EnumPostType.ARM).withProperty(FLIP, true).withProperty(DIRECTION, EnumFacing.WEST);
			default: return state;
		}
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos){
		if(state.getValue(TYPE)!=EnumPostType.POST){
			return state
					.withProperty(LPARM_NORTH, false)
					.withProperty(LPARM_EAST, false)
					.withProperty(LPARM_SOUTH, false)
					.withProperty(LPARM_WEST, false);
			/*
			 * canConnect is rather time consuming, so this is an attempt
			 * to speed this up if the type is not a POST.
			 * 
			 * Also it would not make sense for the Arms to check these too.
			 */
		}
		
		boolean b0=canConnect(worldIn, pos, EnumFacing.NORTH);
		boolean b1=canConnect(worldIn, pos, EnumFacing.EAST);
		boolean b2=canConnect(worldIn, pos, EnumFacing.SOUTH);
		boolean b3=canConnect(worldIn, pos, EnumFacing.WEST);
		
		return state
				.withProperty(LPARM_NORTH, b0)
				.withProperty(LPARM_EAST, b1)
				.withProperty(LPARM_SOUTH, b2)
				.withProperty(LPARM_WEST, b3);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return stateBounds(source, pos, state);
	}
	
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand){
		if(this.postMaterial==EnumPostMaterial.URANIUM){
			if(stateIn.getValue(TYPE)!=EnumPostType.ARM && rand.nextDouble()<0.125){
				double x=pos.getX()+0.375+0.25*rand.nextDouble();
				double y=pos.getY()+1.0-rand.nextDouble();
				double z=pos.getZ()+0.375+0.25*rand.nextDouble();
				worldIn.spawnParticle(EnumParticleTypes.REDSTONE, x,y,z, -1.0, 1.0, 0.25);
			}
		}
	}
	/*
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos){
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}//*/
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		return false;
	}
	
	@Override
	public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity){
		return true;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player){
		return this.postMaterial.getFenceItem();
	}
	
	@Override
	public boolean canConnectTransformer(IBlockAccess world, BlockPos pos){
		return world.getBlockState(pos).getValue(TYPE)==EnumPostType.POST;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(!worldIn.isRemote){
			ItemStack held=playerIn.getHeldItemMainhand();
			if(EnumPostMaterial.isFenceItem(held)){
				if(!held.isItemEqual(this.postMaterial.getFenceItem())){
					playerIn.sendStatusMessage(new TextComponentString("Expected: "+this.postMaterial.getFenceItem().getDisplayName()+"."), true);
					return true;
				}
				
				for(int y=0;y<(worldIn.getActualHeight()-pos.getY());y++){
					BlockPos nPos=pos.add(0,y,0);
					
					if((BlockUtilities.getBlockFrom(worldIn, nPos) instanceof BlockPost)){
						IBlockState s=worldIn.getBlockState(nPos);
						if(s.getValue(BlockPost.TYPE)==EnumPostType.ARM && s.getValue(BlockPost.FLIP)){
							return true;
						}
						
						BlockPos up=nPos.offset(EnumFacing.UP);
						if((BlockUtilities.getBlockFrom(worldIn, up) instanceof BlockPost)){
							s=worldIn.getBlockState(up);
							if(s.getValue(BlockPost.TYPE)==EnumPostType.ARM){
								return true;
							}
						}
					}
					
					if(worldIn.isAirBlock(nPos)){
						IBlockState fb=EnumPostMaterial.getPostStateFrom(held);
						if(fb!=null && worldIn.setBlockState(nPos, fb)){
							if(!playerIn.capabilities.isCreativeMode){
								held.shrink(1);
							}
						}
						return true;
						
					}else if(!(worldIn.getBlockState(nPos).getBlock() instanceof BlockPost)){
						return true;
					}
				}
			}else if(Utils.isHammer(held)){
				switch(state.getValue(TYPE)){
					case POST:case POST_TOP:{
						IBlockState defaultState=getDefaultState().withProperty(TYPE, EnumPostType.ARM);
						switch(facing){
							case NORTH:case EAST:case SOUTH:case WEST:{
								BlockPos nPos=pos.offset(facing);
								if(worldIn.isAirBlock(nPos)){
									defaultState=defaultState.withProperty(DIRECTION, facing);
									worldIn.setBlockState(nPos, defaultState, 3);
									defaultState.neighborChanged(worldIn, nPos, null, null);
									//this.neighborChanged(defaultState, worldIn, nPos, null, null);
								}else if(BlockUtilities.getBlockFrom(worldIn, nPos)==this){
									if(worldIn.getBlockState(nPos).getValue(TYPE)==EnumPostType.ARM){
										worldIn.setBlockToAir(nPos);
									}
								}
							}
							default:break;
						}
						return true;
					}
					case ARM:{
						worldIn.setBlockToAir(pos);
						return true;
					}
				}
			}
		}
		
		return Utils.isHammer(playerIn.getHeldItemMainhand()) || EnumPostMaterial.isFenceItem(playerIn.getHeldItemMainhand());
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos){
		world.getBlockState(pos).neighborChanged(world, pos, block, fromPos);
	}
	
	/*
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos){
		EnumPostType thisType=state.getValue(TYPE);
		
		if(thisType!=EnumPostType.ARM){
			BlockPos d=pos.offset(EnumFacing.DOWN);
			if(BlockUtilities.getBlockFrom(worldIn, d)==Blocks.AIR){
				dropBlockAsItem(worldIn, pos, state, 1);
				worldIn.setBlockToAir(pos);
				return;
			}
		}
		
		IBlockState aboveState=worldIn.getBlockState(pos.offset(EnumFacing.UP));
		Block aboveBlock=aboveState.getBlock();
		switch(thisType){
			case POST:{
				if(!(aboveBlock instanceof BlockPost))
					worldIn.setBlockState(pos, state.withProperty(TYPE, EnumPostType.POST_TOP));
				return;
			}
			case POST_TOP:{
				if((aboveBlock instanceof BlockPost) && aboveState.getValue(TYPE)!=EnumPostType.ARM)
					worldIn.setBlockState(pos, state.withProperty(TYPE, EnumPostType.POST));
				return;
			}
			case ARM:{
				EnumFacing f=state.getValue(DIRECTION).getOpposite();
				if(BlockUtilities.getBlockFromDirection(worldIn, pos, f)!=this){
					worldIn.setBlockToAir(pos);
					return;
				}
				
				if(canConnect(worldIn, pos, EnumFacing.UP)){
					worldIn.setBlockState(pos, state.withProperty(FLIP, false), 3);
				}
				
				boolean bool=canConnect(worldIn, pos, EnumFacing.DOWN);
				worldIn.setBlockState(pos, state.withProperty(FLIP, bool), 3);
				
				return;
			}
		}
	}//*/
	
	public static boolean canConnect(IBlockAccess worldIn, BlockPos posIn, EnumFacing facingIn){
		BlockPos nPos=posIn.offset(facingIn);
		
		IBlockState otherState=worldIn.getBlockState(nPos);
		Block otherBlock=otherState.getBlock();
		
		if(otherBlock==Blocks.AIR || otherBlock instanceof BlockPost) return false; // Nope out when air or self
		
		AxisAlignedBB box=otherState.getBoundingBox(worldIn, nPos);
		boolean b;
		switch(facingIn){
			case NORTH:	b=(box.maxZ==1.0);break;
			case SOUTH:	b=(box.minZ==0.0);break;
			case WEST:	b=(box.maxX==1.0);break;
			case EAST:	b=(box.minX==0.0);break;
			default:	b=false;
		}
		if(b && ((facingIn.getAxis()==Axis.Z && box.minX>0.0 && box.maxX<1.0) || (facingIn.getAxis()==Axis.X && box.minZ>0.0 && box.maxZ<1.0))){
			return true;
		}
		
		switch(facingIn){
			case DOWN:	return box.maxY==1.0;
			case UP:	return box.minY==0.0;
			default:	return false;
		}
	}
	
	static final AxisAlignedBB POST_SHAPE=new AxisAlignedBB(0.3125F, 0.0F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
	
	static AxisAlignedBB stateBounds(IBlockAccess world, BlockPos pos, IBlockState state){
		switch(state.getValue(TYPE)){
			case ARM:{
				EnumFacing facing=state.getValue(DIRECTION);
				boolean flipped=state.getValue(FLIP);
				
				float minY=flipped?0.0F:0.34375F;
				float maxY=flipped?0.65625F:1.0F;
				
				float minX=(facing==EnumFacing.EAST) ?-0.25F:0.3125F;
				float maxX=(facing==EnumFacing.WEST) ? 1.25F:0.6875F;
				float minZ=(facing==EnumFacing.SOUTH)?-0.25F:0.3125F;
				float maxZ=(facing==EnumFacing.NORTH)? 1.25F:0.6875F;
				
				return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
			}
			default: return POST_SHAPE;
		}
	}
}
