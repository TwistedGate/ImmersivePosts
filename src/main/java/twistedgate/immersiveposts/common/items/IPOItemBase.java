package twistedgate.immersiveposts.common.items;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.ImmersivePosts;

/**
 * @author TwistedGate
 */
public class IPOItemBase extends Item{
	public IPOItemBase(String name){
		super(new Item.Properties().group(ImmersivePosts.creativeTab));
		setRegistryName(new ResourceLocation(IPOMod.ID, name));
		
		IPOStuff.ITEMS.add(this);
	}
}
