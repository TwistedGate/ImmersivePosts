package twistedgate.immersiveposts.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import twistedgate.immersiveposts.util.ResourceUtils;

public class ExternalModContent{
	public static DeferredHolder<Block, Block> IE_TREATED_FENCE		= DeferredHolder.create(BuiltInRegistries.BLOCK.key(), ResourceUtils.ie("treated_fence"));
	public static DeferredHolder<Block, Block> IE_ALUMINIUM_FENCE	= DeferredHolder.create(BuiltInRegistries.BLOCK.key(), ResourceUtils.ie("alu_fence"));
	public static DeferredHolder<Block, Block> IE_STEEL_FENCE		= DeferredHolder.create(BuiltInRegistries.BLOCK.key(), ResourceUtils.ie("steel_fence"));
	
	public static void forceClassLoad(){
	}
}
