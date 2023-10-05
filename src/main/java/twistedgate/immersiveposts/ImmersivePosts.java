package twistedgate.immersiveposts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import twistedgate.immersiveposts.client.ClientEventHandler;
import twistedgate.immersiveposts.client.ClientProxy;
import twistedgate.immersiveposts.common.CommonProxy;
import twistedgate.immersiveposts.common.ExternalModContent;
import twistedgate.immersiveposts.common.IPOConfig;
import twistedgate.immersiveposts.common.IPOContent;
import twistedgate.immersiveposts.common.IPORegistries;
import twistedgate.immersiveposts.util.loot.IPOLootFunctions;

/**
 * @author TwistedGate
 */
@Mod(IPOMod.ID)
public class ImmersivePosts{
	
	@Deprecated(forRemoval = true)
	public static final CreativeModeTab creativeTab = null;
	
	public static final Logger log = LogManager.getLogger(IPOMod.ID);
	
	public static CommonProxy proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	
	public ImmersivePosts(){
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, IPOConfig.ALL);
		
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::clientSetup);
		bus.addListener(this::loadComplete);
		
		IPORegistries.addRegistersToEventBus(bus);
		IPOLootFunctions.addRegistersToEventBus(bus);
		
		ExternalModContent.forceClassLoad();
		IPOContent.modConstruction();
	}
	
	public void clientSetup(FMLClientSetupEvent event){
		MinecraftForge.EVENT_BUS.addListener(this::addReloadListeners);
	}
	
	public void loadComplete(FMLLoadCompleteEvent event){
		proxy.completed();
	}
	
	public void addReloadListeners(AddReloadListenerEvent event){
		event.addListener(new ClientEventHandler());
	}
}
