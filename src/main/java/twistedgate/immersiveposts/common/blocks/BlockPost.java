package twistedgate.immersiveposts.common.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.google.common.collect.ImmutableMap;

import blusunrize.immersiveengineering.api.IPostBlock;
import blusunrize.immersiveengineering.common.blocks.metal.BlockMetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDecoration;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockWoodenDecoration;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.enums.EnumPostType;
import twistedgate.immersiveposts.utils.BlockHelper;

/**
 * All-in-one package. Containing everything into one neat class is the best.
 * @author TwistedGate
 */
public class BlockPost extends IPOBlockBase implements IPostBlock{
	public static final AxisAlignedBB POST_SHAPE=new AxisAlignedBB(0.3125, 0.0, 0.3125, 0.6875, 1.0, 0.6875);
	public static final AxisAlignedBB LPARM_NORTH_BOUNDS=new AxisAlignedBB(0.3125, 0.25, 0.0, 0.6875, 0.75, 0.3125);
	public static final AxisAlignedBB LPARM_SOUTH_BOUNDS=new AxisAlignedBB(0.3125, 0.25, 0.6875, 0.6875, 0.75, 1.0);
	public static final AxisAlignedBB LPARM_EAST_BOUNDS=new AxisAlignedBB(0.6875, 0.25, 0.3125, 1.0, 0.75, 0.6875);
	public static final AxisAlignedBB LPARM_WEST_BOUNDS=new AxisAlignedBB(0.0, 0.25, 0.3125, 0.3125, 0.75, 0.6875);
	
	
	public static final PropertyBool LPARM_NORTH=PropertyBool.create("parm_north");
	public static final PropertyBool LPARM_EAST=PropertyBool.create("parm_east");
	public static final PropertyBool LPARM_SOUTH=PropertyBool.create("parm_south");
	public static final PropertyBool LPARM_WEST=PropertyBool.create("parm_west");
	
	public static final PropertyDirection FACING=PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyEnum<EnumPostType> TYPE=PropertyEnum.create("type", EnumPostType.class);
	public static final PropertyBool FLIP=PropertyBool.create("flip");
	
	protected EnumPostMaterial postMaterial;
	public BlockPost(Material blockMaterial, EnumPostMaterial postMaterial){
		super(blockMaterial, postMaterial.getName());
		this.postMaterial=postMaterial;
		
		setResistance(5.0F);
		setHardness(3.0F);
		if(this.postMaterial==EnumPostMaterial.URANIUM)
			setLightLevel(8);
		
		if(this.postMaterial==EnumPostMaterial.WOOD) setHarvestLevel("axe", 0);
		else setHarvestLevel("pickaxe", 1);
		
		setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH)
				.withProperty(FLIP, false)
				.withProperty(TYPE, EnumPostType.POST)
				.withProperty(LPARM_NORTH, false)
				.withProperty(LPARM_EAST, false)
				.withProperty(LPARM_SOUTH, false)
				.withProperty(LPARM_WEST, false)
				);
	}
	
	public final EnumPostMaterial getPostMaterial(){
		return this.postMaterial;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer(){
		return BlockRenderLayer.SOLID;
	}
	
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, new IProperty<?>[]{
			FACING, FLIP, TYPE,
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
		if(state.getValue(TYPE).id()<2)
			drops.add(this.postMaterial.getItemStack());
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		switch(state.getValue(TYPE)){
			case POST: return 0;
			case POST_TOP: return 1;
			case ARM:{
				int rot;
				switch(state.getValue(FACING)){
					case EAST: rot=1;break;
					case SOUTH:rot=2;break;
					case WEST: rot=3;break;
					default:   rot=0; // North, Up and Down
				}
				
				return (state.getValue(FLIP)?6:2)+rot;
			}
			case ARM_DOUBLE:{
				switch(state.getValue(FACING)){
					case EAST: return 11;
					case SOUTH:return 12;
					case WEST: return 13;
					default:   return 10; // North, Up and Down
				}
			}
			case EMPTY: return 15;
			default: return 0;
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		IBlockState state=getDefaultState();
		if(meta==15) return state.withProperty(TYPE, EnumPostType.EMPTY);
		
		if(meta>0){
			if(meta>9) state=state.withProperty(TYPE, EnumPostType.ARM_DOUBLE);
			else if(meta>1) state=state.withProperty(TYPE, EnumPostType.ARM);
			else state=state.withProperty(TYPE, EnumPostType.POST_TOP);
			
			if(meta>=6 && meta<=9) state=state.withProperty(FLIP, true);
			
			switch((meta-2)%4){
				case 0: state=state.withProperty(FACING, EnumFacing.NORTH); break;
				case 1: state=state.withProperty(FACING, EnumFacing.EAST); break;
				case 2: state=state.withProperty(FACING, EnumFacing.SOUTH); break;
				case 3: state=state.withProperty(FACING, EnumFacing.WEST); break;
			}
		}
		
		return state;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand){
		if(this.postMaterial==EnumPostMaterial.URANIUM){
			if(stateIn.getValue(TYPE)!=EnumPostType.ARM && rand.nextFloat()<0.125F){
				double x=pos.getX()+0.375+0.25*rand.nextDouble();
				double y=pos.getY()+rand.nextDouble();
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
		return this.postMaterial.getItemStack();
	}
	
	@Override
	public boolean canConnectTransformer(IBlockAccess world, BlockPos pos){
		return world.getBlockState(pos).getValue(TYPE).id()<2;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState){
		List<AxisAlignedBB> list=getSelectionBounds(state, worldIn, pos);
		if(list!=null && !list.isEmpty())
			for(AxisAlignedBB aabb:list){
				aabb=aabb.offset(pos);
				if(aabb!=null && entityBox.intersects(aabb))
					collidingBoxes.add(aabb);
			}
		
		super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
	}
	
	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World worldIn, BlockPos pos, Vec3d start, Vec3d end){
		List<AxisAlignedBB> bounds=getSelectionBounds(state, worldIn, pos);
		if(bounds!=null && !bounds.isEmpty()){
			RayTraceResult ret=null;
			double minDist=Double.POSITIVE_INFINITY;
			for(AxisAlignedBB aabb:bounds){
				if(aabb==null) continue;
				
				RayTraceResult res=this.rayTrace(pos, start, end, aabb);
				if(res!=null){
					double dist=res.hitVec.squareDistanceTo(start);
					if(dist<minDist){
						ret=res;
						minDist=dist;
					}
				}
			}
			
			return ret;
		}
		
		return this.rayTrace(pos, start, end, state.getBoundingBox(worldIn, pos));
	}
	
	/** This just includes the mini-arms to the selection bounds */
	private List<AxisAlignedBB> getSelectionBounds(IBlockState state, World world, BlockPos pos){
		state=state.getActualState(world, pos);
		
		List<AxisAlignedBB> bounds=null;
		
		if(state.getValue(TYPE).id()<2){
			bounds=new ArrayList<>(5); //Let's start with a cap of 5
			
			if(state.getValue(LPARM_NORTH)) bounds.add(LPARM_NORTH_BOUNDS);
			if(state.getValue(LPARM_SOUTH)) bounds.add(LPARM_SOUTH_BOUNDS);
			if(state.getValue(LPARM_EAST)) bounds.add(LPARM_EAST_BOUNDS);
			if(state.getValue(LPARM_WEST)) bounds.add(LPARM_WEST_BOUNDS);
			
			bounds.add(POST_SHAPE);
		}
		
		return bounds;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(!worldIn.isRemote){
			ItemStack held=playerIn.getHeldItemMainhand();
			if(EnumPostMaterial.isFenceItem(held)){
				if(!held.isItemEqual(this.postMaterial.getItemStack())){
					playerIn.sendStatusMessage(new TextComponentTranslation("immersiveposts.expectedlocal", new TextComponentString(this.postMaterial.getItemStack().getDisplayName())), true);
					return true;
				}
				
				for(int y=0;y<(worldIn.getActualHeight()-pos.getY());y++){
					BlockPos nPos=pos.add(0,y,0);
					
					if((BlockHelper.getBlockFrom(worldIn, nPos) instanceof BlockPost)){
						IBlockState s=worldIn.getBlockState(nPos);
						if(s.getValue(BlockPost.TYPE)==EnumPostType.ARM && s.getValue(BlockPost.FLIP)){
							return true;
						}
						
						BlockPos up=nPos.offset(EnumFacing.UP);
						if((BlockHelper.getBlockFrom(worldIn, up) instanceof BlockPost)){
							s=worldIn.getBlockState(up);
							if(s.getValue(BlockPost.TYPE)==EnumPostType.ARM){
								return true;
							}
						}
					}
					
					if(worldIn.isAirBlock(nPos)){
						IBlockState fb=EnumPostMaterial.getPostStateFrom(held);
						if(fb!=null && !playerIn.getPosition().equals(nPos) && worldIn.setBlockState(nPos, fb)){
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
									defaultState=defaultState.withProperty(FACING, facing);
									worldIn.setBlockState(nPos, defaultState);
									defaultState.neighborChanged(worldIn, nPos, this, null);
								}else if(BlockHelper.getBlockFrom(worldIn, nPos)==this){
									switch(worldIn.getBlockState(nPos).getValue(TYPE)){
										case ARM:{
											worldIn.setBlockToAir(nPos);
											return true;
										}
										case EMPTY:{
											worldIn.setBlockToAir(nPos);
											return true;
										}
										default:break;
									}
								}
							}
							default:break;
						}
						return true;
					}
					case ARM:{
						EnumFacing bfacing=state.getValue(FACING);
						if(worldIn.isAirBlock(pos.offset(bfacing))){
							worldIn.setBlockState(pos.offset(bfacing), state.withProperty(TYPE, EnumPostType.ARM_DOUBLE));
							worldIn.setBlockState(pos, state.withProperty(TYPE, EnumPostType.EMPTY));
						}
						return true;
					}
					case ARM_DOUBLE:{
						EnumFacing bfacing=state.getValue(FACING);
						worldIn.setBlockToAir(pos);
						worldIn.setBlockState(pos.offset(bfacing.getOpposite()), state.withProperty(TYPE, EnumPostType.ARM));
						return true;
					}
					case EMPTY:{
						EnumFacing bfacing=state.getValue(FACING);
						worldIn.setBlockState(pos, state.withProperty(TYPE, EnumPostType.ARM));
						worldIn.setBlockToAir(pos.offset(bfacing));
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
		
		if(facingIn==EnumFacing.DOWN || facingIn==EnumFacing.UP){
			AxisAlignedBB box=otherState.getBoundingBox(worldIn, nPos);
			switch(facingIn){
				case UP:	return box.minY==0.0;
				case DOWN:{
					boolean bool=otherBlock instanceof BlockPost;
					return !bool && box.maxY==1.0;
				}
				default:	return false;
			}
		}
		
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
		
		return false;
	}
	
	
	public static class PostState extends BlockStateContainer.StateImplementation{
		public PostState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties){
			super(block, properties);
		}
		
		@Override
		public IBlockState getActualState(IBlockAccess blockAccess, BlockPos pos){
			if(this.getValue(TYPE).id()>1){
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
			
			if(thisType.id()<2){
				BlockPos belowPos=pos.offset(EnumFacing.DOWN);
				if(BlockHelper.getBlockFrom(world, belowPos)==Blocks.AIR){
					BlockHelper.getBlockFrom(world, pos).dropBlockAsItem(world, pos, this, 0);
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
					EnumFacing f=this.getValue(BlockPost.FACING).getOpposite();
					IBlockState state=world.getBlockState(pos.offset(f));
					if(state!=null && !(state.getBlock() instanceof BlockPost)){
						world.setBlockToAir(pos);
						return;
					}
					
					if(aboveBlock!=Blocks.AIR && (aboveBlock instanceof BlockPost && aboveState.getValue(BlockPost.TYPE)!=EnumPostType.ARM)){
						world.setBlockState(pos, this.withProperty(BlockPost.FLIP, false));
					}else{
						boolean bool=BlockPost.canConnect(world, pos, EnumFacing.DOWN);
						world.setBlockState(pos, this.withProperty(BlockPost.FLIP, bool));
					}
					return;
				}
				case ARM_DOUBLE:{
					EnumFacing f=this.getValue(BlockPost.FACING).getOpposite();
					IBlockState state=world.getBlockState(pos.offset(f));
					if(state!=null && !(state.getBlock() instanceof BlockPost)){
						world.setBlockToAir(pos);
					}
					return;
				}
				case EMPTY:{
					EnumFacing f=this.getValue(BlockPost.FACING).getOpposite();
					IBlockState state=world.getBlockState(pos.offset(f));
					if(state!=null && !(state.getBlock() instanceof BlockPost)){
						world.setBlockToAir(pos);
						return;
					}
					
					state=world.getBlockState(pos);
					f=state.getValue(FACING);
					state=world.getBlockState(pos.offset(f));
					if(state.getBlock()==Blocks.AIR){
						world.setBlockToAir(pos);
					}
				}
			}
		}
		
		private static final AxisAlignedBB X_BOUNDS=new AxisAlignedBB(0.0, 0.34375, 0.3125, 1.0, 1.0, 0.6875);
		private static final AxisAlignedBB Z_BOUNDS=new AxisAlignedBB(0.3125, 0.34375, 0.0, 0.6875, 1.0, 1.0);
		static AxisAlignedBB stateBounds(IBlockState state){
			switch(state.getValue(TYPE)){
				case ARM:case ARM_DOUBLE:{
					EnumFacing facing=state.getValue(FACING);
					boolean flipped=state.getValue(FLIP);
					
					double minY=flipped?0.0:0.34375;
					double maxY=flipped?0.65625:1.0;
					
					double minX=(facing==EnumFacing.EAST) ?0.0:0.3125;
					double maxX=(facing==EnumFacing.WEST) ?1.0:0.6875;
					double minZ=(facing==EnumFacing.SOUTH)?0.0:0.3125;
					double maxZ=(facing==EnumFacing.NORTH)?1.0:0.6875;
					
					return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
				}
				case EMPTY:{
					EnumFacing facing=state.getValue(FACING);
					Axis axis=facing.getAxis();
					
					if(axis==Axis.X){
						return X_BOUNDS;
					}
					return Z_BOUNDS;
				}
				default: return POST_SHAPE;
			}
		}
	}
}
