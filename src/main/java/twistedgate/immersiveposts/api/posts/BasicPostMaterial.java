package twistedgate.immersiveposts.api.posts;

import java.util.function.Supplier;

import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * This is an example implementation of {@link IPostMaterial} but can be used as
 * normal.<br>
 * While basic it has the minimum required functionality to create custom posts!<br>
 * Even then, the rest (Models, Textures, Manual entries and so on) has to be handled by you!
 * 
 * @author TwistedGate
 */
public class BasicPostMaterial implements IPostMaterial{
	
	private boolean isFence;
	private String name;
	private Block sourceBlock;
	private Supplier<Block> sourceBlockSupplier;
	private Properties properties;
	private ResourceLocation texture;
	
	/**
	 * @param materialName
	 * @param postBlockProperties is the Block Properties of the
	 *        Post/Truss-Block
	 * @param postBlockTexture is the Texture used for the Post/Truss-Block
	 * @param sourceBlockSupplier is used to right-click the PostBase with and
	 *        already placed Post/Truss-Blocks.<br>This can be *any* block you
	 *        want, and does not strictly have to be a Fence block.
	 */
	public BasicPostMaterial(String materialName, Properties postBlockProperties, ResourceLocation postBlockTexture, Supplier<Block> sourceBlockSupplier){
		this.name = materialName;
		this.properties = postBlockProperties;
		this.texture = postBlockTexture;
		this.sourceBlockSupplier = sourceBlockSupplier;
	}
	
	@Override
	public ItemStack getItemStack(){
		Block block = getSourceBlock();
		return block == null ? ItemStack.EMPTY : new ItemStack(block);
	}
	
	@Override
	public ResourceLocation getTexture(){
		return this.texture;
	}
	
	@Override
	public Block getSourceBlock(){
		if(this.sourceBlock == null){
			this.sourceBlock = this.sourceBlockSupplier.get();
			this.isFence = (this.sourceBlock != null && this.sourceBlock instanceof FenceBlock);
		}
		return this.sourceBlock;
	}
	
	@Override
	public boolean isFence(){
		return this.isFence;
	}
	
	@Override
	public String getName(){
		return this.name;
	}
	
	@Override
	public String getBlockName(){
		return this.name + "post";
	}
	
	@Override
	public Properties getBlockProperties(){
		return this.properties;
	}
}
