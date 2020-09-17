package twistedgate.immersiveposts.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.IPOConfig;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOContent;
import twistedgate.immersiveposts.ImmersivePosts;

/**
 * @author TwistedGate
 */
public class BlockMetalFence extends FenceBlock{
	static final Block.Properties DEFAULT_PROP=Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 15.0F);
	
	public BlockMetalFence(String materialName){
		super(DEFAULT_PROP);
		setRegistryName(new ResourceLocation(IPOMod.ID, "fence_"+materialName));
		
		IPOContent.BLOCKS.add(this);
		IPOContent.ITEMS.add(new BlockItem(this, new Item.Properties().group(ImmersivePosts.creativeTab)).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items){
		if(IPOConfig.MAIN.isEnabled(getRegistryName()))
			items.add(new ItemStack(this));
	}
}
