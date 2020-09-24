package twistedgate.immersiveposts.common.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import twistedgate.immersiveposts.IPOConfig;
import twistedgate.immersiveposts.IPOContent;
import twistedgate.immersiveposts.ImmersivePosts;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;
import twistedgate.immersiveposts.enums.EnumFlipState;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.enums.EnumPostType;

/**
 * @author TwistedGate
 */
public class BlockPostBase extends IPOBlockBase implements IWaterLoggable{
	private static final VoxelShape BASE_SIZE=VoxelShapes.create(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
	private static final Material BaseMaterial = new Material(MaterialColor.STONE, false, true, true, true, false, false, false, PushReaction.BLOCK);
	
	public static final BooleanProperty WATERLOGGED=BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty HIDDEN=BooleanProperty.create("hidden");
	
	public BlockPostBase(){
		super("postbase", Properties.create(BaseMaterial)
				.harvestTool(ToolType.PICKAXE)
				.hardnessAndResistance(5.0F, 3.0F));
		
		setDefaultState(getStateContainer().getBaseState()
				.with(HIDDEN, false)
				.with(WATERLOGGED, false)
		);
		
		IPOContent.ITEMS.add(new ItemPostBase(this, new Item.Properties().group(ImmersivePosts.creativeTab)));
	}
	
	@Override
	protected void fillStateContainer(net.minecraft.state.StateContainer.Builder<Block, BlockState> builder){
		builder.add(HIDDEN, WATERLOGGED);
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos){
		return !state.get(HIDDEN) ? !state.get(WATERLOGGED) : super.propagatesSkylightDown(state, reader, pos);
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player){
		if(player.isSneaking() && state.get(HIDDEN)){
			ItemStack stack=((PostBaseTileEntity)world.getTileEntity(pos)).getStack();
			if(stack!=ItemStack.EMPTY){
				return stack;
			}
		}
		return super.getPickBlock(state, target, world, pos, player);
	}
	
	@Override
	public IFluidState getFluidState(BlockState state){
		return (!state.get(HIDDEN) && state.get(WATERLOGGED)) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context){
		IFluidState fs=context.getWorld().getFluidState(context.getPos());
		
		BlockState state=super.getStateForPlacement(context)
				.with(WATERLOGGED, fs.getFluid() == Fluids.WATER);
		return state;
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos){
		if(!state.get(HIDDEN) && state.get(WATERLOGGED)){
			world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return state;
	}
	
	@Override
	public boolean canContainFluid(IBlockReader world, BlockPos pos, BlockState state, Fluid fluid){
		return !state.get(HIDDEN) && IWaterLoggable.super.canContainFluid(world, pos, state, fluid);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state){
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return IPOContent.TE_POSTBASE.create();
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return this.getShape(state, worldIn, pos, context);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return state.get(HIDDEN) ? VoxelShapes.fullCube() : BASE_SIZE;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder){
		List<ItemStack> list=new ArrayList<>();
		list.add(new ItemStack(this, 1));
		if(state.get(HIDDEN)){
			TileEntity te=builder.get(LootParameters.BLOCK_ENTITY);
			if(te instanceof PostBaseTileEntity){
				ItemStack teStack=((PostBaseTileEntity)te).getStack();
				list.add(teStack);
			}
		}
		return list;
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit){
		if(!worldIn.isRemote){
			ItemStack held=playerIn.getHeldItemMainhand();
			
			if(held==ItemStack.EMPTY){
				if(playerIn.isSneaking()){
					TileEntity te=worldIn.getTileEntity(pos);
					if(te instanceof PostBaseTileEntity){
						PostBaseTileEntity base=(PostBaseTileEntity)te;
						if(!base.getStack().isEmpty()){
							Block.spawnAsEntity(worldIn, pos, base.getStack());
							base.setStack(ItemStack.EMPTY);
							worldIn.setBlockState(pos, state.with(HIDDEN, false));
						}
						
						return ActionResultType.SUCCESS;
					}
				}
				
			}else{
				if(EnumPostMaterial.isValidItem(held)){
					if(!worldIn.isAirBlock(pos.offset(Direction.UP))){
						BlockState aboveState=worldIn.getBlockState(pos.offset(Direction.UP));
						Block b=aboveState.getBlock();
						
						if(b instanceof BlockPost){
							ItemStack tmp=((BlockPost)b).postMaterial.getItemStack();
							if(!held.isItemEqual(tmp)){
								playerIn.sendStatusMessage(new TranslationTextComponent("immersiveposts.expectedlocal", tmp.getDisplayName()), true);
								return ActionResultType.SUCCESS;
							}
						}
					}
					
					for(int y=1;y<(worldIn.getActualHeight()-pos.getY());y++){
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
							
							if(fb!=null && !playerIn.getPosition().equals(nPos) && worldIn.setBlockState(nPos, fb.updatePostPlacement(null, null, worldIn, nPos, null))){
								if(!playerIn.isCreative()){
									held.shrink(1);
								}
							}
							return ActionResultType.SUCCESS;
							
						}else if(!(worldIn.getBlockState(nPos).getBlock() instanceof BlockPost)){
							return ActionResultType.SUCCESS;
						}
					}
				}
				
				if(held.getItem() instanceof BlockItem && (held.getItem() instanceof BlockItem && acceptedHidingBlock(Block.getBlockFromItem(held.getItem()), worldIn, pos))){
					TileEntity te=worldIn.getTileEntity(pos);
					if(te instanceof PostBaseTileEntity){
						PostBaseTileEntity base=(PostBaseTileEntity)te;
						
						if(base.getStack().isEmpty()){
							ItemStack copy=held.copy();
							copy.setCount(1);
							base.setStack(copy);
							
							if(!playerIn.isCreative()){
								held.shrink(1);
							}
							
							worldIn.setBlockState(pos, state.with(HIDDEN, true).with(WATERLOGGED, false));
							
							return ActionResultType.SUCCESS;
						}
					}
				}
			}
		}else{
			// Client Stuff here
			ItemStack held=playerIn.getHeldItemMainhand();
			if((playerIn.isSneaking() && held==ItemStack.EMPTY) || EnumPostMaterial.isValidItem(held) || (held.getItem() instanceof BlockItem && acceptedHidingBlock(Block.getBlockFromItem(held.getItem()), worldIn, pos))){
				return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.FAIL;
	}
	
	/** Used to check wether the base can hide within a certain block */
	private boolean acceptedHidingBlock(@Nonnull Block block, @Nonnull IBlockReader reader, @Nonnull BlockPos pos){
		BlockState state=block.getDefaultState();
		return block!=Blocks.AIR && state.isNormalCube(reader, pos) && state.isOpaqueCube(reader, pos);
	}
	
	
	public static class ItemPostBase extends BlockItem{
		public ItemPostBase(Block block, Properties properties){
			super(block, properties);
			setRegistryName(block.getRegistryName());
		}
		
		@Override
		@OnlyIn(Dist.CLIENT)
		public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
			if(isPressing(GLFW.GLFW_KEY_LEFT_SHIFT) || isPressing(GLFW.GLFW_KEY_RIGHT_SHIFT)){
				for(EnumPostMaterial t:EnumPostMaterial.values()){
					ITextComponent typeName=new StringTextComponent(t.getItemStack().getDisplayName().getFormattedText());
					
					if(IPOConfig.MAIN.isEnabled(t))
						typeName.applyTextStyle(TextFormatting.GREEN);
					else
						typeName.applyTextStyles(TextFormatting.RED, TextFormatting.STRIKETHROUGH);
					
					tooltip.add(new StringTextComponent("- ").appendSibling(typeName));
				}
			}else{
				tooltip.add(new StringTextComponent(I18n.format("tooltip.postbase")));
			}
		}
		
		/** Find the key that is being pressed while minecraft is in focus */
		@OnlyIn(Dist.CLIENT)
		private boolean isPressing(int key){
			long window=Minecraft.getInstance().getMainWindow().getHandle();
			return GLFW.glfwGetKey(window, key)==GLFW.GLFW_PRESS;
		}
	}
}
