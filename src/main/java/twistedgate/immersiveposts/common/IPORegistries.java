package twistedgate.immersiveposts.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import twistedgate.immersiveposts.api.IPOMod;

public class IPORegistries{
	static final DeferredRegister<CreativeModeTab> CTAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IPOMod.ID);
	static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK, IPOMod.ID);
	static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(BuiltInRegistries.ITEM, IPOMod.ID);
	static final DeferredRegister<BlockEntityType<?>> TILE_REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, IPOMod.ID);
	
	public static void addRegistersToEventBus(IEventBus eventBus){
		CTAB_REGISTER.register(eventBus);
		BLOCK_REGISTER.register(eventBus);
		ITEM_REGISTER.register(eventBus);
		TILE_REGISTER.register(eventBus);
	}
}
