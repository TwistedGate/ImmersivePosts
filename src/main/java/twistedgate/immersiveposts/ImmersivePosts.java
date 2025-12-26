package twistedgate.immersiveposts;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twistedgate.immersiveposts.api.IPOMod;
import twistedgate.immersiveposts.client.ClientEventHandler;
import twistedgate.immersiveposts.client.ClientProxy;
import twistedgate.immersiveposts.common.CommonProxy;
import twistedgate.immersiveposts.common.ExternalModContent;
import twistedgate.immersiveposts.common.IPOConfig;
import twistedgate.immersiveposts.common.IPOContent;
import twistedgate.immersiveposts.common.IPORegistries;
import twistedgate.immersiveposts.util.loot.IPOLootFunctions;

import java.util.function.Supplier;

/**
 * @author TwistedGate
 */
@Mod(IPOMod.ID)
public class ImmersivePosts{
	public static final Logger log = LogManager.getLogger(IPOMod.ID);
	
	public static CommonProxy proxy = proxy(() -> FMLLoader.getDist().isClient() ? new ClientProxy() : new CommonProxy());
	private static CommonProxy proxy(Supplier<CommonProxy> proxy){
		return proxy.get();
	}
	
	public ImmersivePosts(ModContainer container, Dist dist, IEventBus eBus){
		container.registerConfig(ModConfig.Type.SERVER, IPOConfig.ALL);
		
		NeoForge.EVENT_BUS.addListener(this::addReloadListeners);
		eBus.addListener(this::loadComplete);
		
		IPORegistries.addRegistersToEventBus(eBus);
		
		ExternalModContent.forceClassLoad();
		IPOContent.modConstruction();
		IPOLootFunctions.modConstruction(eBus);
	}
	
	public void loadComplete(FMLLoadCompleteEvent event){
		proxy.completed();
	}
	
	public void addReloadListeners(AddReloadListenerEvent event){
		event.addListener(new ClientEventHandler());
	}
}
