package twistedgate.immersiveposts.common.items;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.ImmersivePosts;

public class IPOItemBase extends Item{
	public IPOItemBase(String name){
		setFull3D();
		setCreativeTab(ImmersivePosts.ipCreativeTab);
		setRegistryName(new ResourceLocation(IPOMod.ID, name));
		setTranslationKey(IPOMod.ID+"."+name);
		
		IPOStuff.ITEMS.add(this);
	}
}
