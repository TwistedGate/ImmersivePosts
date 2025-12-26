package twistedgate.immersiveposts.util.loot;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import twistedgate.immersiveposts.api.IPOMod;

public class IPOLootFunctions{
	private static final DeferredRegister<LootPoolEntryType> REGISTER = DeferredRegister.create(
			BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.key(), IPOMod.ID
	);
	
	public static Holder<LootPoolEntryType> baseCoverDrop = registerEntry("base_cover_drop", BaseCoverDropLootEntry.CODEC);
	public static Holder<LootPoolEntryType> postDrop = registerEntry("post_material_drop", PostMaterialDropLootEntry.CODEC);
	
	private static Holder<LootPoolEntryType> registerEntry(String id, MapCodec<? extends LootPoolEntryContainer> serializer){
		return REGISTER.register(id, () -> new LootPoolEntryType(serializer));
	}
	
	public static void modConstruction(IEventBus bus){
		REGISTER.register(bus);
	}
}
