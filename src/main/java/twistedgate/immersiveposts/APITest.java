package twistedgate.immersiveposts;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twistedgate.immersiveposts.api.posts.BasicPostMaterial;
import twistedgate.immersiveposts.api.posts.PostMaterialRegistry;

/**
 * Can also serve as a sort of example on how this works?
 * 
 * @author TwistedGate
 */
//@Mod("apitest")
public class APITest{
	
	// Stand in for YOUR mod id
	static final String MODID = "apitest";
	
	// Stand in for YOUR block register
	static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK, MODID);
	
	public static DeferredHolder<Block, ? extends Block> TEST_POSTBLOCK;
	public static DeferredHolder<Block, ? extends Block> TEST_TRUSSBLOCK;
	
	public APITest(ModContainer container, Dist dist, IEventBus eBus){
		BLOCK_REGISTER.register(eBus);
		
		// Stand-in for YOUR PostMaterial
		final BasicPostMaterial glassPost = new BasicPostMaterial("glass", Properties.ofFullCopy(Blocks.GLASS), ResourceLocation.fromNamespaceAndPath(MODID, "block/post/glasspost"), () -> Blocks.GLASS);
		final BasicPostMaterial cobblePost = new BasicPostMaterial("cobblestone", Properties.ofFullCopy(Blocks.STONE), ResourceLocation.withDefaultNamespace("block/cobblestone"), () -> Blocks.COBBLESTONE);
		
		PostMaterialRegistry.register(BLOCK_REGISTER, glassPost);
		PostMaterialRegistry.register(BLOCK_REGISTER, cobblePost);
		
		TEST_POSTBLOCK = PostMaterialRegistry.getPostFrom(glassPost);
		TEST_TRUSSBLOCK = PostMaterialRegistry.getTrussFrom(glassPost);
		
		PostMaterialRegistry.getPostFrom(cobblePost);
		PostMaterialRegistry.getTrussFrom(cobblePost);
	}
}
