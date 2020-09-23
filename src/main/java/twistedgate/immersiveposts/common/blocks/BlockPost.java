package twistedgate.immersiveposts.common.blocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import blusunrize.immersiveengineering.api.IPostBlock;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FourWayBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twistedgate.immersiveposts.enums.EnumFlipState;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.enums.EnumPostType;

/**
 * All-in-one package. Containing everything into one neat class is the best.
 * @author TwistedGate
 */
public class BlockPost extends IPOBlockBase implements IPostBlock, IWaterLoggable{
	private static final RedstoneParticleData URAN_PARTICLE=new RedstoneParticleData(0.0F, 1.0F, 0.0F, 1.0F);
	
	public static final VoxelShape POST_SHAPE=VoxelShapes.create(0.3125, 0.0, 0.3125, 0.6875, 1.0, 0.6875);
	
	public static final VoxelShape LPARM_NORTH_BOUNDS=VoxelShapes.create(0.3125, 0.25, 0.0, 0.6875, 0.75, 0.3125);
	public static final VoxelShape LPARM_SOUTH_BOUNDS=VoxelShapes.create(0.3125, 0.25, 0.6875, 0.6875, 0.75, 1.0);
	public static final VoxelShape LPARM_EAST_BOUNDS=VoxelShapes.create(0.6875, 0.25, 0.3125, 1.0, 0.75, 0.6875);
	public static final VoxelShape LPARM_WEST_BOUNDS=VoxelShapes.create(0.0, 0.25, 0.3125, 0.3125, 0.75, 0.6875);
	
	// LPARM = (Little-)Post Arm
	public static final BooleanProperty LPARM_NORTH=BooleanProperty.create("parm_north");
	public static final BooleanProperty LPARM_EAST=BooleanProperty.create("parm_east");
	public static final BooleanProperty LPARM_SOUTH=BooleanProperty.create("parm_south");
	public static final BooleanProperty LPARM_WEST=BooleanProperty.create("parm_west");
	
	public static final BooleanProperty WATERLOGGED=BlockStateProperties.WATERLOGGED;
	
	public static final DirectionProperty FACING=DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
	public static final EnumProperty<EnumPostType> TYPE=EnumProperty.create("type", EnumPostType.class);
	
	public static final EnumProperty<EnumFlipState> FLIPSTATE=EnumProperty.create("flipstate", EnumFlipState.class);
	
	protected final EnumPostMaterial postMaterial;
	private StateContainer<Block, BlockState> altStateContainer=null;
	public BlockPost(EnumPostMaterial postMaterial){
		super(postMaterial.getName(), postMaterial.getProperties());
		this.postMaterial=postMaterial;
		
		setDefaultState(getStateContainer().getBaseState()
				.with(WATERLOGGED, false)
				.with(FACING, Direction.NORTH)
				.with(FLIPSTATE, EnumFlipState.UP)
				.with(TYPE, EnumPostType.POST_TOP)
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
	public StateContainer<Block, BlockState> getStateContainer(){
		if(this.altStateContainer==null){
			StateContainer.Builder<Block, BlockState> builder=new StateContainer.Builder<>(this);
			builder.add(
					WATERLOGGED, FACING, FLIPSTATE, TYPE,
					LPARM_NORTH, LPARM_EAST, LPARM_SOUTH, LPARM_WEST
					);
			
			this.altStateContainer=builder.create(PostState::new); // There has to be a better way for this
		}
		
		return this.altStateContainer;
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos){
		return !state.get(WATERLOGGED);
	}
	
	@Override
	public IFluidState getFluidState(BlockState state){
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context){
		BlockState state=super.getStateForPlacement(context);
		IFluidState fs=context.getWorld().getFluidState(context.getPos());
		
		state=state.with(WATERLOGGED, fs.getFluid() == Fluids.WATER);
		return state;
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos){
		if(state.get(WATERLOGGED)){
			world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return state;
	}
	
	@Override
	public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity){
		return true;
	}
	
	@Override
	public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face){
		return false;
	}
	
	@Override
	public Direction[] getValidRotations(BlockState state, IBlockReader world, BlockPos pos){
		return null;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder){
		if(state.get(TYPE).id()<2)
			return Arrays.asList(this.postMaterial.getItemStack());
		
		return Collections.emptyList();
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand){
		if(this.postMaterial==EnumPostMaterial.URANIUM){
			if(stateIn.get(TYPE)!=EnumPostType.ARM && rand.nextFloat()<0.125F){
				double x=pos.getX()+0.375+0.25*rand.nextDouble();
				double y=pos.getY()+rand.nextDouble();
				double z=pos.getZ()+0.375+0.25*rand.nextDouble();
				worldIn.addParticle(URAN_PARTICLE, x,y,z, 0.0, 0.0, 0.0);
			}
		}
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player){
		return this.postMaterial.getItemStack();
	}
	
	@Override
	public boolean canConnectTransformer(IBlockReader world, BlockPos pos){
		return world.getBlockState(pos).get(TYPE).id()<2;
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit){
		if(!worldIn.isRemote){
			ItemStack held=playerIn.getHeldItemMainhand();
			if(EnumPostMaterial.isValidItem(held)){
				if(!held.isItemEqual(this.postMaterial.getItemStack())){
					playerIn.sendStatusMessage(new TranslationTextComponent("immersiveposts.expectedlocal", this.postMaterial.getItemStack().getDisplayName()), true);
					return ActionResultType.SUCCESS;
				}
				
				for(int y=0;y<(worldIn.getActualHeight()-pos.getY());y++){
					BlockPos nPos=pos.add(0,y,0);
					
					BlockState nState=worldIn.getBlockState(nPos);
					if(nState.getBlock() instanceof BlockPost){
						EnumPostType type=nState.get(BlockPost.TYPE);
						if(!(type==EnumPostType.POST || type==EnumPostType.POST_TOP) && nState.get(BlockPost.FLIPSTATE)==EnumFlipState.DOWN){
							return ActionResultType.SUCCESS;
						}else{
							nState=worldIn.getBlockState(nPos.offset(Direction.UP));
							if(nState.getBlock() instanceof BlockPost){
								type=nState.get(BlockPost.TYPE);
								if(!(type==EnumPostType.POST || type==EnumPostType.POST_TOP)){
									return ActionResultType.SUCCESS;
								}
							}
						}
					}
					
					if(worldIn.isAirBlock(nPos) || worldIn.getBlockState(nPos).getBlock()==Blocks.WATER){
						BlockState fb=EnumPostMaterial.getPostStateFrom(held)
								.with(WATERLOGGED, worldIn.getBlockState(nPos).getBlock()==Blocks.WATER);
						
						if(fb!=null && !playerIn.getPosition().equals(nPos) && worldIn.setBlockState(nPos, fb)){
							if(!playerIn.isCreative()){
								held.shrink(1);
							}
						}
						return ActionResultType.SUCCESS;
						
					}else if(!(worldIn.getBlockState(nPos).getBlock() instanceof BlockPost)){
						return ActionResultType.SUCCESS;
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
								BlockState nState=worldIn.getBlockState(nPos);
								
								if(nState.isAir(worldIn, nPos) || nState.getBlock()==Blocks.WATER){
									defaultState=defaultState.with(FACING, facing)
											.with(WATERLOGGED, nState.getBlock()==Blocks.WATER);
									
									worldIn.setBlockState(nPos, defaultState);
									defaultState.neighborChanged(worldIn, nPos, this, null, false);
								}else if(getBlockFrom(worldIn, nPos)==this){
									switch(nState.get(TYPE)){
										case ARM:{
											worldIn.setBlockState(nPos, nState.get(WATERLOGGED)?Blocks.WATER.getDefaultState():Blocks.AIR.getDefaultState());
											return ActionResultType.SUCCESS;
										}
										case EMPTY:{
											worldIn.setBlockState(nPos, nState.get(WATERLOGGED)?Blocks.WATER.getDefaultState():Blocks.AIR.getDefaultState());
											return ActionResultType.SUCCESS;
										}
										default:break;
									}
								}
							}
							default:break;
						}
						return ActionResultType.SUCCESS;
					}
					case ARM:{
						Direction bfacing=state.get(FACING);
						BlockPos offset=pos.offset(bfacing);
						if(worldIn.isAirBlock(offset) || worldIn.getBlockState(offset).getBlock() == Blocks.WATER){
							worldIn.setBlockState(offset, state.with(TYPE, EnumPostType.ARM_DOUBLE).with(WATERLOGGED, worldIn.getBlockState(offset).getBlock() == Blocks.WATER));
							worldIn.setBlockState(pos, state.with(TYPE, EnumPostType.EMPTY));
						}
						return ActionResultType.SUCCESS;
					}
					case ARM_DOUBLE:{
						Direction bfacing=state.get(FACING);
						worldIn.setBlockState(pos, state.get(WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState());
						worldIn.setBlockState(pos.offset(bfacing.getOpposite()), state.with(TYPE, EnumPostType.ARM));
						return ActionResultType.SUCCESS;
					}
					case EMPTY:{
						Direction bfacing=state.get(FACING);
						worldIn.setBlockState(pos, state.with(TYPE, EnumPostType.ARM));
						worldIn.setBlockState(pos.offset(bfacing), state.get(WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState());
						return ActionResultType.SUCCESS;
					}
				}
			}
		}
		
		if(Utils.isHammer(playerIn.getHeldItemMainhand()) || EnumPostMaterial.isValidItem(playerIn.getHeldItemMainhand())){
			return ActionResultType.SUCCESS;
		}
		
		return ActionResultType.FAIL;
	}
	
	
	public static boolean canConnect(IBlockReader worldIn, BlockPos posIn, Direction facingIn){
		BlockPos nPos=posIn.offset(facingIn);
		
		BlockState otherState=worldIn.getBlockState(nPos);
		Block otherBlock=otherState.getBlock();
		
		// Go straight out if air, no questions asked.
		if(otherBlock==Blocks.AIR)
			return false;
		
		// Secondary, more indepth check
		if(otherBlock.isAir(otherState, worldIn, nPos) || otherBlock instanceof FourWayBlock || otherBlock instanceof BlockPost)
			return false;
		
		if(facingIn==Direction.DOWN || facingIn==Direction.UP){
			VoxelShape shape=otherState.getShape(worldIn, nPos);
			if(!shape.isEmpty()) {
				AxisAlignedBB box=shape.getBoundingBox();
				switch(facingIn){
					case UP:	return box.minY==0.0;
					case DOWN:{
						boolean bool=otherBlock instanceof BlockPost;
						return !bool && box.maxY==1.0;
					}
					default: break;
				}
				return false;
			}
		}
		
		VoxelShape shape=otherState.getShape(worldIn, nPos);
		if(!shape.isEmpty()){
			AxisAlignedBB box=shape.getBoundingBox();
			boolean b;
			
			switch(facingIn){
				case NORTH:	b=(box.maxZ==1.0);break;
				case SOUTH:	b=(box.minZ==0.0);break;
				case WEST:	b=(box.maxX==1.0);break;
				case EAST:	b=(box.minX==0.0);break;
				default:	b=false;
			}
			
			if(b){
				if(facingIn.getAxis()==Axis.Z && box.minX>0.0 && box.maxX<1.0) return true;
				if(facingIn.getAxis()==Axis.X && box.minZ>0.0 && box.maxZ<1.0) return true;
			}
		}
		
		return false;
	}
	
	public static class PostState extends BlockState{
		public PostState(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> properties){
			super(blockIn, properties);
		}
		
		@Override
		public void onBlockAdded(World worldIn, BlockPos pos, BlockState oldState, boolean isMoving){}
		
		@Override
		public BlockState updatePostPlacement(Direction face, BlockState queried, IWorld world, BlockPos pos, BlockPos offsetPos){
			if(this.get(TYPE).id()>1){
				return this;
				/*
				 * canConnect is rather time consuming, so this is an attempt to speed this up.
				 */
			}
			
			boolean b0=canConnect(world, pos, Direction.NORTH);
			boolean b1=canConnect(world, pos, Direction.EAST);
			boolean b2=canConnect(world, pos, Direction.SOUTH);
			boolean b3=canConnect(world, pos, Direction.WEST);
			
			return this.with(LPARM_NORTH, b0)
						.with(LPARM_EAST, b1)
						.with(LPARM_SOUTH, b2)
						.with(LPARM_WEST, b3);
		}
		
		@Override // Again, just no..
		public BlockState rotate(Rotation rot){
			return this;
		}
		
		@Override
		@OnlyIn(Dist.CLIENT)
		public boolean isSideInvisible(BlockState state, Direction face){
			return false;
		}
		
		@Override
		public VoxelShape getShape(IBlockReader worldIn, BlockPos pos, ISelectionContext context){
			return stateBounds(this);
		}
		
		@Override
		public VoxelShape getCollisionShape(IBlockReader worldIn, BlockPos pos, ISelectionContext context){
			return getShape(worldIn, pos, context);
		}
		
		@Override
		public void neighborChanged(World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving){
			if(world.isRemote) return;
			
			updateState(world, pos);
		}
		
		private void updateState(World world, BlockPos pos){
			EnumPostType thisType=this.get(TYPE);
			
			if(thisType.id()<=1){ // If POST (0) or POST_TOP (1)
				BlockState state=world.getBlockState(pos.offset(Direction.DOWN));
				if(state.getBlock()==Blocks.AIR || state.getBlock()==Blocks.WATER){
					Block.spawnDrops(this, world, pos);
					replaceSelf(world, pos);
					return;
				}
			}
			
			BlockState aboveState=world.getBlockState(pos.offset(Direction.UP));
			Block aboveBlock=aboveState.getBlock();
			switch(thisType){
				case POST:{
					if(!(aboveBlock instanceof BlockPost)){
						world.setBlockState(pos, this.with(TYPE, EnumPostType.POST_TOP));
					}
					return;
				}
				case POST_TOP:{
					if((aboveBlock instanceof BlockPost) && aboveState.get(TYPE)==EnumPostType.POST_TOP){
						world.setBlockState(pos, this.with(TYPE, EnumPostType.POST));
					}
					return;
				}
				case ARM:{
					Direction f=this.get(FACING).getOpposite();
					BlockState state=world.getBlockState(pos.offset(f));
					
					if(state!=null && !(state.getBlock() instanceof BlockPost)){
						replaceSelf(world, pos);
					}else{
						world.setBlockState(pos, this.with(FLIPSTATE, getFlipState(world, pos)));
					}
					
					return;
				}
				case ARM_DOUBLE:{
					Direction f=this.get(FACING).getOpposite();
					BlockState state=world.getBlockState(pos.offset(f));
					if(state!=null && !(state.getBlock() instanceof BlockPost)){
						replaceSelf(world, pos);
					}
					
					return;
				}
				case EMPTY:{
					BlockState state=world.getBlockState(pos.offset(this.get(FACING).getOpposite()));
					if(state!=null && !(state.getBlock() instanceof BlockPost)){
						replaceSelf(world, pos);
						return;
					}
					
					state=world.getBlockState(pos.offset(this.get(FACING)));
					if(state.getBlock()==Blocks.AIR || state.getBlock()==Blocks.WATER){
						replaceSelf(world, pos);
					}
				}
			}
		}
		
		/** Replaces itself with Air or with Water if Waterlogged. (Convenience Method) */
		private void replaceSelf(World world, BlockPos pos){
			world.setBlockState(pos, this.get(WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState());
		}
		
		private EnumFlipState getFlipState(IBlockReader world, BlockPos pos){
			BlockState aboveState=world.getBlockState(pos.offset(Direction.UP));
			BlockState belowState=world.getBlockState(pos.offset(Direction.DOWN));
			
			Block aboveBlock=aboveState.getBlock();
			Block belowBlock=belowState.getBlock();
			
			boolean up=BlockPost.canConnect(world, pos, Direction.UP) && ((aboveBlock instanceof BlockPost)?aboveState.get(TYPE)!=EnumPostType.ARM:true);
			boolean down=BlockPost.canConnect(world, pos, Direction.DOWN) && ((belowBlock instanceof BlockPost)?belowState.get(TYPE)!=EnumPostType.ARM:true);
			
			EnumFlipState flipState;
			if(up && down) flipState=EnumFlipState.BOTH;
			else if(down) flipState=EnumFlipState.DOWN;
			else flipState=EnumFlipState.UP;
			
			return flipState;
		}
		
		private static final VoxelShape X_BOUNDS=VoxelShapes.create(0.0, 0.34375, 0.3125, 1.0, 1.0, 0.6875);
		private static final VoxelShape Z_BOUNDS=VoxelShapes.create(0.3125, 0.34375, 0.0, 0.6875, 1.0, 1.0);
		private static final Map<Integer, VoxelShape> shapeCache=new HashMap<>();
		static VoxelShape stateBounds(BlockState state){
			EnumPostType type=state.get(TYPE);
			switch(type){
				case ARM:case ARM_DOUBLE:{
					Direction dir=state.get(FACING);
					EnumFlipState flipstate=state.get(FLIPSTATE);
					
					/*
						Bit-6 = FlipDown
						Bit-5 = FlipUp
						Bit-4 = West
						Bit-3 = South
						Bit-2 = East
						Bit-1 = North
						
						If Bit5 and Bit6 are both 1 then its EnumFlipState.BOTH
						By default it's EnumFlipState.UP
					 */
					int id=0x00;
					
					switch(flipstate){
						case UP:	id=0x10; break;
						case DOWN:	id=0x20; break;
						case BOTH:	id=0x30; break;
					}
					
					switch(dir){
						case WEST:	id|=0x08;
						case SOUTH:	id|=0x04;
						case EAST:	id|=0x02;
						default:	id|=0x01; // Basicly default to North
					}
					
					if(!shapeCache.containsKey(id)){
						double minY=0.0;
						double maxY=1.0;
						switch(flipstate){
							case UP:   minY=0.34375; maxY=1.0; break;
							case DOWN: minY=0.0; maxY=0.65625; break;
							case BOTH: minY=0.0; maxY=1.0; break;
						}
						
						double minX=(dir==Direction.EAST) ?0.0:0.3125;
						double maxX=(dir==Direction.WEST) ?1.0:0.6875;
						double minZ=(dir==Direction.SOUTH)?0.0:0.3125;
						double maxZ=(dir==Direction.NORTH)?1.0:0.6875;
						
						VoxelShape shape=VoxelShapes.create(minX, minY, minZ, maxX, maxY, maxZ);
						shapeCache.put(id, shape);
						return shape;
					}
					
					return shapeCache.get(id);
				}
				case EMPTY:{
					if(state.get(FACING).getAxis()==Axis.X)
						return X_BOUNDS;
					
					return Z_BOUNDS;
				}
				default:{
					VoxelShape shape=POST_SHAPE;
					
					if(state.get(LPARM_NORTH)) shape=VoxelShapes.or(shape, LPARM_NORTH_BOUNDS);
					if(state.get(LPARM_SOUTH)) shape=VoxelShapes.or(shape, LPARM_SOUTH_BOUNDS);
					if(state.get(LPARM_EAST)) shape=VoxelShapes.or(shape, LPARM_EAST_BOUNDS);
					if(state.get(LPARM_WEST)) shape=VoxelShapes.or(shape, LPARM_WEST_BOUNDS);
					
					return shape;
				}
			}
		}
	}
}
