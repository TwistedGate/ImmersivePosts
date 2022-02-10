package twistedgate.immersiveposts.common.blocks;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.IPOConfig;

/**
 * @author TwistedGate
 */
public class MetalFenceBlock extends FenceBlock{
	static final BlockBehaviour.Properties DEFAULT_PROP = BlockBehaviour.Properties.of(Material.METAL)
			.strength(3.0F, 15.0F)
			.sound(SoundType.METAL)
			.requiresCorrectToolForDrops()
			//.harvestTool(ToolType.PICKAXE)
			.noOcclusion()
			.isViewBlocking((s, r, p) -> false);
	
	public MetalFenceBlock(String materialName){
		super(DEFAULT_PROP);
		setRegistryName(new ResourceLocation(IPOMod.ID, "fence_" + materialName));
	}
	
	public MetalFenceBlock(){
		super(DEFAULT_PROP);
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items){
		if(IPOConfig.MAIN.isEnabled(getRegistryName())){
			items.add(new ItemStack(this));
		}
	}
}
