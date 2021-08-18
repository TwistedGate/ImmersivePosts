package twistedgate.immersiveposts.common.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import blusunrize.immersiveengineering.api.IPostBlock;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twistedgate.immersiveposts.enums.EnumHTrussType;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

public class HorizontalTrussBlock extends GenericPostBlock implements IPostBlock, IWaterLoggable{
	public static final BooleanProperty CONNECTOR_POINT_TOP = BooleanProperty.create("connector_point_top");
	public static final BooleanProperty CONNECTOR_POINT_BOTTOM = BooleanProperty.create("connector_point_bottom");
	
	public static final BooleanProperty PANEL_NORTH = BooleanProperty.create("panel_north");
	public static final BooleanProperty PANEL_EAST = BooleanProperty.create("panel_east");
	public static final BooleanProperty PANEL_SOUTH = BooleanProperty.create("panel_south");
	public static final BooleanProperty PANEL_WEST = BooleanProperty.create("panel_west");
	
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
	public static final EnumProperty<EnumHTrussType> TYPE = EnumProperty.create("type", EnumHTrussType.class);
	
	public HorizontalTrussBlock(EnumPostMaterial postMaterial){
		super(postMaterial, "_truss");
		
		setDefaultState(getStateContainer().getBaseState()
				.with(WATERLOGGED, false)
				.with(FACING, Direction.NORTH)
				.with(TYPE, EnumHTrussType.SINGLE)
				.with(CONNECTOR_POINT_TOP, false)
				.with(CONNECTOR_POINT_BOTTOM, false)
				.with(PANEL_NORTH, false)
				.with(PANEL_EAST, false)
				.with(PANEL_SOUTH, false)
				.with(PANEL_WEST, false)
				);
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder){
		builder.add(
				WATERLOGGED, FACING, TYPE,
				PANEL_NORTH, PANEL_EAST, PANEL_SOUTH, PANEL_WEST,
				CONNECTOR_POINT_TOP, CONNECTOR_POINT_BOTTOM
			);
	}
	
	@Override
	public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos){
		return 0;
	}
	
	@Override
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos){
		return 1.0F;
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos){
		return !state.get(WATERLOGGED);
	}
	
	@Override
	public FluidState getFluidState(BlockState state){
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context){
		BlockState state = super.getStateForPlacement(context);
		FluidState fs = context.getWorld().getFluidState(context.getPos());
		
		return state.with(WATERLOGGED, fs.getFluid() == Fluids.WATER);
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos){
		if(state.get(WATERLOGGED)){
			world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		
		boolean b0 = PostBlock.canConnect(world, pos, Direction.UP);
		boolean b1 = PostBlock.canConnect(world, pos, Direction.DOWN) || world.getBlockState(pos.offset(Direction.DOWN)).getBlock() instanceof PostBlock;
		
		return state
				.with(CONNECTOR_POINT_TOP, b0)
				.with(CONNECTOR_POINT_BOTTOM, b1);
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
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand){
		if(this.postMaterial == EnumPostMaterial.URANIUM){
			if(rand.nextFloat() < 0.125F){
				double x = pos.getX() + 0.375 + 0.25 * rand.nextDouble();
				double y = pos.getY() + rand.nextDouble();
				double z = pos.getZ() + 0.375 + 0.25 * rand.nextDouble();
				worldIn.addParticle(PostBlock.URAN_PARTICLE, x, y, z, 0.0, 0.0, 0.0);
			}
		}
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side){
		return false;
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player){
		return this.postMaterial.getItemStack();
	}
	
	@Override
	public boolean canConnectTransformer(IBlockReader world, BlockPos pos){
		return true;
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit){
		if(!worldIn.isRemote){
			ItemStack held = playerIn.getHeldItemMainhand();
			
			if(Utils.isHammer(held)){
				Direction face = hit.getFace();
				Direction facing = state.get(FACING);
				
				BlockState newState = state;
				switch(facing){
					case NORTH: case SOUTH:{
						if(face == Direction.EAST){
							newState = newState.with(PANEL_EAST, !newState.get(PANEL_EAST));
							
						}else if(face == Direction.WEST){
							newState = newState.with(PANEL_WEST, !newState.get(PANEL_WEST));
						}
						break;
					}
					case EAST: case WEST:{
						if(face == Direction.NORTH){
							newState = newState.with(PANEL_NORTH, !newState.get(PANEL_NORTH));
							
						}else if(face == Direction.SOUTH){
							newState = newState.with(PANEL_SOUTH, !newState.get(PANEL_SOUTH));
						}
						break;
					}
					default:break;
				}
				
				if(!newState.equals(state)){
					worldIn.setBlockState(pos, newState);
					return ActionResultType.SUCCESS;
				}
			}
		}
		
		if(Utils.isHammer(playerIn.getHeldItemMainhand())){
			return ActionResultType.SUCCESS;
		}
		
		return ActionResultType.PASS;
	}
	
	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving){
		if(world.isRemote)
			return;
		
		Direction facing = state.get(FACING);
		
		BlockPos posA = pos.offset(facing);
		BlockPos posB = pos.offset(facing.getOpposite());
		
		BlockState stateA = world.getBlockState(posA);
		BlockState stateB = world.getBlockState(posB);
		
		if((stateA.getBlock().isAir(stateA, world, posA) || stateB.getBlock().isAir(stateB, world, posB)) || (stateA.getBlock() == Blocks.WATER || stateB.getBlock() == Blocks.WATER)){
			replaceSelf(state, world, pos);
			return;
		}
	}
	
	static final VoxelShape NORTH_SOUTH = VoxelShapes.create(0.3125, 0.0, 0.0, 0.6875, 1.0, 1.0);
	static final VoxelShape EAST_WEST = VoxelShapes.create(0.0, 0.0, 0.3125, 1.0, 1.0, 0.6875);
	
	private static int getCacheIndex(Direction.Axis axis, boolean panelNorth, boolean panelEast, boolean panelSouth, boolean panelWest){
		int result = axis == Direction.Axis.X ? 1 : 0;
		if(panelNorth) result |= 2;
		if(panelEast) result |= 4;
		if(panelSouth) result |= 8;
		if(panelWest) result |= 16;
		return result;
	}
	
	private static final VoxelShape[] SHAPE_CACHE = new VoxelShape[32];
	
	static{
		boolean[] bools = {false, true};
		for(Direction.Axis axis:new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}){
			for(boolean north:bools){
				for(boolean east:bools){
					for(boolean south:bools){
						for(boolean west:bools){
							SHAPE_CACHE[getCacheIndex(axis, north, east, south, west)] = computeShape(axis, north, east, south, west);
						}
					}
				}
			}
		}
	}
	
	private static VoxelShape computeShape(Direction.Axis axis, boolean panelNorth, boolean panelEast, boolean panelSouth, boolean panelWest){
		VoxelShape shape = null;
		switch(axis){
			case Z:{
				shape = NORTH_SOUTH;
				break;
			}
			case X:{
				shape = EAST_WEST;
				break;
			}
			default:
				break;
		}
		
		if(shape == null){
			return VoxelShapes.empty();
		}
		
		if(panelNorth)	shape = VoxelShapes.combine(shape, VoxelShapes.create(0.0, 0.0, 0.0, 1.0, 1.0, 0.5), IBooleanFunction.OR);
		if(panelEast)	shape = VoxelShapes.combine(shape, VoxelShapes.create(0.5, 0.0, 0.0, 1.0, 1.0, 1.0), IBooleanFunction.OR);
		if(panelSouth)	shape = VoxelShapes.combine(shape, VoxelShapes.create(0.0, 0.0, 0.5, 1.0, 1.0, 1.0), IBooleanFunction.OR);
		if(panelEast)	shape = VoxelShapes.combine(shape, VoxelShapes.create(0.0, 0.0, 0.0, 0.5, 1.0, 1.0), IBooleanFunction.OR);
		
		return shape.simplify();
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		Direction.Axis axis = state.get(FACING).getAxis();
		if(axis != Direction.Axis.X && axis != Direction.Axis.Z){
			return VoxelShapes.empty();
		}else{
			return SHAPE_CACHE[getCacheIndex(axis, state.get(PANEL_NORTH), state.get(PANEL_EAST), state.get(PANEL_SOUTH), state.get(PANEL_WEST))];
		}
	}
}
