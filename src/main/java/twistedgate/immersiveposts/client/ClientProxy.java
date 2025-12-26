package twistedgate.immersiveposts.client;

import blusunrize.immersiveengineering.api.ManualHelper;
import com.electronwill.nightconfig.core.Config;
import twistedgate.immersiveposts.common.CommonProxy;
import twistedgate.immersiveposts.common.IPOConfig;

/**
 * @author TwistedGate
 */
public class ClientProxy extends CommonProxy{
	@Override
	public void completed(){
		ManualHelper.addConfigGetter(str -> {
			if(str.equals("maxTrussLength")){
				return IPOConfig.MAIN.maxTrussLength.get();
			}
			
			// Last resort
			Config cfg = IPOConfig.getRawConfig();
			if(cfg.contains(str)){
				return cfg.get(str);
			}
			return null;
		});
	}
}
