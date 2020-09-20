package twistedgate.immersiveposts.common.blocks;

import java.util.Arrays;
import java.util.List;

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
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import twistedgate.immersiveposts.IPOConfig;
import twistedgate.immersiveposts.IPOContent;
import twistedgate.immersiveposts.ImmersivePosts;
import twistedgate.immersiveposts.enums.EnumFlipState;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.enums.EnumPostType;

/**
 * @author TwistedGate
 */
public class BlockPostBase extends IPOBlockBase implements IWaterLoggable{
	private static final VoxelShape BASE_SIZE=VoxelShapes.create(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
	private static final Material BaseMaterial = new Material(MaterialColor.STONE, false, true, true, true, false, false, PushReaction.BLOCK);
	
	public static final BooleanProperty WATERLOGGED=BlockStateProperties.WATERLOGGED;
	
	public BlockPostBase(){
		super("postbase", Properties.create(BaseMaterial)
				.harvestTool(ToolType.PICKAXE)
				.hardnessAndResistance(5.0F, 3.0F));
		
		setDefaultState(getStateContainer().getBaseState()
				.with(WATERLOGGED, false)
		);
		
		IPOContent.ITEMS.add(new ItemPostBase(this, new Item.Properties().group(ImmersivePosts.creativeTab)));
	}
	
	@Override
	protected void fillStateContainer(net.minecraft.state.StateContainer.Builder<Block, BlockState> builder){
		builder.add(WATERLOGGED);
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
		BlockState state=super.getStateForPlacement(context);
		FluidState fs=context.getWorld().getFluidState(context.getPos());
		
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
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return BASE_SIZE;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder){
		return Arrays.asList(new ItemStack(this, 1));
	}
	
	@Override
	public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side){
		return false;
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit){
		if(!worldIn.isRemote){
			ItemStack held=playerIn.getHeldItemMainhand();
			if(held!=ItemStack.EMPTY){
				if(EnumPostMaterial.isValidItem(held)){
					if(!worldIn.isAirBlock(pos.offset(Direction.UP))){
						BlockState aboveState=worldIn.getBlockState(pos.offset(Direction.UP));
						Block b=aboveState.getBlock();
						
						if(b instanceof BlockPost){
							ItemStack tmp=((BlockPost)b).postMaterial.getItemStack();
							if(!held.isItemEqual(tmp)){
								playerIn.sendStatusMessage(new TranslationTextComponent("immersiveposts.expectedlocal", new StringTextComponent(tmp.getDisplayName().getString())), true);
								return ActionResultType.SUCCESS;
							}
						}
					}
					
					for(int y=1;y<(worldIn.getHeight(Type.WORLD_SURFACE, pos.getX(), pos.getZ())-pos.getY());y++){
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
			}
		}
		
		if(EnumPostMaterial.isValidItem(playerIn.getHeldItemMainhand()))
			return ActionResultType.SUCCESS;
		
		return ActionResultType.FAIL;
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
					IFormattableTextComponent typeName=new StringTextComponent("");
					typeName.append(t.getItemStack().getDisplayName());
					
					if(IPOConfig.MAIN.isEnabled(t))
						typeName.mergeStyle(TextFormatting.GREEN);
					else
						typeName.mergeStyle(TextFormatting.RED, TextFormatting.STRIKETHROUGH);
					
					tooltip.add(new StringTextComponent("- ").append(typeName));
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
