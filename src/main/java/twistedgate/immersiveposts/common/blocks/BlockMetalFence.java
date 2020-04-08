package twistedgate.immersiveposts.common.blocks;

import net.minecraft.block.FenceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.ImmersivePosts;

/**
 * @author TwistedGate
 */
public class BlockMetalFence extends FenceBlock{
	public BlockMetalFence(String name){
		super(Properties.create(Material.IRON).hardnessAndResistance(3.0F, 15.0F));
		setRegistryName(new ResourceLocation(IPOMod.ID, name));
		
		IPOStuff.BLOCKS.add(this);
		IPOStuff.ITEMS.add(new BlockItem(this, new Item.Properties().group(ImmersivePosts.creativeTab)).setRegistryName(this.getRegistryName()));
	}
}
