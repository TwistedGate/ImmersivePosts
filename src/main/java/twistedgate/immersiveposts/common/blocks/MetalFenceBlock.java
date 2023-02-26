package twistedgate.immersiveposts.common.blocks;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import twistedgate.immersiveposts.common.IPOConfig;

/**
 * @author TwistedGate
 */
public class MetalFenceBlock extends FenceBlock{
	static final BlockBehaviour.Properties DEFAULT_PROP = BlockBehaviour.Properties.of(Material.METAL)
			.strength(3.0F, 15.0F)
			.sound(SoundType.METAL)
			.requiresCorrectToolForDrops()
			.noOcclusion()
			.isViewBlocking((s, r, p) -> false);
	
	public MetalFenceBlock(){
		super(DEFAULT_PROP);
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items){
		if(IPOConfig.MAIN.isEnabled(this)){
			items.add(new ItemStack(this));
		}
	}
}
