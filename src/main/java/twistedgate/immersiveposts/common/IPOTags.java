package twistedgate.immersiveposts.common;

import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import twistedgate.immersiveposts.IPOMod;

public class IPOTags{
	public static final Tag.Named<Block> IGNORED_BY_POSTARM = createBlockWrapper(new ResourceLocation(IPOMod.ID, "ignored_by_postarm"));
	
	public static class Ingots{
		public static final Tag.Named<Item> IRON = ingotTag("iron");
		public static final Tag.Named<Item> GOLD = ingotTag("gold");
		public static final Tag.Named<Item> COPPER = ingotTag("copper");
		public static final Tag.Named<Item> LEAD = ingotTag("lead");
		public static final Tag.Named<Item> SILVER = ingotTag("silver");
		public static final Tag.Named<Item> NICKEL = ingotTag("nickel");
		public static final Tag.Named<Item> CONSTANTAN = ingotTag("constantan");
		public static final Tag.Named<Item> ELECTRUM = ingotTag("electrum");
		public static final Tag.Named<Item> URANIUM = ingotTag("uranium");
		
		private static final Tag.Named<Item> ingotTag(String name){
			return createItemWrapper(new ResourceLocation("forge", "ingots/" + name));
		}
	}
	
	public static class Rods{
		public static final Tag.Named<Item> ALL = rodTag("all_metal");
		
		public static final Tag.Named<Item> IRON = rodTag("iron");
		public static final Tag.Named<Item> GOLD = rodTag("gold");
		public static final Tag.Named<Item> COPPER = rodTag("copper");
		public static final Tag.Named<Item> LEAD = rodTag("lead");
		public static final Tag.Named<Item> SILVER = rodTag("silver");
		public static final Tag.Named<Item> NICKEL = rodTag("nickel");
		public static final Tag.Named<Item> CONSTANTAN = rodTag("constantan");
		public static final Tag.Named<Item> ELECTRUM = rodTag("electrum");
		public static final Tag.Named<Item> URANIUM = rodTag("uranium");
		
		private static final Tag.Named<Item> rodTag(String name){
			return createItemWrapper(new ResourceLocation("forge", "rods/" + name));
		}
	}
	
	public static class Fences{
		public static final Tag.Named<Block> ALL = createBlockWrapper(new ResourceLocation("fences"));
	}
	
	private static Tag.Named<Item> createItemWrapper(ResourceLocation name){
		return createGenericWrapper(name, ItemTags::bind);
	}
	
	private static Tag.Named<Block> createBlockWrapper(ResourceLocation name){
		return createGenericWrapper(name, BlockTags::bind);
	}
	
	private static <T> Tag.Named<T> createGenericWrapper(ResourceLocation name, Function<String, Tag.Named<T>> createNew){
		return createNew.apply(name.toString());
	}
}
