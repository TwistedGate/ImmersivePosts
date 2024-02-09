package twistedgate.immersiveposts.common.blocks;

import javax.annotation.Nullable;

import blusunrize.immersiveengineering.api.IPostBlock;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import twistedgate.immersiveposts.api.posts.IPostMaterial;
import twistedgate.immersiveposts.enums.EnumHTrussType;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

public class HorizontalTrussBlock extends GenericPostBlock implements IPostBlock, SimpleWaterloggedBlock{
	public static final BooleanProperty CONNECTOR_POINT_TOP = BooleanProperty.create("connector_point_top");
	public static final BooleanProperty CONNECTOR_POINT_BOTTOM = BooleanProperty.create("connector_point_bottom");
	
	public static final BooleanProperty PANEL_NORTH = BooleanProperty.create("panel_north");
	public static final BooleanProperty PANEL_EAST = BooleanProperty.create("panel_east");
	public static final BooleanProperty PANEL_SOUTH = BooleanProperty.create("panel_south");
	public static final BooleanProperty PANEL_WEST = BooleanProperty.create("panel_west");
	
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
	public static final EnumProperty<EnumHTrussType> TYPE = EnumProperty.create("type", EnumHTrussType.class);
	
	public HorizontalTrussBlock(IPostMaterial material){
		super(material);
		
		registerDefaultState(getStateDefinition().any()
				.setValue(WATERLOGGED, false)
				.setValue(FACING, Direction.NORTH)
				.setValue(TYPE, EnumHTrussType.SINGLE)
				.setValue(CONNECTOR_POINT_TOP, false)
				.setValue(CONNECTOR_POINT_BOTTOM, false)
				.setValue(PANEL_NORTH, false)
				.setValue(PANEL_EAST, false)
				.setValue(PANEL_SOUTH, false)
				.setValue(PANEL_WEST, false)
				);
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder){
		builder.add(
				WATERLOGGED, FACING, TYPE,
				PANEL_NORTH, PANEL_EAST, PANEL_SOUTH, PANEL_WEST,
				CONNECTOR_POINT_TOP, CONNECTOR_POINT_BOTTOM
			);
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getLightBlock(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos){
		return 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public float getShadeBrightness(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos){
		return 1.0F;
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos){
		return !state.getValue(WATERLOGGED);
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull FluidState getFluidState(BlockState state){
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context){
		BlockState state = super.getStateForPlacement(context);
		FluidState fs = context.getLevel().getFluidState(context.getClickedPos());
		
		return state == null ? null : state.setValue(WATERLOGGED, fs.getType() == Fluids.WATER);
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor world, @NotNull BlockPos pos, @NotNull BlockPos facingPos){
		if(state.getValue(WATERLOGGED)){
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		
		boolean b0 = PostBlock.canConnect(world, pos, Direction.UP);
		boolean b1 = PostBlock.canConnect(world, pos, Direction.DOWN) || world.getBlockState(pos.relative(Direction.DOWN)).getBlock() instanceof PostBlock;
		
		return state
				.setValue(CONNECTOR_POINT_TOP, b0)
				.setValue(CONNECTOR_POINT_BOTTOM, b1);
	}
	
	@Override
	public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity){
		return true;
	}
	
	@Override
	public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face){
		return false;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand){
		if(getPostMaterial() == EnumPostMaterial.URANIUM){
			if(rand.nextFloat() < 0.125F){
				double x = pos.getX() + 0.375 + 0.25 * rand.nextDouble();
				double y = pos.getY() + rand.nextDouble();
				double z = pos.getZ() + 0.375 + 0.25 * rand.nextDouble();
				worldIn.addParticle(PostBlock.URAN_PARTICLE, x, y, z, 0.0, 0.0, 0.0);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean skipRendering(@NotNull BlockState state, @NotNull BlockState adjacentBlockState, @NotNull Direction side){
		return false;
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player){
		return getPostMaterial().getItemStack();
	}
	
	@Override
	public boolean canConnectTransformer(BlockGetter world, BlockPos pos){
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player playerIn, @NotNull InteractionHand handIn, @NotNull BlockHitResult hit){
		if(!worldIn.isClientSide){
			ItemStack held = playerIn.getMainHandItem();
			
			if(Utils.isHammer(held)){
				Direction face = hit.getDirection();
				Direction facing = state.getValue(FACING);
				
				BlockState newState = state;
				switch (facing) {
					case NORTH, SOUTH -> {
						if (face == Direction.EAST) {
							newState = newState.setValue(PANEL_EAST, !newState.getValue(PANEL_EAST));

						} else if (face == Direction.WEST) {
							newState = newState.setValue(PANEL_WEST, !newState.getValue(PANEL_WEST));
						}
					}
					case EAST, WEST -> {
						if (face == Direction.NORTH) {
							newState = newState.setValue(PANEL_NORTH, !newState.getValue(PANEL_NORTH));

						} else if (face == Direction.SOUTH) {
							newState = newState.setValue(PANEL_SOUTH, !newState.getValue(PANEL_SOUTH));
						}
					}
					default -> {
					}
				}
				
				if(!newState.equals(state)){
					worldIn.setBlockAndUpdate(pos, newState);
					return InteractionResult.SUCCESS;
				}
			}
		}
		
		if(Utils.isHammer(playerIn.getMainHandItem())){
			return InteractionResult.SUCCESS;
		}
		
		return InteractionResult.PASS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos fromPos, boolean isMoving){
		if(world.isClientSide)
			return;
		
		Direction facing = state.getValue(FACING);
		
		BlockPos posA = pos.relative(facing);
		BlockPos posB = pos.relative(facing.getOpposite());
		
		BlockState stateA = world.getBlockState(posA);
		BlockState stateB = world.getBlockState(posB);
		
		if((stateA.isAir() || stateB.isAir()) || (stateA.getBlock() == Blocks.WATER || stateB.getBlock() == Blocks.WATER)){
			replaceSelf(state, world, pos);
		}
	}
	
	static final VoxelShape NORTH_SOUTH = Shapes.box(0.3125, 0.0, 0.0, 0.6875, 1.0, 1.0);
	static final VoxelShape EAST_WEST = Shapes.box(0.0, 0.0, 0.3125, 1.0, 1.0, 0.6875);
	
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
		boolean[] booleans = {false, true};
		for(Direction.Axis axis:new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}){
			for(boolean north:booleans){
				for(boolean east:booleans){
					for(boolean south:booleans){
						for(boolean west:booleans){
							SHAPE_CACHE[getCacheIndex(axis, north, east, south, west)] = computeShape(axis, north, east, south, west);
						}
					}
				}
			}
		}
	}
	
	private static VoxelShape computeShape(Direction.Axis axis, boolean panelNorth, boolean panelEast, boolean panelSouth, boolean panelWest){
		VoxelShape shape = null;
		switch (axis) {
			case Z -> shape = NORTH_SOUTH;
			case X -> shape = EAST_WEST;
		}
		
		if(shape == null){
			return Shapes.empty();
		}
		
		if(panelNorth)	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 0.5), BooleanOp.OR);
		if(panelEast)	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.5, 0.0, 0.0, 1.0, 1.0, 1.0), BooleanOp.OR);
		if(panelSouth)	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.0, 0.0, 0.5, 1.0, 1.0, 1.0), BooleanOp.OR);
		if(panelWest)	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.0, 0.0, 0.0, 0.5, 1.0, 1.0), BooleanOp.OR);
		
		return shape.optimize();
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context){
		Direction.Axis axis = state.getValue(FACING).getAxis();
		if(axis != Direction.Axis.X && axis != Direction.Axis.Z){
			return Shapes.empty();
		}else{
			return SHAPE_CACHE[getCacheIndex(axis, state.getValue(PANEL_NORTH), state.getValue(PANEL_EAST), state.getValue(PANEL_SOUTH), state.getValue(PANEL_WEST))];
		}
	}
}
