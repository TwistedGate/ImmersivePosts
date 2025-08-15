package twistedgate.immersiveposts.common;

import com.electronwill.nightconfig.core.Config;
import com.google.common.base.Preconditions;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twistedgate.immersiveposts.IPOMod;

import java.lang.reflect.Field;

@EventBusSubscriber(modid = IPOMod.ID, bus = Bus.MOD)
public class IPOConfig{
	public static final Logger log = LogManager.getLogger(IPOMod.ID + "/Config");
	
	public static final ForgeConfigSpec ALL;
	public static final Posts MAIN;
	
	static{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		MAIN = new Posts(builder);
		ALL = builder.build();
	}
	
	private static Config rawConfig;
	public static Config getRawConfig(){
		if(rawConfig == null){
			try{
				Field childConfig = ForgeConfigSpec.class.getDeclaredField("childConfig");
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
		public final IntValue maxTrussLength;
		
		Posts(ForgeConfigSpec.Builder builder){
			builder.comment("Maximum length of Horizontal Trusses", "Does not affect already existing Trusses when changed", "Default: 8");
			maxTrussLength = builder.defineInRange("maxTrussLength", 8, 3, 128);
		}
	}
	
	@SubscribeEvent
	public static void onConfigReload(ModConfigEvent ev){
	}
}
