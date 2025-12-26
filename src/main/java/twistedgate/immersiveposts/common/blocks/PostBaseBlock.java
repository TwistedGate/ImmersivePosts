package twistedgate.immersiveposts.common.blocks;

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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
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
import twistedgate.immersiveposts.api.posts.IPostMaterial;
import twistedgate.immersiveposts.common.IPOTileTypes;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;
import twistedgate.immersiveposts.enums.EnumFlipState;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.enums.EnumPostType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author TwistedGate
 */
public class PostBaseBlock extends IPOBlockBase implements SimpleWaterloggedBlock, EntityBlock{
	private static BlockBehaviour.Properties prop(){
		return EnumPostMaterial.PostBlockProperties.stone()
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
	public boolean propagatesSkylightDown(BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos){
		return !state.getValue(HIDDEN) ? !state.getValue(WATERLOGGED) : super.propagatesSkylightDown(state, reader, pos);
	}
	
	@Nonnull
	@Override
	public ItemStack getCloneItemStack(@Nonnull BlockState state, @Nonnull HitResult target, @Nonnull LevelReader world, @Nonnull BlockPos pos, Player player){
		if(player.isShiftKeyDown() && state.getValue(HIDDEN)){
			ItemStack stack = ((PostBaseTileEntity) world.getBlockEntity(pos)).getStack();
			if(stack != ItemStack.EMPTY){
				return stack;
			}
		}
		return super.getCloneItemStack(state, target, world, pos, player);
	}
	
	@Nonnull
	@Override
	public FluidState getFluidState(BlockState state){
		return (!state.getValue(HIDDEN) && state.getValue(WATERLOGGED)) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context){
		BlockState state = super.getStateForPlacement(context);
		FluidState fs = context.getLevel().getFluidState(context.getClickedPos());
		
		state = state.setValue(WATERLOGGED, fs.getType() == Fluids.WATER);
		return state;
	}
	
	@Nonnull
	@Override
	public BlockState updateShape(BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor world, @Nonnull BlockPos pos, @Nonnull BlockPos facingPos){
		if(!state.getValue(HIDDEN) && state.getValue(WATERLOGGED)){
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return state;
	}
	
	private static final VoxelShape BASE_SIZE = Shapes.box(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
	@Override
	public boolean canPlaceLiquid(Player player, @Nonnull BlockGetter world, @Nonnull BlockPos pos, BlockState state, @Nonnull Fluid fluid){
		return !state.getValue(HIDDEN) && SimpleWaterloggedBlock.super.canPlaceLiquid(player, world, pos, state, fluid);
	}
	
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pPos, @Nonnull BlockState pState){
		return IPOTileTypes.POST_BASE.get().create(pPos, pState);
	}
	
	@Nonnull
	@Override
	public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context){
		return this.getShape(state, worldIn, pos, context);
	}
	
	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context){
		return state.getValue(HIDDEN) ? Shapes.block() : BASE_SIZE;
	}
	
	@Override
	public boolean skipRendering(@Nonnull BlockState state, @Nonnull BlockState adjacentBlockState, @Nonnull Direction side){
		return false;
	}
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult hit){
		BlockEntity te = worldIn.getBlockEntity(pos);
		if(te instanceof PostBaseTileEntity baseTe){
			if(baseTe.interact(state, worldIn, pos, playerIn, handIn)){
				return InteractionResult.SUCCESS;
			}
		}
		
		if(worldIn.isClientSide){ // Client Stuff here
			ItemStack held = playerIn.getMainHandItem();
			if(IPostMaterial.isValidItem(held)){
				return InteractionResult.SUCCESS;
			}
			
			return InteractionResult.FAIL;
		}
		
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
					
					if(fb != null && !playerIn.blockPosition().equals(nPos) && worldIn.setBlockAndUpdate(nPos, fb.updateShape(null, null, worldIn, nPos, null))){
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
		
		return InteractionResult.FAIL;
	}
	
	
	public static class ItemPostBase extends BlockItem{
		public ItemPostBase(Block block){
			super(block, new Item.Properties());
		}
		
		@Override
		public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag tooltipFlag){
			super.appendHoverText(stack, context, tooltip, tooltipFlag);
			tooltip.add(Component.translatable("tooltip.postbase"));
		}
		
		/*
		@OnlyIn(Dist.CLIENT)
		public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn){
			tooltip.add(Component.literal(I18n.get("tooltip.postbase")));
		}
		*/
	}
}
