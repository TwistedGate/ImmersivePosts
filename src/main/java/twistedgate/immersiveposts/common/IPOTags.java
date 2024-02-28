package twistedgate.immersiveposts.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import twistedgate.immersiveposts.IPOMod;

public class IPOTags{
	public static final TagKey<Block> IGNORED_BY_POSTARM = createBlockWrapper(new ResourceLocation(IPOMod.ID, "ignored_by_postarm"));
	
	public static class Ingots{
		public static final TagKey<Item> IRON = ingotTag("iron");
		public static final TagKey<Item> GOLD = ingotTag("gold");
		public static final TagKey<Item> COPPER = ingotTag("copper");
		public static final TagKey<Item> LEAD = ingotTag("lead");
		public static final TagKey<Item> SILVER = ingotTag("silver");
		public static final TagKey<Item> NICKEL = ingotTag("nickel");
		public static final TagKey<Item> CONSTANTAN = ingotTag("constantan");
		public static final TagKey<Item> ELECTRUM = ingotTag("electrum");
		public static final TagKey<Item> URANIUM = ingotTag("uranium");
		
		private static TagKey<Item> ingotTag(String name){
			return createItemWrapper(new ResourceLocation("forge", "ingots/" + name));
		}
	}
	
	public static class Rods{
		public static final TagKey<Item> ALL = rodTag("all_metal");
		
		public static final TagKey<Item> IRON = rodTag("iron");
		public static final TagKey<Item> GOLD = rodTag("gold");
		public static final TagKey<Item> COPPER = rodTag("copper");
		public static final TagKey<Item> LEAD = rodTag("lead");
		public static final TagKey<Item> SILVER = rodTag("silver");
		public static final TagKey<Item> NICKEL = rodTag("nickel");
		public static final TagKey<Item> CONSTANTAN = rodTag("constantan");
		public static final TagKey<Item> ELECTRUM = rodTag("electrum");
		public static final TagKey<Item> URANIUM = rodTag("uranium");
		
		private static TagKey<Item> rodTag(String name){
			return createItemWrapper(new ResourceLocation("forge", "rods/" + name));
		}
	}
	
	public static class Fences{
		public static final TagKey<Block> ALL = createBlockWrapper(new ResourceLocation("fences"));
	}
	
	private static TagKey<Item> createItemWrapper(ResourceLocation name){
        return TagKey.create(Registries.ITEM, name);
	}
	
	private static TagKey<Block> createBlockWrapper(ResourceLocation name){
		return TagKey.create(Registries.BLOCK, name);
	}
}
