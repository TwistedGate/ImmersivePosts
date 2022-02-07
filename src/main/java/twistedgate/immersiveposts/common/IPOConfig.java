package twistedgate.immersiveposts.common;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.electronwill.nightconfig.core.Config;
import com.google.common.base.Preconditions;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

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
		public final BooleanValue enableIron;
		public final BooleanValue enableGold;
		public final BooleanValue enableCopper;
		public final BooleanValue enableLead;
		public final BooleanValue enableSilver;
		public final BooleanValue enableNickel;
		public final BooleanValue enableConstantan;
		public final BooleanValue enableElectrum;
		public final BooleanValue enableUranium;
		public final BooleanValue enableNether;
		public final BooleanValue enableConcrete;
		public final BooleanValue enableConcreteLeaded;
		
		public final IntValue maxTrussLength;
		
		Posts(ForgeConfigSpec.Builder builder){
			builder.comment("Maximum length of Horizontal Trusses", "Does not affect already existing Trusses when changed", "Default: 8");
			maxTrussLength = builder.defineInRange("maxTrussLength", 8, 3, 128);
			
			builder.push("Post Types");
			{
				builder.comment("For the removal of unwanted post variants.", "Simply set any of them to \"false\" to \"remove\", ez..");
				
				enableIron			= builder.define("iron", true);
				enableGold			= builder.define("gold", true);
				enableCopper		= builder.define("copper", true);
				enableLead			= builder.define("lead", true);
				enableSilver		= builder.define("silver", true);
				enableNickel		= builder.define("nickel", true);
				enableConstantan	= builder.define("constantan", true);
				enableElectrum		= builder.define("electrum", true);
				enableUranium		= builder.define("uranium", true);
				enableNether		= builder.define("nether", true);
				enableConcrete		= builder.define("concrete", true);
				enableConcreteLeaded= builder.define("concreteleaded", true);
			}
			builder.pop();
		}
		
		public boolean isEnabled(ResourceLocation loc){
			try{
				String name = loc.getPath().substring(loc.getPath().indexOf('_') + 1).toUpperCase();
				return isEnabled(EnumPostMaterial.valueOf(name));
			}catch(Exception e){
				return true;
			}
		}
		
		public boolean isEnabled(EnumPostMaterial material){
			if(material==null) return true;
			
			switch(material){
				case CONCRETE:			return enableConcrete.get();
				case CONCRETE_LEADED:	return enableConcreteLeaded.get();
				case CONSTANTAN:		return enableConstantan.get();
				case COPPER:			return enableCopper.get();
				case ELECTRUM:			return enableElectrum.get();
				case GOLD:				return enableGold.get();
				case IRON:				return enableIron.get();
				case LEAD:				return enableLead.get();
				case NETHERBRICK:		return enableNether.get();
				case NICKEL:			return enableNickel.get();
				case SILVER:			return enableSilver.get();
				case URANIUM:			return enableUranium.get();
				default:				return true; // Treated, Alu and Steel cannot be disabled.
			}
		}
	}
	
	@SubscribeEvent
	public static void onConfigReload(ModConfigEvent ev){
	}
}
