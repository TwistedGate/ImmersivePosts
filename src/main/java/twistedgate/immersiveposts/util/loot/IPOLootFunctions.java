package twistedgate.immersiveposts.util.loot;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;

public class IPOLootFunctions{
	public static LootPoolEntryType baseCoverDrop;
	public static LootPoolEntryType postDrop;
	
	public static void modConstruction(){
		baseCoverDrop = registerEntry(BaseCoverDropLootEntry.ID, new BaseCoverDropLootEntry.Serializer());
		postDrop = registerEntry(PostMaterialDropLootEntry.ID, new PostMaterialDropLootEntry.Serializer());
	}
	
	private static LootPoolEntryType registerEntry(ResourceLocation id, Serializer<? extends LootPoolEntryContainer> serializer){
		return Registry.register(Registry.LOOT_POOL_ENTRY_TYPE, id, new LootPoolEntryType(serializer));
	}
}
