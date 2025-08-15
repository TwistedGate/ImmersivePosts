package twistedgate.immersiveposts.api.posts;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import twistedgate.immersiveposts.common.IPOContent.Blocks.HorizontalTruss;
import twistedgate.immersiveposts.common.IPOContent.Blocks.Posts;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

import javax.annotation.Nonnull;

public interface IPostMaterial{
	
	/** Source-block ItemStack */
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
	
	/** Gets the default post BlockState of the given source-block ItemStack */
	static BlockState getPostState(@Nonnull ItemStack stack){
		return getPostState(getPostMaterial(stack));
	}
	
	/** Gets the default post BlockState of the given material */
	static BlockState getPostState(IPostMaterial material){
		Block block = Posts.get(material);
		if(block == null && PostMaterialRegistry.MAP.containsKey(material)){
			block = PostMaterialRegistry.getPostFrom(material).get();
		}
		
		return block.defaultBlockState();
	}
	
	/** Gets the default truss BlockState of the given source-block ItemStack */
	static BlockState getTrussState(@Nonnull ItemStack stack){
		return getTrussState(getPostMaterial(stack));
	}
	
	/** Gets the default truss BlockState of the given material */
	static BlockState getTrussState(@Nonnull IPostMaterial material){
		Block block = HorizontalTruss.get(material);
		if(block == null && PostMaterialRegistry.MAP.containsKey(material)){
			block = PostMaterialRegistry.getTrussFrom(material).get();
		}
		
		return block.defaultBlockState();
	}
	
	/** Gets the material of the given source-block ItemStack */
	static IPostMaterial getPostMaterial(@Nonnull ItemStack stack){
		for(EnumPostMaterial mat:EnumPostMaterial.values()){
			if(ItemStack.isSameItem(stack, mat.getItemStack())){
				return mat;
			}
		}
		
		for(IPostMaterial mat:PostMaterialRegistry.MAP.keySet()){
			if(ItemStack.isSameItem(stack, mat.getItemStack())){
				return mat;
			}
		}
		
		return null;
	}
	
	static boolean isValidItem(ItemStack stack){
		if(stack == null || stack.isEmpty())
			return false;
		
		for(EnumPostMaterial mat:EnumPostMaterial.values()){
			if(ItemStack.isSameItem(stack, mat.getItemStack())){
				return true;
			}
		}
		
		for(IPostMaterial mat:PostMaterialRegistry.MAP.keySet()){
			if(ItemStack.isSameItem(stack, mat.getItemStack())){
				return true;
			}
		}
		
		return false;
	}
}
