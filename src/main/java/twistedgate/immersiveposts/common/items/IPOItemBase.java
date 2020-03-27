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
		//setCreativeTab(ImmersivePosts.creativeTab);
		setRegistryName(new ResourceLocation(IPOMod.ID, name));
		//setTranslationKey(IPOMod.ID+"."+name);
		//setFull3D();
		
		IPOStuff.ITEMS.add(this);
	}
}
