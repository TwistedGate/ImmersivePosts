package twistedgate.immersiveposts.common.blocks;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import twistedgate.immersiveposts.IPOConfig;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.ImmersivePosts;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.enums.EnumPostType;
import twistedgate.immersiveposts.utils.BlockHelper;

/**
 * @author TwistedGate
 */
public class BlockPostBase extends IPOBlockBase{
	private static final AxisAlignedBB BASE_SIZE=new AxisAlignedBB(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
	private static final Material BaseMaterial = new Material.Builder(MaterialColor.STONE).doesNotBlockMovement().notSolid().build();
	
	public BlockPostBase(){
		super("postbase", Properties.create(BaseMaterial)
				.harvestTool(ToolType.PICKAXE)
				.hardnessAndResistance(5.0F, 3.0F));
		
		IPOStuff.ITEMS.add(new ItemPostBase(this, new Item.Properties().group(ImmersivePosts.creativeTab)));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return VoxelShapes.create(BASE_SIZE);
	}
	
	@Override
	public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side){
		return true;
	}
	
	@Override
	public boolean isSolid(BlockState state){
		return false;
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit){
		if(!worldIn.isRemote){
			ItemStack held=playerIn.getHeldItemMainhand();
			if(EnumPostMaterial.isFenceItem(held)){
				if(!IPOConfig.isEnabled(EnumPostMaterial.getFrom(held))){
					return true;
				}
				
				if(!worldIn.isAirBlock(pos.offset(Direction.UP))){
					BlockState aboveState=worldIn.getBlockState(pos.offset(Direction.UP));
					Block b=aboveState.getBlock();
					
					if(b instanceof BlockPost){
						ItemStack tmp=((BlockPost)b).postMaterial.getItemStack();
						if(!held.isItemEqual(tmp)){
							playerIn.sendStatusMessage(new TranslationTextComponent("immersiveposts.expectedlocal", new StringTextComponent(tmp.getDisplayName().getString())), true);
							return true;
						}
					}
				}
				
				for(int y=1;y<(worldIn.getActualHeight()-pos.getY());y++){
					BlockPos nPos=pos.add(0,y,0);
					
					if((BlockHelper.getBlockFrom(worldIn, nPos) instanceof BlockPost)){
						BlockState s=worldIn.getBlockState(nPos);
						if(s.get(BlockPost.TYPE)==EnumPostType.ARM){
							return true;
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
			}
		}
		
		return EnumPostMaterial.isFenceItem(playerIn.getHeldItemMainhand());
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
					if(IPOConfig.isEnabled(t))
						tooltip.add(new StringTextComponent("- \u00A7a"+t.getItemStack().getDisplayName().getFormattedText()));
				}
			}else{
				tooltip.add(new StringTextComponent(I18n.format("tooltip.postbase")));
			}
		}
		
		@OnlyIn(Dist.CLIENT)
		private boolean isPressing(int key){
			long window=Minecraft.getInstance().mainWindow.getHandle();
			return GLFW.glfwGetKey(window, key)==GLFW.GLFW_PRESS;
		}
	}
}
