package twistedgate.immersiveposts.client;

import com.electronwill.nightconfig.core.Config;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualEntry;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.Tree.InnerNode;
import net.minecraft.resources.ResourceLocation;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.CommonProxy;
import twistedgate.immersiveposts.common.IPOConfig;

/**
 * @author TwistedGate
 */
public class ClientProxy extends CommonProxy{
	@Override
	public void completed(){
		ManualHelper.addConfigGetter(str -> {
			switch(str){
				case "maxTrussLength":{
					return IPOConfig.MAIN.maxTrussLength.get();
				}
				default:
					break;
			}
			
			// Last resort
			Config cfg = IPOConfig.getRawConfig();
			if(cfg.contains(str)){
				return cfg.get(str);
			}
			return null;
		});
		
		setupManualPage();
	}
	
	public void setupManualPage(){
		/*
		 * TODO Give src/main/resources/assets/immersiveengineering/manual/autoload.json a shot
		 */
		
		ManualInstance man = ManualHelper.getManual();
		
		InnerNode<ResourceLocation, ManualEntry> cat = man.getRoot().getOrCreateSubnode(modLoc("main"), 100);
		
		man.addEntry(cat, modLoc("postbase"), 0);
		man.addEntry(cat, modLoc("usage"), 1);
		man.addEntry(cat, modLoc("posts"), 2);
	}
	
	private ResourceLocation modLoc(String str){
		return new ResourceLocation(IPOMod.ID, str);
	}
}
