package twistedgate.immersiveposts.common.blocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableMap;

import blusunrize.immersiveengineering.api.IPostBlock;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext.Builder;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockFence;
//import net.minecraft.block.BlockWall;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.properties.IProperty;
//import net.minecraft.block.properties.PropertyBool;
//import net.minecraft.block.properties.PropertyDirection;
//import net.minecraft.block.properties.PropertyEnum;
//import net.minecraft.block.state.BlockFaceShape;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.init.Blocks;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.BlockRenderLayer;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.EnumFacing.Axis;
//import net.minecraft.util.EnumHand;
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraft.util.NonNullList;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.RayTraceResult;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.util.text.TextComponentString;
//import net.minecraft.util.text.TextComponentTranslation;
//import net.minecraft.world.IBlockAccess;
//import net.minecraft.world.World;
//import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twistedgate.immersiveposts.IPOConfig;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.enums.EnumPostType;
import twistedgate.immersiveposts.utils.BlockHelper;

import javax.annotation.Nullable;

/**
 * All-in-one package. Containing everything into one neat class is the best.
 * @author TwistedGate
 */
public class BlockPost extends IPOBlockBase implements IPostBlock, IWaterLoggable {
																								// -> note: also directly possible from coords, for now just from existing aabb
	public static final VoxelShape POST_SHAPE = VoxelShapes.create(0.3125, 0.0, 0.3125, 0.6875, 1.0, 0.6875);
	public static final VoxelShape LPARM_NORTH_BOUNDS= VoxelShapes.create(0.3125, 0.25, 0.0, 0.6875, 0.75, 0.3125);
	public static final VoxelShape LPARM_SOUTH_BOUNDS= VoxelShapes.create(0.3125, 0.25, 0.6875, 0.6875, 0.75, 1.0);
	public static final VoxelShape LPARM_EAST_BOUNDS = VoxelShapes.create(0.6875, 0.25, 0.3125, 1.0, 0.75, 0.6875);
	public static final VoxelShape LPARM_WEST_BOUNDS = VoxelShapes.create(0.0, 0.25, 0.3125, 0.3125, 0.75, 0.6875);
	public static final BooleanProperty LPARM_NORTH=BooleanProperty.create("parm_north");
	public static final BooleanProperty LPARM_EAST=BooleanProperty.create("parm_east");
	public static final BooleanProperty LPARM_SOUTH=BooleanProperty.create("parm_south");
	public static final BooleanProperty LPARM_WEST=BooleanProperty.create("parm_west");
	public static final DirectionProperty FACING=DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final EnumProperty<EnumPostType> TYPE=EnumProperty.create("type", EnumPostType.class);
	public static final BooleanProperty FLIP=BooleanProperty.create("flip");
	
	protected EnumPostMaterial postMaterial;

	public BlockPost(EnumPostMaterial postMaterial){
		super(postMaterial.getName(), postMaterial.getProperties());
		this.postMaterial=postMaterial;
		setDefaultState(getStateContainer().getBaseState()
			.with(FACING, Direction.NORTH)
			.with(FLIP, false)
			.with(TYPE, EnumPostType.POST)
			.with(LPARM_NORTH, false)
			.with(LPARM_EAST, false)
			.with(LPARM_SOUTH, false)
			.with(LPARM_WEST, false)
		);
	}
	
	@Deprecated
	public BlockPost(Material blockMaterial, EnumPostMaterial postMaterial){
		super(postMaterial.getName(), postMaterial.getProperties());
		this.postMaterial=postMaterial;
		/*
		setResistance(5.0F);
		setHardness(3.0F);
		if(this.postMaterial==EnumPostMaterial.URANIUM)
			setLightLevel(8);
		
		if(this.postMaterial==EnumPostMaterial.WOOD) setHarvestLevel("axe", 0);
		else setHarvestLevel("pickaxe", 1);
		*/
		setDefaultState(getDefaultState()
				.with(FACING, Direction.NORTH)
				.with(FLIP, false)
				.with(TYPE, EnumPostType.POST)
				.with(LPARM_NORTH, false)
				.with(LPARM_EAST, false)
				.with(LPARM_SOUTH, false)
				.with(LPARM_WEST, false)
				);
	}
	
	public final EnumPostMaterial getPostMaterial(){
		return this.postMaterial;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer(){
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(FACING, FLIP, TYPE, LPARM_NORTH, LPARM_EAST, LPARM_SOUTH, LPARM_WEST, WATERLOGGED);
		// --> Das war:
		//			protected BlockStateContainer createBlockState(){
		//				return new BlockStateContainer(this, new IProperty<?>[]{
		//					FACING, FLIP, TYPE,
		//					LPARM_NORTH, LPARM_EAST, LPARM_SOUTH, LPARM_WEST,
		//				}){
		//					@Override
		//					protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties){
		//						return new PostState(block, properties);
		//					}
		//				};
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockState state = super.getStateForPlacement(context);
		// IWaterLoggable
		IFluidState fs = context.getWorld().getFluidState(context.getPos());
		state = state.with(WATERLOGGED,fs.getFluid()==Fluids.WATER);
		return state;
	}

	@Override
	public boolean canSpawnInBlock()
	{ return false; }

	@Override
	@SuppressWarnings("deprecation")
	public PushReaction getPushReaction(BlockState state)
	{ return PushReaction.BLOCK; }

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
	{
		if(state.get(WATERLOGGED)) return false;
		return super.propagatesSkylightDown(state, reader, pos);
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos)
	{
		if(state.get(WATERLOGGED)) world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		return state;
	}

	@Override
	@SuppressWarnings("deprecation")
	public List<ItemStack> getDrops(BlockState state, Builder builder){
		if(state.get(TYPE).id()<2)
			return Arrays.asList(this.postMaterial.getItemStack());
		
		return Collections.emptyList();
	}



	// TODO Find the new way of doing random display tick stuff

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		// That method replaces -->
			//		public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand){
			//			if(this.postMaterial==EnumPostMaterial.URANIUM){
			//				if(stateIn.get(TYPE)!=EnumPostType.ARM && rand.nextFloat()<0.125F){
			//					double x=pos.getX()+0.375+0.25*rand.nextDouble();
			//					double y=pos.getY()+rand.nextDouble();
			//					double z=pos.getZ()+0.375+0.25*rand.nextDouble();
			//					worldIn.spawnParticle(EnumParticleTypes.REDSTONE, x,y,z, -1.0, 1.0, 0.25);
			//					worldIn.addParticle(null, true, x, y, z, -1.0, 1.0, 0.25);
			//				}
			//			}
	}

	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player){
		return this.postMaterial.getItemStack();
	}
	
	@Override
	public boolean canConnectTransformer(IBlockReader world, BlockPos pos){
		return world.getBlockState(pos).get(TYPE).id()<2;
	}


	//// For earlier block testing just the post shape, eventually returning the extended state shape

	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader source, BlockPos pos, ISelectionContext selectionContext)
	{ return POST_SHAPE; }

	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext selectionContext)
	{	return getShape(state, world, pos, selectionContext); }


			/*
			// TODO Collision Handling Part 1
			public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState){
				List<AxisAlignedBB> list=getSelectionBounds(state, worldIn, pos);
				if(list!=null && !list.isEmpty())
					for(AxisAlignedBB aabb:list){
						aabb=aabb.offset(pos);
						if(aabb!=null && entityBox.intersects(aabb))
							collidingBoxes.add(aabb);
					}

				//super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
			}

			// TODO Collision Handling Part 2
			public RayTraceResult collisionRayTrace(BlockState state, World worldIn, BlockPos pos, Vec3d start, Vec3d end){
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
			}*/


			/* This just includes the mini-arms to the selection bounds /
			private List<AxisAlignedBB> getSelectionBounds(BlockState state, World world, BlockPos pos){
				state=state.getExtendedState(world, pos);

				List<AxisAlignedBB> bounds=null;

				if(state.get(TYPE).id()<2){
					bounds=new ArrayList<>(5); //Let's start with a cap of 5

					if(state.get(LPARM_NORTH)) bounds.add(LPARM_NORTH_BOUNDS);
					if(state.get(LPARM_SOUTH)) bounds.add(LPARM_SOUTH_BOUNDS);
					if(state.get(LPARM_EAST)) bounds.add(LPARM_EAST_BOUNDS);
					if(state.get(LPARM_WEST)) bounds.add(LPARM_WEST_BOUNDS);

					bounds.add(POST_SHAPE);
				}

				return bounds;
			}*/


	@Override
	@SuppressWarnings("deprecation")
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit){
		if(!worldIn.isRemote){
			ItemStack held=playerIn.getHeldItemMainhand();
			if(EnumPostMaterial.isFenceItem(held)){
				if(!held.isItemEqual(this.postMaterial.getItemStack())){
					playerIn.sendStatusMessage(new TranslationTextComponent("immersiveposts.expectedlocal", new StringTextComponent(this.postMaterial.getItemStack().getDisplayName().getString())), true);
					return true;
				}
				
				if(!IPOConfig.isEnabled(EnumPostMaterial.getFrom(held))){
					return true;
				}
				
				for(int y=0;y<(worldIn.getActualHeight()-pos.getY());y++){
					BlockPos nPos=pos.add(0,y,0);
					
					if((BlockHelper.getBlockFrom(worldIn, nPos) instanceof BlockPost)){
						BlockState s=worldIn.getBlockState(nPos);
						if(s.get(BlockPost.TYPE)==EnumPostType.ARM && s.get(BlockPost.FLIP)){
							return true;
						}
						
						BlockPos up=nPos.offset(Direction.UP);
						if((BlockHelper.getBlockFrom(worldIn, up) instanceof BlockPost)){
							s=worldIn.getBlockState(up);
							if(s.get(BlockPost.TYPE)==EnumPostType.ARM){
								return true;
							}
						}
					}
					
					if(worldIn.isAirBlock(nPos)){
						BlockState fb=EnumPostMaterial.getPostStateFrom(held);
						if(fb!=null && !playerIn.getPosition().equals(nPos) && worldIn.setBlockState(nPos, fb)){
							if(!playerIn.isCreative()){
								held.shrink(1);
							}
						}
						return true;
						
					}else if(!(worldIn.getBlockState(nPos).getBlock() instanceof BlockPost)){
						return true;
					}
				}
			}else if(Utils.isHammer(held)){
				switch(state.get(TYPE)){
					case POST:case POST_TOP:{
						Direction facing=hit.getFace();
						BlockState defaultState=getDefaultState().with(TYPE, EnumPostType.ARM);
						switch(facing){
							case NORTH:case EAST:case SOUTH:case WEST:{
								BlockPos nPos=pos.offset(facing);
								if(worldIn.isAirBlock(nPos)){
									defaultState=defaultState.with(FACING, facing);
									worldIn.setBlockState(nPos, defaultState);
									//defaultState.neighborChanged(worldIn, nPos, this, null);
								}else if(BlockHelper.getBlockFrom(worldIn, nPos)==this){
									switch(worldIn.getBlockState(nPos).get(TYPE)){
										case ARM:{
											worldIn.setBlockState(nPos, Blocks.AIR.getDefaultState());
											return true;
										}
										case EMPTY:{
											worldIn.setBlockState(nPos, Blocks.AIR.getDefaultState());
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
						Direction bfacing=state.get(FACING);
						if(worldIn.isAirBlock(pos.offset(bfacing))){
							worldIn.setBlockState(pos.offset(bfacing), state.with(TYPE, EnumPostType.ARM_DOUBLE));
							worldIn.setBlockState(pos, state.with(TYPE, EnumPostType.EMPTY));
						}
						return true;
					}
					case ARM_DOUBLE:{
						Direction bfacing=state.get(FACING);
						worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
						worldIn.setBlockState(pos.offset(bfacing.getOpposite()), state.with(TYPE, EnumPostType.ARM));
						return true;
					}
					case EMPTY:{
						Direction bfacing=state.get(FACING);
						worldIn.setBlockState(pos, state.with(TYPE, EnumPostType.ARM));
						worldIn.setBlockState(pos.offset(bfacing), Blocks.AIR.getDefaultState());
						return true;
					}
				}
			}
		}
		
		return Utils.isHammer(playerIn.getHeldItemMainhand()) || EnumPostMaterial.isFenceItem(playerIn.getHeldItemMainhand());
	}
	
	
	public static boolean canConnect(IBlockReader worldIn, BlockPos posIn, Direction facingIn){
		BlockPos nPos=posIn.offset(facingIn);
		
		BlockState otherState=worldIn.getBlockState(nPos);
		Block otherBlock=otherState.getBlock();
		
		if(otherBlock==Blocks.AIR) return false; // Go straight out if air, no questions asked.
		
		if(facingIn==Direction.DOWN || facingIn==Direction.UP){
			AxisAlignedBB box=otherState.getCollisionShape(worldIn, nPos).getBoundingBox();
			switch(facingIn){
				case UP:	return box.minY==0.0;
				case DOWN:{
					boolean bool=otherBlock instanceof BlockPost;
					return !bool && box.maxY==1.0;
				}
				default:	return false;
			}
		}
		
		if(otherBlock instanceof BlockPost) return false;
		
		// TODO Re-add the anti-fence connection.
		//if(otherState.getBlockFaceShape(worldIn, nPos, facingIn)==BlockFaceShape.MIDDLE_POLE) return false;
		
		AxisAlignedBB box=otherState.getCollisionShape(worldIn, nPos).getBoundingBox();
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
	
	public static class PostState extends BlockState{
		
		public PostState(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> properties){
			super(blockIn, properties);
		}

		@Override
		public BlockState getExtendedState(IBlockReader world, BlockPos pos){
			if(this.getBlockState().get(TYPE).id()>1){
				return this.getBlockState()
						.with(LPARM_NORTH, false)
						.with(LPARM_EAST, false)
						.with(LPARM_SOUTH, false)
						.with(LPARM_WEST, false);
				/*
				 * canConnect is rather time consuming, so this is an attempt to speed this up.
				 */
			}
			
			boolean b0=canConnect(world, pos, Direction.NORTH);
			boolean b1=canConnect(world, pos, Direction.EAST);
			boolean b2=canConnect(world, pos, Direction.SOUTH);
			boolean b3=canConnect(world, pos, Direction.WEST);
			
			return this.getBlockState()
					.with(LPARM_NORTH, b0)
					.with(LPARM_EAST, b1)
					.with(LPARM_SOUTH, b2)
					.with(LPARM_WEST, b3);
		}
		
		/*
		@Override
		public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockPos pos, Direction face){
			BlockState state=worldIn.getBlockState(pos.offset(face));
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
		*/
		
		@Override
		public VoxelShape getShape(IBlockReader worldIn, BlockPos pos){
			return stateBounds(this);
		}
		
		@Override
		public boolean isFlammable(IBlockReader world, BlockPos pos, Direction face){
			return false;
		}
		
		@Override
		public boolean isLadder(IWorldReader world, BlockPos pos, LivingEntity entity){
			return true;
		}
		
		@Override
		public void onNeighborChange(IWorldReader world, BlockPos pos, BlockPos neighbor){
			//super.onNeighborChange(world, pos, neighbor);
			
			// TODO Maybe it's better to not cast IWorldReader to World
			neighborChanged((World)world, pos, world.getBlockState(neighbor).getBlock(), neighbor);
		}
		
		public void neighborChanged(World world, BlockPos pos, Block block, BlockPos fromPos){
			EnumPostType thisType=this.getBlockState().get(BlockPost.TYPE);
			
			if(thisType.id()<2){
				BlockPos belowPos=pos.offset(Direction.DOWN);
				if(BlockHelper.getBlockFrom(world, belowPos)==Blocks.AIR){
					// FIXME This has to drop the item, so find a way!
					//BlockHelper.getBlockFrom(world, pos).dropBlockAsItem(world, pos, this, 0);
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
					return;
				}
			}
			
			BlockState aboveState=world.getBlockState(pos.offset(Direction.UP));
			Block aboveBlock=aboveState.getBlock();
			switch(thisType){
				case POST:{
					if(!(aboveBlock instanceof BlockPost))
						world.setBlockState(pos, this.getBlockState().with(BlockPost.TYPE, EnumPostType.POST_TOP));
					return;
				}
				case POST_TOP:{
					if((aboveBlock instanceof BlockPost) && aboveState.get(BlockPost.TYPE)!=EnumPostType.ARM)
						world.setBlockState(pos, this.getBlockState().with(BlockPost.TYPE, EnumPostType.POST));
					return;
				}
				case ARM:{
					Direction f=this.getBlockState().get(BlockPost.FACING).getOpposite();
					BlockState state=world.getBlockState(pos.offset(f));
					if(state!=null && !(state.getBlock() instanceof BlockPost)){
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
						return;
					}
					
					if(aboveBlock!=Blocks.AIR && (aboveBlock instanceof BlockPost && aboveState.get(BlockPost.TYPE)!=EnumPostType.ARM)){
						world.setBlockState(pos, this.getBlockState().with(BlockPost.FLIP, false));
					}else{
						boolean bool=BlockPost.canConnect(world, pos, Direction.DOWN);
						world.setBlockState(pos, this.getBlockState().with(BlockPost.FLIP, bool));
					}
					return;
				}
				case ARM_DOUBLE:{
					Direction f=this.getBlockState().get(BlockPost.FACING).getOpposite();
					BlockState state=world.getBlockState(pos.offset(f));
					if(state!=null && !(state.getBlock() instanceof BlockPost)){
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
					}
					return;
				}
				case EMPTY:{
					Direction f=this.getBlockState().get(BlockPost.FACING).getOpposite();
					BlockState state=world.getBlockState(pos.offset(f));
					if(state!=null && !(state.getBlock() instanceof BlockPost)){
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
						return;
					}
					
					state=world.getBlockState(pos);
					f=state.get(FACING);
					state=world.getBlockState(pos.offset(f));
					if(state.getBlock()==Blocks.AIR){
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
					}
				}
			}
		}
		
		private static final VoxelShape X_BOUNDS= VoxelShapes.create(0.0, 0.34375, 0.3125, 1.0, 1.0, 0.6875);
		private static final VoxelShape Z_BOUNDS= VoxelShapes.create(0.3125, 0.34375, 0.0, 0.6875, 1.0, 1.0);
		static VoxelShape stateBounds(BlockState state){
			switch(state.get(TYPE)){
				case ARM:case ARM_DOUBLE:{
					Direction facing=state.get(FACING);
					boolean flipped=state.get(FLIP);
					
					double minY=flipped?0.0:0.34375;
					double maxY=flipped?0.65625:1.0;
					
					double minX=(facing==Direction.EAST) ?0.0:0.3125;
					double maxX=(facing==Direction.WEST) ?1.0:0.6875;
					double minZ=(facing==Direction.SOUTH)?0.0:0.3125;
					double maxZ=(facing==Direction.NORTH)?1.0:0.6875;
					
					return VoxelShapes.create(minX, minY, minZ, maxX, maxY, maxZ);
				}
				case EMPTY:{
					Direction facing=state.get(FACING);
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
