package twistedgate.immersiveposts.client;

import blusunrize.immersiveengineering.api.ManualHelper;
import com.electronwill.nightconfig.core.Config;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import twistedgate.immersiveposts.client.model.PostBaseModel;
import twistedgate.immersiveposts.common.CommonProxy;
import twistedgate.immersiveposts.common.IPOConfig;

/**
 * @author TwistedGate
 */
public class ClientProxy extends CommonProxy{
	
	@Override
	public void construct(FMLConstructModEvent event){
	}
	
	@Override
	public void commonSetup(FMLCommonSetupEvent event){
		NeoForge.EVENT_BUS.addListener(this::addReloadListeners);
	}
	
	@Override
	public void clientSetup(FMLClientSetupEvent event){
	}
	
	@Override
	public void serverSetup(FMLDedicatedServerSetupEvent event){
	}
	
	@Override
	public void completed(FMLLoadCompleteEvent event){
		event.enqueueWork(() -> ManualHelper.addConfigGetter(str -> switch(str){
			case "maxTrussLength" -> IPOConfig.MAIN.maxTrussLength.get();
			default -> {
				// Last resort
				Config cfg = IPOConfig.getRawConfig();
				if(cfg.contains(str)){
					yield cfg.get(str);
				}
				yield null;
			}
		}));
	}
	
	@Override
	public void addReloadListeners(AddReloadListenerEvent event){
		event.addListener(new PostBaseModel.ReloadListener());
	}
}
