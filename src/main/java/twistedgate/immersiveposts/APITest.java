package twistedgate.immersiveposts;

import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
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
	static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	
	public static RegistryObject<? extends Block> TEST_POSTBLOCK;
	public static RegistryObject<? extends Block> TEST_TRUSSBLOCK;
	
	public APITest(){
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		BLOCK_REGISTER.register(bus);
		
		// Stand-in for YOUR PostMaterial
		final BasicPostMaterial glassPost = new BasicPostMaterial("glass", Properties.create(Material.GLASS), new ResourceLocation(MODID, "block/post/glasspost"), () -> Blocks.GLASS);
		final BasicPostMaterial cobblePost = new BasicPostMaterial("cobblestone", Properties.create(Material.ROCK), new ResourceLocation("block/cobblestone"), () -> Blocks.COBBLESTONE);
		
		PostMaterialRegistry.register(BLOCK_REGISTER, glassPost);
		PostMaterialRegistry.register(BLOCK_REGISTER, cobblePost);
		
		TEST_POSTBLOCK = PostMaterialRegistry.getPostFrom(glassPost);
		TEST_TRUSSBLOCK = PostMaterialRegistry.getTrussFrom(glassPost);
		
		PostMaterialRegistry.getPostFrom(cobblePost);
		PostMaterialRegistry.getTrussFrom(cobblePost);
	}
}
