package twistedgate.immersiveposts.common.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import blusunrize.immersiveengineering.api.IPostBlock;
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
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twistedgate.immersiveposts.enums.EnumHorizontalTrussType;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

public class HorizontalTrussBlock extends GenericPostBlock implements IPostBlock, IWaterLoggable{
	
	public static final BooleanProperty CONNECTOR_POINT_TOP = BooleanProperty.create("connector_point_top");
	public static final BooleanProperty CONNECTOR_POINT_BOTTOM = BooleanProperty.create("connector_point_bottom");
	
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
	public static final EnumProperty<EnumHorizontalTrussType> TYPE = EnumProperty.create("type", EnumHorizontalTrussType.class);
	
	public HorizontalTrussBlock(EnumPostMaterial postMaterial){
		super(postMaterial, "_horizontal");
		
		setDefaultState(getStateContainer().getBaseState()
				.with(WATERLOGGED, false)
				.with(FACING, Direction.NORTH)
				.with(TYPE, EnumHorizontalTrussType.HORIZONTAL_A)
				.with(CONNECTOR_POINT_TOP, false)
				.with(CONNECTOR_POINT_BOTTOM, false)
				);
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder){
		builder.add(WATERLOGGED, FACING, TYPE, CONNECTOR_POINT_TOP, CONNECTOR_POINT_BOTTOM);
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
		
		if(facing == Direction.UP){
			boolean b = PostBlock.canConnect(world, pos, facing);
			state = state.with(CONNECTOR_POINT_TOP, b);
		}
		if(facing == Direction.DOWN){
			boolean b = PostBlock.canConnect(world, pos, facing);
			state = state.with(CONNECTOR_POINT_BOTTOM, b);
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
		// TODO Maybe?
		return false;
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
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		Direction dir = state.get(FACING);
		
		switch(dir){
			case NORTH: case SOUTH:{
				return VoxelShapes.create(0.3125, 0.0, 0.0, 0.6875, 1.0, 1.0);
			}
			case EAST: case WEST:{
				return VoxelShapes.create(0.0, 0.0, 0.3125, 1.0, 1.0, 0.6875);
			}
			default:break;
		}
		
		return VoxelShapes.empty();
	}
}
