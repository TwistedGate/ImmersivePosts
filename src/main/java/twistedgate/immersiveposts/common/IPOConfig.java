package twistedgate.immersiveposts.common;

import com.electronwill.nightconfig.core.Config;
import com.google.common.base.Preconditions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twistedgate.immersiveposts.api.IPOMod;

import java.lang.reflect.Field;

@EventBusSubscriber(modid = IPOMod.ID, bus = Bus.MOD)
public class IPOConfig{
	public static final Logger log = LogManager.getLogger(IPOMod.ID + "/Config");
	
	public static final ModConfigSpec ALL;
	public static final Posts MAIN;
	
	static{
		Builder builder = new Builder();
		MAIN = new Posts(builder);
		ALL = builder.build();
	}
	
	private static Config rawConfig;
	public static Config getRawConfig(){
		if(rawConfig == null){
			try{
				Field childConfig = ModConfigSpec.class.getDeclaredField("childConfig");
				childConfig.setAccessible(true);
				rawConfig = (Config) childConfig.get(ALL);
				Preconditions.checkNotNull(rawConfig);
			}catch(Exception x){
				throw new RuntimeException(x);
			}
		}
		return rawConfig;
	}
	
	public static class Posts{
		public final ConfigValue<Integer> maxTrussLength;
		
		Posts(Builder builder){
			builder.comment("Maximum length of Horizontal Trusses", "Does not affect already existing Trusses when changed", "Default: 8");
			this.maxTrussLength = builder.defineInRange("maxTrussLength", 8, 3, 128);
		}
	}
	
	@SubscribeEvent
	public static void onConfigReload(ModConfigEvent ev){
	}
}
