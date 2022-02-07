package twistedgate.immersiveposts.api.posts;

import javax.annotation.Nonnull;

import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.common.IPOConfig;
import twistedgate.immersiveposts.common.IPOContent.Blocks.HorizontalTruss;
import twistedgate.immersiveposts.common.IPOContent.Blocks.Posts;
import twistedgate.immersiveposts.common.blocks.PostBlock;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.enums.EnumPostType;

public interface IPostMaterial{
	
	/** Source-block itemstack */
	ItemStack getItemStack();
	
	/** Returns the texture location for this material type */
	ResourceLocation getTexture();
	
	/** Returns the Source-Block */
	Block getSourceBlock();
	
	/**
	 * Is the Source-Block a fence or a different type of block? Used for
	 * manual.
	 */
	boolean isFence();
	
	/** Returns the Post Material name. (Lowercase only) */
	String getName();
	
	/** Returns the Post/Truss-Block registry name. (Lowercase only) */
	String getBlockName();
	
	/** Returns {@link Properties} for the Post-Block */
	Properties getBlockProperties();
	
	public static BlockState getPostState(@Nonnull ItemStack stack){
		return getPostState(getPostMaterial(stack));
	}
	
	public static BlockState getPostState(IPostMaterial material){
		Block block = Posts.get(material);
		if(block == null && PostMaterialRegistry.MAP.containsKey(material)){
			block = PostMaterialRegistry.getPostFrom(material).get();
		}
		
		return block.getDefaultState().with(PostBlock.TYPE, EnumPostType.POST_TOP);
	}
	
	public static BlockState getTrussState(@Nonnull ItemStack stack){
		return getTrussState(getPostMaterial(stack));
	}
	
	public static BlockState getTrussState(@Nonnull IPostMaterial material){
		Block block = HorizontalTruss.get(material);
		if(block == null && PostMaterialRegistry.MAP.containsKey(material)){
			block = PostMaterialRegistry.getTrussFrom(material).get();
		}
		
		return block.getDefaultState();
	}
	
	public static IPostMaterial getPostMaterial(@Nonnull ItemStack stack){
		for(EnumPostMaterial mat:EnumPostMaterial.values()){
			if(stack.isItemEqual(mat.getItemStack()) && IPOConfig.MAIN.isEnabled(mat)){
				return mat;
			}
		}
		
		for(IPostMaterial mat:PostMaterialRegistry.MAP.keySet()){
			if(stack.isItemEqual(mat.getItemStack())){
				return mat;
			}
		}
		
		return null;
	}
	
	public static boolean isValidItem(ItemStack stack){
		if(stack == null || stack.isEmpty())
			return false;
		
		for(EnumPostMaterial mat:EnumPostMaterial.values()){
			if(stack.isItemEqual(mat.getItemStack()) && IPOConfig.MAIN.isEnabled(mat)){
				return true;
			}
		}
		
		for(IPostMaterial mat:PostMaterialRegistry.MAP.keySet()){
			if(stack.isItemEqual(mat.getItemStack())){
				return true;
			}
		}
		
		return false;
	}
}
