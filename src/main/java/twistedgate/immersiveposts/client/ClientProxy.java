package twistedgate.immersiveposts.client;

import blusunrize.immersiveengineering.api.ManualHelper;
import com.electronwill.nightconfig.core.Config;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import twistedgate.immersiveposts.common.CommonProxy;
import twistedgate.immersiveposts.common.IPOConfig;

/**
 * @author TwistedGate
 */
public class ClientProxy extends CommonProxy{
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
}
