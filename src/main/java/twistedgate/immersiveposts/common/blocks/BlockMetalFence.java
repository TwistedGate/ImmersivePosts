package twistedgate.immersiveposts.common.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import twistedgate.immersiveposts.IPOConfig;
import twistedgate.immersiveposts.ImmersivePosts;
import twistedgate.immersiveposts.common.IPOContent;
import twistedgate.immersiveposts.common.IPOMod;

/**
 * @author TwistedGate
 */
public class BlockMetalFence extends FenceBlock{
	static final AbstractBlock.Properties DEFAULT_PROP=AbstractBlock.Properties.create(Material.IRON)
			.hardnessAndResistance(3.0F, 15.0F)
			.sound(SoundType.METAL)
			.setRequiresTool()
			.harvestTool(ToolType.PICKAXE)
			.notSolid()
			.setBlocksVision((s,r,p)->false);
	
	public BlockMetalFence(String materialName){
		super(DEFAULT_PROP);
		setRegistryName(new ResourceLocation(IPOMod.ID, "fence_"+materialName));
		
		IPOContent.BLOCKS.add(this);
		IPOContent.ITEMS.add(new BlockItem(this, new Item.Properties().group(ImmersivePosts.creativeTab)).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items){
		if(IPOConfig.MAIN.isEnabled(getRegistryName())){
			items.add(new ItemStack(this));
		}
	}
}
