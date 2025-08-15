package twistedgate.immersiveposts.common.blocks;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * @author TwistedGate
 */
public class MetalFenceBlock extends FenceBlock{
	static final BlockBehaviour.Properties DEFAULT_PROP = BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
			.strength(3.0F, 15.0F)
			.sound(SoundType.METAL)
			.requiresCorrectToolForDrops()
			.noOcclusion()
			.isViewBlocking((s, r, p) -> false);
	
	public MetalFenceBlock(){
		super(DEFAULT_PROP);
	}
}
