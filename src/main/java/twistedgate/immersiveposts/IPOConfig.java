package twistedgate.immersiveposts;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

@EventBusSubscriber(modid=IPOMod.ID, bus=Bus.MOD)
public class IPOConfig{
	
	public static final ForgeConfigSpec VALUES;
	
	static{
		ForgeConfigSpec.Builder builder=new ForgeConfigSpec.Builder();
		
		disableAll=builder
				.comment("Sets all to false for you.", "§5Resets automaticly.").define("disableAll", false);
		enableAll=builder
				.comment("Sets all to true for you.","§5Resets automaticly.").define("enableAll", false);
		
		enableIronPost=builder
				.comment("Removes this post type entirely.").define("enableIron", true);
		enableGoldPost=builder
				.comment("Removes this post type entirely.").define("enableGold", true);
		enableCopperPost=builder
				.comment("Removes this post type entirely.").define("enableCopper", true);
		enableLeadPost=builder
				.comment("Removes this post type entirely.").define("enableLead", true);
		enableSilverPost=builder
				.comment("Removes this post type entirely.").define("enableSilver", true);
		enableNickelPost=builder
				.comment("Removes this post type entirely.").define("enableNickel", true);
		enableConstantanPost=builder
				.comment("Removes this post type entirely.").define("enableConstantan", true);
		enableElectrumPost=builder
				.comment("Removes this post type entirely.").define("enableElectrum", true);
		enableUraniumPost=builder
				.comment("Removes this post type entirely.").define("enableUranium", true);
		enableNetherPost=builder
				.comment("Removes this post type entirely.").define("enableNether", true);
		enableConcretePost=builder
				.comment("Removes this post type entirely.").define("enableConcrete", true);
		enableConcreteLeadedPost=builder
				.comment("Removes this post type entirely.").define("enableConcreteLeaded", true);
		
		VALUES=builder.build();
		
	}
	
	public static BooleanValue disableAll;
	public static BooleanValue enableAll;
	
	public static BooleanValue enableIronPost;
	public static BooleanValue enableGoldPost;
	public static BooleanValue enableCopperPost;
	public static BooleanValue enableLeadPost;
	public static BooleanValue enableSilverPost;
	public static BooleanValue enableNickelPost;
	public static BooleanValue enableConstantanPost;
	public static BooleanValue enableElectrumPost;
	public static BooleanValue enableUraniumPost;
	public static BooleanValue enableNetherPost;
	public static BooleanValue enableConcretePost;
	public static BooleanValue enableConcreteLeadedPost;
	
	public static boolean isEnabled(EnumPostMaterial material){
		if(material==null) return false;
		
		switch(material){
			case CONCRETE:			return enableConcretePost.get();
			case CONCRETE_LEADED:	return enableConcreteLeadedPost.get();
			case CONSTANTAN:		return enableConstantanPost.get();
			case COPPER:			return enableCopperPost.get();
			case ELECTRUM:			return enableElectrumPost.get();
			case GOLD:				return enableGoldPost.get();
			case IRON:				return enableIronPost.get();
			case LEAD:				return enableLeadPost.get();
			case NETHERBRICK:		return enableNetherPost.get();
			case NICKEL:			return enableNickelPost.get();
			case SILVER:			return enableSilverPost.get();
			case URANIUM:			return enableUraniumPost.get();
			default:				return true; // For defaults
		}
	}
	
	@SubscribeEvent
	public static void onConfigChangedEvent(OnConfigChangedEvent event){
		if(event.getModID().equals(IPOMod.ID)){
			//ConfigManager.sync(IPOMod.ID, Type.INSTANCE);
			
			if(disableAll.get()){
				disableAll.set(false);
				changeAll(false);
			}
			if(enableAll.get()){
				enableAll.set(false);
				changeAll(true);
			}
			
			//ConfigManager.sync(IPOMod.ID, Type.INSTANCE);
		}
	}
	
	private static void changeAll(boolean value){
		enableNetherPost.set(value);
		enableIronPost.set(value);
		enableGoldPost.set(value);
		enableCopperPost.set(value);
		enableLeadPost.set(value);
		enableSilverPost.set(value);
		enableNickelPost.set(value);
		enableConstantanPost.set(value);
		enableElectrumPost.set(value);
		enableConcretePost.set(value);
		enableConcreteLeadedPost.set(value);
		enableUraniumPost.set(value);
		
		VALUES.save();
	}
}
