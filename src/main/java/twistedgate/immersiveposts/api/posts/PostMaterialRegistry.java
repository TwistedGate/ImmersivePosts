package twistedgate.immersiveposts.api.posts;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.tuple.Pair;
import twistedgate.immersiveposts.common.blocks.HorizontalTrussBlock;
import twistedgate.immersiveposts.common.blocks.PostBlock;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple registry for other mods to add custom posts
 * 
 * @author TwistedGate
 */
public class PostMaterialRegistry{
	static final Map<IPostMaterial, Pair<DeferredHolder<Block, ? extends Block>, DeferredHolder<Block, ? extends Block>>> MAP = new HashMap<>();
	
	/**
	 * Registers your PostMaterial and creates all that is necessary for your
	 * shiny new post be noticed/recognized as such by ImmersivePosts.<br>
	 * <br>
	 * This has to be called before trying to use
	 * {@link PostMaterialRegistry#getPostFrom(IPostMaterial)} or
	 * {@link PostMaterialRegistry#getTrussFrom(IPostMaterial)} IF you wish to
	 * use these for <i>something</i>.
	 * 
	 * @param register
	 * @param material
	 */
	public static void register(DeferredRegister<Block> register, IPostMaterial material){
		if(MAP.containsKey(material)){
			throw new IllegalArgumentException("PostMaterial \"" + material.getName() + "\" already exists!");
		}
		
		DeferredHolder<Block, PostBlock> post = register.register(material.getBlockName(), () -> new PostBlock(material));
		DeferredHolder<Block, HorizontalTrussBlock> truss = register.register(material.getBlockName() + "_truss", () -> new HorizontalTrussBlock(material));
		
		MAP.put(material, Pair.of(post, truss));
	}
	
	/**
	 * @param material
	 * @return DeferredHolder reference to the Post block of <i>material</i>.
	 */
	public static final DeferredHolder<Block, ? extends Block> getPostFrom(IPostMaterial material){
		Pair<DeferredHolder<Block, ? extends Block>, DeferredHolder<Block, ? extends Block>> pair = MAP.get(material);
		return pair != null ? pair.getLeft() : null;
	}
	
	/**
	 * @param material
	 * @return DeferredHolder reference to the Truss block of <i>material</i>.
	 */
	public static final DeferredHolder<Block, ? extends Block> getTrussFrom(IPostMaterial material){
		Pair<DeferredHolder<Block, ? extends Block>, DeferredHolder<Block, ? extends Block>> pair = MAP.get(material);
		return pair != null ? pair.getRight() : null;
	}
}
