package twistedgate.immersiveposts;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

@Config(modid=IPOMod.ID)
@EventBusSubscriber
public class IPOConfig{
	
	public static boolean isEnabled(EnumPostMaterial material){
		if(material==null) return false;
		
		switch(material){
			case CONCRETE:			return enableConcretePost;
			case CONCRETE_LEADED:	return enableConcreteLeadedPost;
			case CONSTANTAN:		return enableConstantanPost;
			case COPPER:			return enableCopperPost;
			case ELECTRUM:			return enableElectrumPost;
			case GOLD:				return enableGoldPost;
			case IRON:				return enableIronPost;
			case LEAD:				return enableLeadPost;
			case NETHERBRICK:		return enableNetherPost;
			case NICKEL:			return enableNickelPost;
			case SILVER:			return enableSilverPost;
			case URANIUM:			return enableUraniumPost;
			default:				return true; // For defaults
		}
	}
	
	@Name("Disable All")
	@Comment({"Sets all to false for you.","§5Resets automaticly."})
	@RequiresMcRestart
	public static boolean disableAll=false;
	
	@Name("Enable All")
	@Comment({"Sets all to true for you.","§5Resets automaticly."})
	@RequiresMcRestart
	public static boolean enableAll=false;
	
	
	@Name("Enable Iron")
	@Comment("Removes this post type entirely.")
	@RequiresMcRestart
	public static boolean enableIronPost=true;
	
	@Name("Enable Gold")
	@Comment("Removes this post type entirely.")
	@RequiresMcRestart
	public static boolean enableGoldPost=true;
	
	@Name("Enable Copper")
	@Comment("Removes this post type entirely.")
	@RequiresMcRestart
	public static boolean enableCopperPost=true;
	
	@Name("Enable Lead")
	@Comment("Removes this post type entirely.")
	@RequiresMcRestart
	public static boolean enableLeadPost=true;
	
	@Name("Enable Silver")
	@Comment("Removes this post type entirely.")
	@RequiresMcRestart
	public static boolean enableSilverPost=true;
	
	@Name("Enable Nickel")
	@Comment("Removes this post type entirely.")
	@RequiresMcRestart
	public static boolean enableNickelPost=true;
	
	@Name("Enable Constantan")
	@Comment("Removes this post type entirely.")
	@RequiresMcRestart
	public static boolean enableConstantanPost=true;
	
	@Name("Enable Electrum")
	@Comment("Removes this post type entirely.")
	@RequiresMcRestart
	public static boolean enableElectrumPost=true;
	
	@Name("Enable Uranium")
	@Comment("Removes this post type entirely.")
	@RequiresMcRestart
	public static boolean enableUraniumPost=true;
	
	@Name("Enable Netherbrick")
	@Comment("Removes this post type entirely.")
	@RequiresMcRestart
	public static boolean enableNetherPost=true;
	
	@Name("Enable Concrete")
	@Comment("Removes this post type entirely.")
	@RequiresMcRestart
	public static boolean enableConcretePost=true;
	
	@Name("Enable Concrete (Leaded)")
	@Comment("Removes this post type entirely.")
	@RequiresMcRestart
	public static boolean enableConcreteLeadedPost=true;
	
	@SubscribeEvent
	public static void onConfigChangedEvent(OnConfigChangedEvent event){
		if(event.getModID().equals(IPOMod.ID)){
			ConfigManager.sync(IPOMod.ID, Type.INSTANCE);
			
			if(disableAll){
				disableAll=false;
				changeAll(false);
			}
			if(enableAll){
				enableAll=false;
				changeAll(true);
			}
			
			ConfigManager.sync(IPOMod.ID, Type.INSTANCE);
		}
	}
	
	private static void changeAll(boolean bool){
		enableNetherPost=bool;
		enableIronPost=bool;
		enableGoldPost=bool;
		enableCopperPost=bool;
		enableLeadPost=bool;
		enableSilverPost=bool;
		enableNickelPost=bool;
		enableConstantanPost=bool;
		enableElectrumPost=bool;
		enableConcretePost=bool;
		enableConcreteLeadedPost=bool;
		enableUraniumPost=bool;
	}
}
