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
import net.minecraft.block.state.BlockFaceShape;
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
	
	public static boolean canConnect(IBlockAccess worldIn, BlockPos posIn, EnumFacing facingIn){
		BlockPos nPos=posIn.offset(facingIn);
		
		IBlockState otherState=worldIn.getBlockState(nPos);
		Block otherBlock=otherState.getBlock();
		
		if(otherBlock==Blocks.AIR) return false; // Go straight out if air, no questions asked.
		if(otherBlock instanceof BlockPost || otherState.getBlockFaceShape(worldIn, nPos, facingIn)==BlockFaceShape.MIDDLE_POLE) return false;
		
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
	
	public static class PostState extends BlockStateContainer.StateImplementation{
		public PostState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties){
			super(block, properties);
		}
		
		@Override
		public IBlockState getActualState(IBlockAccess blockAccess, BlockPos pos){
			if(this.getValue(TYPE)==EnumPostType.ARM){
				return this
						.withProperty(LPARM_NORTH, false)
						.withProperty(LPARM_EAST, false)
						.withProperty(LPARM_SOUTH, false)
						.withProperty(LPARM_WEST, false);
				/*
				 * canConnect is rather time consuming, so this is an attempt to speed this up.
				 */
			}
			
			boolean b0=canConnect(blockAccess, pos, EnumFacing.NORTH);
			boolean b1=canConnect(blockAccess, pos, EnumFacing.EAST);
			boolean b2=canConnect(blockAccess, pos, EnumFacing.SOUTH);
			boolean b3=canConnect(blockAccess, pos, EnumFacing.WEST);
			
			return this
					.withProperty(LPARM_NORTH, b0)
					.withProperty(LPARM_EAST, b1)
					.withProperty(LPARM_SOUTH, b2)
					.withProperty(LPARM_WEST, b3);
		}
		
		@Override
		public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockPos pos, EnumFacing face){
			IBlockState state=worldIn.getBlockState(pos.offset(face));
			Block block=state.getBlock();
			if(block instanceof BlockFence || block instanceof BlockWall) return BlockFaceShape.UNDEFINED;
			
			boolean f=false;
			if(block instanceof BlockWoodenDecoration)
				if(block.getMetaFromState(state)==BlockTypes_WoodenDecoration.FENCE.getMeta())
					f=true;
			
			if(block instanceof BlockMetalDecoration1){
				int tmp=block.getMetaFromState(state);
				if(tmp==BlockTypes_MetalDecoration1.ALUMINUM_FENCE.getMeta() || tmp==BlockTypes_MetalDecoration1.STEEL_FENCE.getMeta())
					f=true;
			}
			
			
			return f?BlockFaceShape.UNDEFINED:BlockFaceShape.SOLID;
		}
		
		@Override
		public AxisAlignedBB getBoundingBox(IBlockAccess blockAccess, BlockPos pos){
			return stateBounds(this);
		}
		
		@Override
		public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side){
			return false;
		}
		
		@Override
		public boolean isNormalCube(){
			return false;
		}
		
		@Override
		public boolean isOpaqueCube(){
			return false;
		}
		
		@Override
		public boolean isFullCube(){
			return false;
		}
		
		@Override
		public void neighborChanged(World world, BlockPos pos, Block block, BlockPos fromPos){
			EnumPostType thisType=this.getValue(BlockPost.TYPE);
			
			if(thisType!=EnumPostType.ARM){
				BlockPos d=pos.offset(EnumFacing.DOWN);
				if(BlockUtilities.getBlockFrom(world, d)==Blocks.AIR){
					block.dropBlockAsItem(world, pos, this, 0);
					world.setBlockToAir(pos);
					return;
				}
			}
			
			IBlockState aboveState=world.getBlockState(pos.offset(EnumFacing.UP));
			Block aboveBlock=aboveState.getBlock();
			switch(thisType){
				case POST:{
					if(!(aboveBlock instanceof BlockPost))
						world.setBlockState(pos, this.withProperty(BlockPost.TYPE, EnumPostType.POST_TOP));
					return;
				}
				case POST_TOP:{
					if((aboveBlock instanceof BlockPost) && aboveState.getValue(BlockPost.TYPE)!=EnumPostType.ARM)
						world.setBlockState(pos, this.withProperty(BlockPost.TYPE, EnumPostType.POST));
					return;
				}
				case ARM:{
					EnumFacing f=this.getValue(BlockPost.DIRECTION).getOpposite();
					IBlockState state=world.getBlockState(pos.offset(f));
					if(state!=null && !(state.getBlock() instanceof BlockPost)){
						world.setBlockToAir(pos);
						return;
					}
					
					if(BlockPost.canConnect(world, pos, EnumFacing.UP)){
						world.setBlockState(pos, this.withProperty(BlockPost.FLIP, false), 3);
					}
					
					boolean bool=BlockPost.canConnect(world, pos, EnumFacing.DOWN);
					world.setBlockState(pos, this.withProperty(BlockPost.FLIP, bool), 3);
					
					return;
				}
			}
		}
		
		static AxisAlignedBB stateBounds(IBlockState state){
			switch(state.getValue(TYPE)){
				case ARM:{
					EnumFacing facing=state.getValue(DIRECTION);
					boolean flipped=state.getValue(FLIP);
					
					double minY=flipped?0.0:0.34375;
					double maxY=flipped?0.65625:1.0;
					
					double minX=(facing==EnumFacing.EAST) ?0.0:0.3125;
					double maxX=(facing==EnumFacing.WEST) ?1.0:0.6875;
					double minZ=(facing==EnumFacing.SOUTH)?0.0:0.3125;
					double maxZ=(facing==EnumFacing.NORTH)?1.0:0.6875;
					
					return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
				}
				default: return POST_SHAPE;
			}
		}
	}
}
