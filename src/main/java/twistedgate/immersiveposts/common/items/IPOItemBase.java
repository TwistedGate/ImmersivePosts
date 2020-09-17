package twistedgate.immersiveposts.common.items;

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
public class IPOItemBase extends Item{
	public IPOItemBase(String name){
		super(new Item.Properties().group(ImmersivePosts.creativeTab));
		setRegistryName(new ResourceLocation(IPOMod.ID, name));
		
		IPOContent.ITEMS.add(this);
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items){
		if(this.isInGroup(group) && IPOConfig.MAIN.isEnabled(getRegistryName())){
			items.add(new ItemStack(this));
		}
	}
}
