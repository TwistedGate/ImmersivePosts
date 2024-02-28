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
	
	/** Source-block itemStack */
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
	
	/** Gets the default post blockstate of the given source-block itemStack */
	static BlockState getPostState(@Nonnull ItemStack stack){
		return getPostState(getPostMaterial(stack));
	}
	
	/** Gets the default post blockstate of the given material */
	@SuppressWarnings("all")
	static BlockState getPostState(IPostMaterial material){
		Block block = Posts.get(material);
		if(block == null && PostMaterialRegistry.MAP.containsKey(material)){
			block = PostMaterialRegistry.getPostFrom(material).get();
		}
		
		return block.defaultBlockState();
	}
	
	/** Gets the default truss blockstate of the given source-block itemstack */
	//Useless? Consider removing it!
	@SuppressWarnings("all")
	static BlockState getTrussState(@Nonnull ItemStack stack){
		return getTrussState(getPostMaterial(stack));
	}
	
	/** Gets the default truss blockstate of the given material */
	@SuppressWarnings("all")
	static BlockState getTrussState(@Nonnull IPostMaterial material){
		Block block = HorizontalTruss.get(material);
		if(block == null && PostMaterialRegistry.MAP.containsKey(material)){
			block = PostMaterialRegistry.getTrussFrom(material).get();
		}

		return block.defaultBlockState();
	}
	
	/** Gets the material of the given source-block itemStack */
	static IPostMaterial getPostMaterial(@Nonnull ItemStack stack){
		for(EnumPostMaterial mat:EnumPostMaterial.values()){
			if(stack.is(mat.getItemStack().getItem())){
				return mat;
			}
		}
		
		for(IPostMaterial mat:PostMaterialRegistry.MAP.keySet()){
			if(stack.is(mat.getItemStack().getItem())){
				return mat;
			}
		}
		
		return null;
	}
	
	static boolean isValidItem(ItemStack stack){
		if(stack == null || stack.isEmpty())
			return false;
		
		for(EnumPostMaterial mat:EnumPostMaterial.values()){
			if(stack.is(mat.getItemStack().getItem())){
				return true;
			}
		}
		
		for(IPostMaterial mat:PostMaterialRegistry.MAP.keySet()){
			if(stack.is(mat.getItemStack().getItem())){
				return true;
			}
		}
		
		return false;
	}
}
