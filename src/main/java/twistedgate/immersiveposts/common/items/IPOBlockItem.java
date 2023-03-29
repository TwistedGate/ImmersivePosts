package twistedgate.immersiveposts.common.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public abstract class IPOBlockItem extends BlockItem{
	public IPOBlockItem(Block block, Item.Properties props){
		super(block, props);
	}
	
	public void fillCreativeTab(Output out){
		out.accept(this);
	}
}
