package twistedgate.immersiveposts.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twistedgate.immersiveposts.IPOMod;

public class IPORegistries{
	static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, IPOMod.ID);
	static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, IPOMod.ID);
	static final DeferredRegister<BlockEntityType<?>> TILE_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, IPOMod.ID);
	
	public static final void addRegistersToEventBus(IEventBus eventBus){
		BLOCK_REGISTER.register(eventBus);
		ITEM_REGISTER.register(eventBus);
		TILE_REGISTER.register(eventBus);
	}
}
