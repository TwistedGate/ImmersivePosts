package twistedgate.immersiveposts.util.loot;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import twistedgate.immersiveposts.IPOMod;

@EventBusSubscriber(modid = IPOMod.ID, bus = EventBusSubscriber.Bus.MOD)
public class IPOLootFunctions{
	public static LootPoolEntryType baseCoverDrop;
	public static LootPoolEntryType postDrop;
	
	@SubscribeEvent
	// Just need to do this during *some* registry event, registries are frozen outside those
	public static void onRegister(RegisterEvent ev){
		if(ev.getForgeRegistry() != null && ev.getForgeRegistry().equals(ForgeRegistries.BLOCKS)){
			baseCoverDrop = registerEntry(BaseCoverDropLootEntry.ID, new BaseCoverDropLootEntry.Serializer());
			postDrop = registerEntry(PostMaterialDropLootEntry.ID, new PostMaterialDropLootEntry.Serializer());
		}
	}
	
	private static LootPoolEntryType registerEntry(ResourceLocation id, Serializer<? extends LootPoolEntryContainer> serializer){
		return Registry.register(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE, id, new LootPoolEntryType(serializer));
	}
}
