package twistedgate.immersiveposts;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
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
		
		if(dist == Dist.CLIENT)
			eBus.register(new ClientEventHandler());
		
		eBus.addListener(this::construct);
		eBus.addListener(this::commonSetup);
		eBus.addListener(this::clientSetup);
		eBus.addListener(this::serverSetup);
		eBus.addListener(this::completed);
		
		NeoForge.EVENT_BUS.addListener(this::addReloadListeners);
		
		IPORegistries.addRegistersToEventBus(eBus);
		
		ExternalModContent.forceClassLoad();
		IPOContent.modConstruction();
		IPOLootFunctions.modConstruction(eBus);
	}
	
	public void construct(FMLConstructModEvent event){
		proxy.construct(event);
	}
	
	public void commonSetup(FMLCommonSetupEvent event){
		proxy.commonSetup(event);
	}
	
	public void clientSetup(FMLClientSetupEvent event){
		proxy.clientSetup(event);
	}
	
	public void serverSetup(FMLDedicatedServerSetupEvent event){
		proxy.serverSetup(event);
	}
	
	public void completed(FMLLoadCompleteEvent event){
		proxy.completed(event);
	}
	
	public void addReloadListeners(AddReloadListenerEvent event){
		proxy.addReloadListeners(event);
	}
}
