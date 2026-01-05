package twistedgate.immersiveposts.common;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

/**
 * @author TwistedGate
 */
public class CommonProxy{
	public void construct(FMLConstructModEvent event){
	}
	
	public void commonSetup(FMLCommonSetupEvent event){
	}
	
	public void clientSetup(FMLClientSetupEvent event){
	}
	
	public void serverSetup(FMLDedicatedServerSetupEvent event){
	}
	
	public void completed(FMLLoadCompleteEvent event){
	}
	
	public void addReloadListeners(AddReloadListenerEvent event){
	}
}
