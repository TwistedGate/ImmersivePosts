package twistedgate.immersiveposts.util.loot;

import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import twistedgate.immersiveposts.IPOMod;

@EventBusSubscriber(modid = IPOMod.ID, bus = EventBusSubscriber.Bus.MOD)
public class IPOLootFunctions{
	private static final DeferredRegister<LootPoolEntryType> ENTRY_REGISTER = DeferredRegister.create(Registries.LOOT_POOL_ENTRY_TYPE, IPOMod.ID);
	
	public static final RegistryObject<LootPoolEntryType> POSTBASE_COVERDROP = registerEntry(BaseCoverDropLootEntry.ID, BaseCoverDropLootEntry.Serializer::new);
	public static final RegistryObject<LootPoolEntryType> POST_DROP = registerEntry(PostMaterialDropLootEntry.ID, PostMaterialDropLootEntry.Serializer::new);
	
	private static RegistryObject<LootPoolEntryType> registerEntry(String id, Supplier<Serializer<? extends LootPoolEntryContainer>> serializer){
		return ENTRY_REGISTER.register(id, () -> new LootPoolEntryType(serializer.get()));
	}
	
	public static final void addRegistersToEventBus(IEventBus eventBus){
		ENTRY_REGISTER.register(eventBus);
	}
}
