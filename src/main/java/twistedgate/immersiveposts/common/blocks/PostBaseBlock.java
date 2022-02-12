package twistedgate.immersiveposts.common.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twistedgate.immersiveposts.ImmersivePosts;
import twistedgate.immersiveposts.api.posts.IPostMaterial;
import twistedgate.immersiveposts.common.IPOContent;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;
import twistedgate.immersiveposts.enums.EnumFlipState;
import twistedgate.immersiveposts.enums.EnumPostType;

/**
 * @author TwistedGate
 */
public class PostBaseBlock extends IPOBlockBase implements SimpleWaterloggedBlock, EntityBlock{
	private static BlockBehaviour.Properties prop(){
		Material BaseMaterial = new Material(MaterialColor.STONE, false, true, true, true, false, false, PushReaction.BLOCK);
		
		BlockBehaviour.Properties prop = BlockBehaviour.Properties.of(BaseMaterial)
				.sound(SoundType.STONE)
				.requiresCorrectToolForDrops()
				.strength(5.0F, 3.0F)
				.noOcclusion();
		
		return prop;
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
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos){
		return !state.getValue(HIDDEN) ? !state.getValue(WATERLOGGED) : super.propagatesSkylightDown(state, reader, pos);
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player){
		if(player.isShiftKeyDown() && state.getValue(HIDDEN)){
			ItemStack stack = ((PostBaseTileEntity) world.getBlockEntity(pos)).getStack();
			if(stack != ItemStack.EMPTY){
				return stack;
			}
		}
		return ItemStack.EMPTY;//super.getPickBlock(state, target, world, pos, player);
	}
	
	@Override
	public FluidState getFluidState(BlockState state){
		return (!state.getValue(HIDDEN) && state.getValue(WATERLOGGED)) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context){
		BlockState state = super.getStateForPlacement(context);
		FluidState fs = context.getLevel().getFluidState(context.getClickedPos());
		
		state = state.setValue(WATERLOGGED, fs.getType() == Fluids.WATER);
		return state;
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos){
		if(!state.getValue(HIDDEN) && state.getValue(WATERLOGGED)){
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return state;
	}
	
	private static final VoxelShape BASE_SIZE = Shapes.box(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
	@Override
	public boolean canPlaceLiquid(BlockGetter world, BlockPos pos, BlockState state, Fluid fluid){
		return !state.getValue(HIDDEN) && SimpleWaterloggedBlock.super.canPlaceLiquid(world, pos, state, fluid);
	}
	
//	@Override
//	public boolean hasTileEntity(BlockState state){
//		return true;
//	}
//	
//	@Override
//	public BlockEntity createTileEntity(BlockState state, BlockGetter world){
//		return IPOContent.TE_POSTBASE.create();
//	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState){
		return IPOContent.TE_POSTBASE.create(pPos, pState);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context){
		return this.getShape(state, worldIn, pos, context);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context){
		return state.getValue(HIDDEN) ? Shapes.block() : BASE_SIZE;
	}
	
	@Override
	public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side){
		return false;
	}
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult hit){
		BlockEntity te = worldIn.getBlockEntity(pos);
		if(te instanceof PostBaseTileEntity){
			if(((PostBaseTileEntity) te).interact(state, worldIn, pos, playerIn, handIn)){
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
						if(!held.sameItem(tmp)){
							playerIn.displayClientMessage(new TranslatableComponent("immersiveposts.expectedlocal", tmp.getHoverName()), true);
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
		}else{
			// Client Stuff here
			ItemStack held = playerIn.getMainHandItem();
			if(IPostMaterial.isValidItem(held)){
				return InteractionResult.SUCCESS;
			}
		}
		
		return InteractionResult.FAIL;
	}
	
	
	public static class ItemPostBase extends BlockItem{
		public ItemPostBase(Block block){
			super(block, new Item.Properties().tab(ImmersivePosts.creativeTab));
		}
		
		@Override
		@OnlyIn(Dist.CLIENT)
		public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn){
			tooltip.add(new TextComponent(I18n.get("tooltip.postbase")));
		}
	}
}
