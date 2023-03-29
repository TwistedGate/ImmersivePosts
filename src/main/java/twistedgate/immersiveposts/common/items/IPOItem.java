package twistedgate.immersiveposts.common.items;

import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.Item;

/**
 * @author TwistedGate
 */
public class IPOItem extends Item{
	public IPOItem(){
		super(new Item.Properties());
	}
	
	public void fillCreativeTab(Output out){
		out.accept(this);
	}
}
