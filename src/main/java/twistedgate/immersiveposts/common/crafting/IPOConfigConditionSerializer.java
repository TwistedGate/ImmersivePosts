package twistedgate.immersiveposts.common.crafting;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.google.gson.JsonObject;

import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.IPOConfig;
import twistedgate.immersiveposts.common.crafting.IPOConfigConditionSerializer.IPOConfigCondition;

public class IPOConfigConditionSerializer implements IConditionSerializer<IPOConfigCondition>{
	public static final ResourceLocation ID=new ResourceLocation(IPOMod.ID, "cfg");
	
	@Override
	public void write(JsonObject json, IPOConfigCondition condition){
		json.addProperty("key", condition.key);
		json.addProperty("value", condition.value);
	}
	
	@Override
	public IPOConfigCondition read(JsonObject json){
		String key=JSONUtils.getString(json, "key");
		boolean value=JSONUtils.getBoolean(json, "value");
		
		return new IPOConfigCondition(key, value);
	}
	
	@Override
	public ResourceLocation getID(){
		return ID;
	}
	
	public static class IPOConfigCondition implements ICondition{
		private final boolean value;
		private final String key;
		public IPOConfigCondition(String key, boolean value){
			this.key=key;
			this.value=value;
		}
		
		@Override
		public ResourceLocation getID(){
			return ID;
		}
		
		@Override
		public boolean test(){
			UnmodifiableConfig cfg=IPOConfig.ALL.getValues();
			Object cfgEntry=cfg.get(this.key);
			
			if(cfgEntry==null){
				// this isnt the best way of doing it, but its better than nothing.
				cfgEntry=((Config)cfg.get("Post Types")).get(this.key);
			}
			
			if(cfgEntry instanceof ForgeConfigSpec.BooleanValue){
				Boolean cfgValue=((BooleanValue)cfgEntry).get();
				return cfgValue!=null && cfgValue==value;
			}
			IPOConfig.log.error("[IPOConfigCondition]: Unknown key \"{}\"", this.key);
			return false;
		}
	}
}
