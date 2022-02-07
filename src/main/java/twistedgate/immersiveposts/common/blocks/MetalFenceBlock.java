package twistedgate.immersiveposts.common.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.IPOConfig;

/**
 * @author TwistedGate
 */
public class MetalFenceBlock extends FenceBlock{
	static final AbstractBlock.Properties DEFAULT_PROP = AbstractBlock.Properties.create(Material.IRON)
			.hardnessAndResistance(3.0F, 15.0F)
			.sound(SoundType.METAL)
			.setRequiresTool()
			.harvestTool(ToolType.PICKAXE)
			.notSolid()
			.setBlocksVision((s, r, p) -> false);
	
	public MetalFenceBlock(String materialName){
		super(DEFAULT_PROP);
		setRegistryName(new ResourceLocation(IPOMod.ID, "fence_" + materialName));
	}
	
	public MetalFenceBlock(){
		super(DEFAULT_PROP);
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items){
		if(IPOConfig.MAIN.isEnabled(getRegistryName())){
			items.add(new ItemStack(this));
		}
	}
}
