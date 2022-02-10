package twistedgate.immersiveposts;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
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
		final BasicPostMaterial glassPost = new BasicPostMaterial("glass", Properties.of(Material.GLASS), new ResourceLocation(MODID, "block/post/glasspost"), () -> Blocks.GLASS);
		final BasicPostMaterial cobblePost = new BasicPostMaterial("cobblestone", Properties.of(Material.STONE), new ResourceLocation("block/cobblestone"), () -> Blocks.COBBLESTONE);
		
		PostMaterialRegistry.register(BLOCK_REGISTER, glassPost);
		PostMaterialRegistry.register(BLOCK_REGISTER, cobblePost);
		
		TEST_POSTBLOCK = PostMaterialRegistry.getPostFrom(glassPost);
		TEST_TRUSSBLOCK = PostMaterialRegistry.getTrussFrom(glassPost);
		
		PostMaterialRegistry.getPostFrom(cobblePost);
		PostMaterialRegistry.getTrussFrom(cobblePost);
	}
}
