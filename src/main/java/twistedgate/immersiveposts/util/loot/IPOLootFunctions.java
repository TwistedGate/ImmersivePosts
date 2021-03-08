package twistedgate.immersiveposts.util.loot;

import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPoolEntryType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class IPOLootFunctions{
	public static LootPoolEntryType baseCoverDrop;
	public static LootPoolEntryType postDrop;
	
	public static void modConstruction(){
		baseCoverDrop = registerEntry(BaseCoverDropLootEntry.ID, new BaseCoverDropLootEntry.Serializer());
		postDrop = registerEntry(PostMaterialDropLootEntry.ID, new PostMaterialDropLootEntry.Serializer());
	}
	
	private static LootPoolEntryType registerEntry(ResourceLocation id, ILootSerializer<? extends LootEntry> serializer){
		return Registry.register(Registry.LOOT_POOL_ENTRY_TYPE, id, new LootPoolEntryType(serializer));
	}
}
