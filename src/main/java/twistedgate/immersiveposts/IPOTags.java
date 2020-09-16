package twistedgate.immersiveposts;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class IPOTags{
	public static class Ingots{
		public static final ITag.INamedTag<Item> IRON=ingotTag("iron");
		public static final ITag.INamedTag<Item> GOLD=ingotTag("gold");
		public static final ITag.INamedTag<Item> COPPER=ingotTag("copper");
		public static final ITag.INamedTag<Item> LEAD=ingotTag("lead");
		public static final ITag.INamedTag<Item> SILVER=ingotTag("silver");
		public static final ITag.INamedTag<Item> NICKEL=ingotTag("nickel");
		public static final ITag.INamedTag<Item> CONSTANTAN=ingotTag("constantan");
		public static final ITag.INamedTag<Item> ELECTRUM=ingotTag("electrum");
		public static final ITag.INamedTag<Item> URANIUM=ingotTag("uranium");
		
		private static final ITag.INamedTag<Item> ingotTag(String name){
			return createItemWrapper(new ResourceLocation("forge","ingots/"+name));
		}
	}
	
	public static class Rods{
		public static final ITag.INamedTag<Item> ALL=rodTag("all_metal");

		public static final ITag.INamedTag<Item> IRON=rodTag("iron");
		public static final ITag.INamedTag<Item> GOLD=rodTag("gold");
		public static final ITag.INamedTag<Item> COPPER=rodTag("copper");
		public static final ITag.INamedTag<Item> LEAD=rodTag("lead");
		public static final ITag.INamedTag<Item> SILVER=rodTag("silver");
		public static final ITag.INamedTag<Item> NICKEL=rodTag("nickel");
		public static final ITag.INamedTag<Item> CONSTANTAN=rodTag("constantan");
		public static final ITag.INamedTag<Item> ELECTRUM=rodTag("electrum");
		public static final ITag.INamedTag<Item> URANIUM=rodTag("uranium");
		
		private static final ITag.INamedTag<Item> rodTag(String name){
			return createItemWrapper(new ResourceLocation("forge","rods/"+name));
		}
	}
	
	public static class Fences{
		public static final ITag.INamedTag<Block> ALL=createBlockWrapper(new ResourceLocation("fences"));
	}
	
	private static ITag.INamedTag<Item> createItemWrapper(ResourceLocation name){
		return createGenericWrapper(ItemTags.func_242177_b(), name, ItemTags::makeWrapperTag);
	}
	
	private static ITag.INamedTag<Block> createBlockWrapper(ResourceLocation name){
		return createGenericWrapper(BlockTags.func_242174_b(), name, BlockTags::makeWrapperTag);
	}
	
	private static <T> INamedTag<T> createGenericWrapper(List<? extends INamedTag<T>> tags, ResourceLocation name, Function<String, INamedTag<T>> createNew){
		Optional<? extends INamedTag<T>> existing = tags.stream().filter(tag -> tag.getName().equals(name)).findAny();
		if(existing.isPresent()){
			return existing.get();
		}else{
			return createNew.apply(name.toString());
		}
	}
}
