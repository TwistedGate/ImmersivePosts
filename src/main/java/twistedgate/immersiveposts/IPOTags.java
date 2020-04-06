package twistedgate.immersiveposts;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class IPOTags{
	public static class Ingots{
		public static final Tag<Item> IRON=ingotTag("iron");
		public static final Tag<Item> GOLD=ingotTag("gold");
		public static final Tag<Item> COPPER=ingotTag("copper");
		public static final Tag<Item> LEAD=ingotTag("lead");
		public static final Tag<Item> SILVER=ingotTag("silver");
		public static final Tag<Item> NICKEL=ingotTag("nickel");
		public static final Tag<Item> CONSTANTAN=ingotTag("constantan");
		public static final Tag<Item> ELECTRUM=ingotTag("electrum");
		public static final Tag<Item> URANIUM=ingotTag("uranium");
		
		private static final Tag<Item> ingotTag(String name){
			return new ItemTags.Wrapper(new ResourceLocation("forge","ingots/"+name));
		}
		
		private Ingots(){}
	}
	
	public static class Rods{
		public static final Tag<Item> ALL=rodTag("all_metal");

		public static final Tag<Item> IRON=rodTag("iron");
		public static final Tag<Item> GOLD=rodTag("gold");
		public static final Tag<Item> COPPER=rodTag("copper");
		public static final Tag<Item> LEAD=rodTag("lead");
		public static final Tag<Item> SILVER=rodTag("silver");
		public static final Tag<Item> NICKEL=rodTag("nickel");
		public static final Tag<Item> CONSTANTAN=rodTag("constantan");
		public static final Tag<Item> ELECTRUM=rodTag("electrum");
		public static final Tag<Item> URANIUM=rodTag("uranium");
		
		private static final Tag<Item> rodTag(String name){
			return new ItemTags.Wrapper(new ResourceLocation("forge","rods/"+name));
		}
		
		private Rods(){}
	}
	
	private IPOTags(){}
}
