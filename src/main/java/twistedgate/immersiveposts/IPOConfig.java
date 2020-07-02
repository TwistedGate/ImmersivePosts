package twistedgate.immersiveposts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

@Mod.EventBusSubscriber(modid=IPOMod.ID, bus=Bus.MOD)
public class IPOConfig{
	private static final Logger log=LogManager.getLogger(IPOMod.ID+"/Config");
	
	protected static final ForgeConfigSpec ALL;
	public static final Posts MAIN;
	
	public static class Posts{
		public final BooleanValue removeIron;
		public final BooleanValue removeGold;
		public final BooleanValue removeCopper;
		public final BooleanValue removeLead;
		public final BooleanValue removeSilver;
		public final BooleanValue removeNickel;
		public final BooleanValue removeConstantan;
		public final BooleanValue removeElectrum;
		public final BooleanValue removeUranium;
		public final BooleanValue removeNether;
		public final BooleanValue removeConcrete;
		public final BooleanValue removeConcreteLeaded;
		
		Posts(ForgeConfigSpec.Builder builder){
			builder.comment("For the removal of unwanted posts.");
			
			removeIron=builder.define("removeIron", false);
			removeGold=builder.define("removeGold", false);
			removeCopper=builder.define("removeCopper", false);
			removeLead=builder.define("removeLead", false);
			removeSilver=builder.define("removeSilver", false);
			removeNickel=builder.define("removeNickel", false);
			removeConstantan=builder.define("removeConstantan", false);
			removeElectrum=builder.define("removeElectrum", false);
			removeUranium=builder.define("removeUranium", false);
			removeNether=builder.define("removeNether", false);
			removeConcrete=builder.define("removeConcrete", false);
			removeConcreteLeaded=builder.define("removeConcreteLeaded", false);
		}
		
		public boolean isDisabled(ResourceLocation loc){
			try{
				String name=loc.getPath().substring(loc.getPath().indexOf('_')+1).toUpperCase();
				log.debug("Calling isDisabled({}) -> {}", loc, name);
				return isDisabled(EnumPostMaterial.valueOf(name));
			}catch(Exception e){
				log.debug("isDisabled({}) failed. ({})", loc, e.getMessage());
				return false;
			}
		}
		
		public boolean isDisabled(EnumPostMaterial material){
			if(material==null) return false;
			
			log.debug("Calling isDisabled({})", material);
			
			switch(material){
				case CONCRETE:			return removeConcrete.get();
				case CONCRETE_LEADED:	return removeConcreteLeaded.get();
				case CONSTANTAN:		return removeConstantan.get();
				case COPPER:			return removeCopper.get();
				case ELECTRUM:			return removeElectrum.get();
				case GOLD:				return removeGold.get();
				case IRON:				return removeIron.get();
				case LEAD:				return removeLead.get();
				case NETHERBRICK:		return removeNether.get();
				case NICKEL:			return removeNickel.get();
				case SILVER:			return removeSilver.get();
				case URANIUM:			return removeUranium.get();
				default:				return false; // Treated, Alu and Steel cannot be disabled.
			}
		}
	}
	
	static{
		ForgeConfigSpec.Builder builder=new ForgeConfigSpec.Builder();
		MAIN=new Posts(builder);
		ALL=builder.build();
	}
	
	@SubscribeEvent
	public static void loading(ModConfig.Loading event){
		log.debug("Config Loading.");
	}
	
	@SubscribeEvent
	public static void reloading(ModConfig.Reloading event){
		log.debug("Config Reloading.");
	}
}
