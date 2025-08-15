package twistedgate.immersiveposts.util.loot;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import twistedgate.immersiveposts.IPOMod;

@Mod.EventBusSubscriber(modid = IPOMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IPOLootFunctions{
	private static final DeferredRegister<LootPoolEntryType> REGISTER = DeferredRegister.create(
			BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.key(), IPOMod.ID
	);
	
	public static RegistryObject<LootPoolEntryType> baseCoverDrop = registerEntry("base_cover_drop", new BaseCoverDropLootEntry.Serializer());
	public static RegistryObject<LootPoolEntryType> postDrop = registerEntry("post_material_drop", new PostMaterialDropLootEntry.Serializer());
	
	private static RegistryObject<LootPoolEntryType> registerEntry(String id, Serializer<? extends LootPoolEntryContainer> serializer){
		return REGISTER.register(id, () -> new LootPoolEntryType(serializer));
	}
	
	public static void modConstruction(IEventBus bus){
		REGISTER.register(bus);
	}
}
