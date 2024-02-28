package twistedgate.immersiveposts.common.blocks;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import twistedgate.immersiveposts.api.posts.IPostMaterial;
import twistedgate.immersiveposts.common.IPOTileTypes;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;
import twistedgate.immersiveposts.enums.EnumFlipState;
import twistedgate.immersiveposts.enums.EnumPostType;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author TwistedGate
 */
public class PostBaseBlock extends IPOBlockBase implements SimpleWaterloggedBlock, EntityBlock{
	private static BlockBehaviour.Properties prop(){

		return Properties.of()
				.sound(SoundType.STONE)
				.requiresCorrectToolForDrops()
				.strength(5.0F, 3.0F)
				.noOcclusion();
	}
	
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty HIDDEN = BooleanProperty.create("hidden");
	
	public PostBaseBlock(){
		super(prop());
		
		registerDefaultState(getStateDefinition().any()
				.setValue(HIDDEN, false)
				.setValue(WATERLOGGED, false)
		);
	}
	
	@Override
	protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> builder){
		builder.add(HIDDEN, WATERLOGGED);
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos){
		return !state.getValue(HIDDEN) ? !state.getValue(WATERLOGGED) : super.propagatesSkylightDown(state, reader, pos);
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player){
		if(player.isShiftKeyDown() && state.getValue(HIDDEN)){
			final var entity = ((PostBaseTileEntity) world.getBlockEntity(pos));
			// You are never sure that block entity is already present in world at that moment
			if(entity == null) return super.getCloneItemStack(state, target, world, pos, player);
			ItemStack stack = entity.getStack();
			if(stack != ItemStack.EMPTY){
				return stack;
			}
		}
		return super.getCloneItemStack(state, target, world, pos, player);
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull FluidState getFluidState(BlockState state){
		return (!state.getValue(HIDDEN) && state.getValue(WATERLOGGED)) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context){
		BlockState state = super.getStateForPlacement(context);
		FluidState fs = context.getLevel().getFluidState(context.getClickedPos());
		if(state == null) return null;
		state = state.setValue(WATERLOGGED, fs.getType() == Fluids.WATER);
		return state;
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor world, @NotNull BlockPos pos, @NotNull BlockPos facingPos){
		if(!state.getValue(HIDDEN) && state.getValue(WATERLOGGED)){
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return state;
	}
	
	private static final VoxelShape BASE_SIZE = Shapes.box(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
	@Override
	public boolean canPlaceLiquid(@NotNull BlockGetter world, @NotNull BlockPos pos, BlockState state, @NotNull Fluid fluid){
		return !state.getValue(HIDDEN) && SimpleWaterloggedBlock.super.canPlaceLiquid(world, pos, state, fluid);
	}
	
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state){
		return IPOTileTypes.POST_BASE.get().create(pos, state);
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context){
		return this.getShape(state, worldIn, pos, context);
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context){
		return state.getValue(HIDDEN) ? Shapes.block() : BASE_SIZE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean skipRendering(@NotNull BlockState state, @NotNull BlockState adjacentBlockState, @NotNull Direction side){
		return false;
	}

	@SuppressWarnings("all")
	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player playerIn, @NotNull InteractionHand handIn, @NotNull BlockHitResult hit){
		BlockEntity te = worldIn.getBlockEntity(pos);
		if(te instanceof PostBaseTileEntity){
			if(((PostBaseTileEntity) te).interact(state, worldIn, pos, playerIn)){
				return InteractionResult.SUCCESS;
			}
		}
		
		if(!worldIn.isClientSide){
			ItemStack held = playerIn.getMainHandItem();
			
			if(IPostMaterial.isValidItem(held)){
				if(!worldIn.isEmptyBlock(pos.relative(Direction.UP))){
					BlockState aboveState = worldIn.getBlockState(pos.relative(Direction.UP));
					Block b = aboveState.getBlock();
					
					if(b instanceof PostBlock){
						ItemStack tmp = ((PostBlock) b).getPostMaterial().getItemStack();
						if(!held.is(tmp.getItem())){
							playerIn.displayClientMessage(Component.translatable("immersiveposts.expectedlocal", tmp.getHoverName()), true);
							return InteractionResult.SUCCESS;
						}
					}
				}
				
				for(int y = 1;y <= (worldIn.getHeight(Types.WORLD_SURFACE, pos.getX(), pos.getZ()) - pos.getY());y++){
					BlockPos nPos = pos.offset(0, y, 0);
					
					BlockState nState = worldIn.getBlockState(nPos);
					if(nState.getBlock() instanceof PostBlock){
						EnumPostType type = nState.getValue(PostBlock.TYPE);
						if(!(type == EnumPostType.POST || type == EnumPostType.POST_TOP) && nState.getValue(PostBlock.FLIPSTATE) == EnumFlipState.DOWN){
							return InteractionResult.SUCCESS;
						}else{
							nState = worldIn.getBlockState(nPos.relative(Direction.UP));
							if(nState.getBlock() instanceof PostBlock){
								type = nState.getValue(PostBlock.TYPE);
								if(!(type == EnumPostType.POST || type == EnumPostType.POST_TOP)){
									return InteractionResult.SUCCESS;
								}
							}
						}
					}
					
					if(worldIn.isEmptyBlock(nPos) || worldIn.getBlockState(nPos).getBlock() == Blocks.WATER){
						BlockState fb = IPostMaterial.getPostState(held)
								.setValue(WATERLOGGED, worldIn.getBlockState(nPos).getBlock() == Blocks.WATER);

						if(!playerIn.blockPosition().equals(nPos) && worldIn.setBlockAndUpdate(nPos, fb.updateShape(null, null, worldIn, nPos, null))){
							if(!playerIn.isCreative()){
								held.shrink(1);
							}
						}
						return InteractionResult.SUCCESS;
						
					}else if(!(worldIn.getBlockState(nPos).getBlock() instanceof PostBlock)){
						return InteractionResult.SUCCESS;
					}
				}
			}
		}else{
			// Client Stuff here
			ItemStack held = playerIn.getMainHandItem();
			if(IPostMaterial.isValidItem(held)){
				return InteractionResult.SUCCESS;
			}
		}
		
		return InteractionResult.FAIL;
	}
	
	
	public static class ItemPostBase extends BlockItem {
		public ItemPostBase(Block block){
			super(block, new Item.Properties());
		}
		
		@Override
		@OnlyIn(Dist.CLIENT)
		public void appendHoverText(@NotNull ItemStack stack, Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn){
			tooltip.add(Component.literal(I18n.get("tooltip.postbase")));
		}
	}
}
