package twistedgate.immersiveposts.api.posts;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import twistedgate.immersiveposts.common.blocks.HorizontalTrussBlock;
import twistedgate.immersiveposts.common.blocks.PostBlock;

/**
 * Simple registry for other mods to add custom posts
 * 
 * @author TwistedGate
 */
public class PostMaterialRegistry{
	static final Map<IPostMaterial, Pair<RegistryObject<? extends Block>, RegistryObject<? extends Block>>> MAP = new HashMap<>();
	
	/**
	 * Registers your PostMaterial and creates all that is necessary for your
	 * shiny new post be noticed/recognized as such by ImmersivePosts.<br>
	 * <br>
	 * This has to be called before trying to use
	 * {@link PostMaterialRegistry#getPostFrom(IPostMaterial)} or
	 * {@link PostMaterialRegistry#getTrussFrom(IPostMaterial)} IF you wish to
	 * use these for <i>something</i>.
	 *
	 */
	public static void register(DeferredRegister<Block> register, IPostMaterial material){
		if(MAP.containsKey(material)){
			throw new IllegalArgumentException("PostMaterial \"" + material.getName() + "\" already exists!");
		}
		
		RegistryObject<PostBlock> post = register.register(material.getBlockName(), () -> new PostBlock(material));
		RegistryObject<HorizontalTrussBlock> truss = register.register(material.getBlockName() + "_truss", () -> new HorizontalTrussBlock(material));
		
		MAP.put(material, Pair.of(post, truss));
	}
	
	/**
	 * @return RegistryObject reference to the Post block of <i>material</i>.
	 */
	public static RegistryObject<? extends Block> getPostFrom(IPostMaterial material){
		Pair<RegistryObject<? extends Block>, RegistryObject<? extends Block>> pair = MAP.get(material);
		return pair != null ? pair.getLeft() : null;
	}
	
	/**
	 * @return RegistryObject reference to the Truss block of <i>material</i>.
	 */
	public static RegistryObject<? extends Block> getTrussFrom(IPostMaterial material){
		Pair<RegistryObject<? extends Block>, RegistryObject<? extends Block>> pair = MAP.get(material);
		return pair != null ? pair.getRight() : null;
	}
}
