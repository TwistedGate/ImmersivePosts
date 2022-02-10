package twistedgate.immersiveposts.common.items;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import twistedgate.immersiveposts.ImmersivePosts;
import twistedgate.immersiveposts.common.IPOConfig;

/**
 * @author TwistedGate
 */
public class IPOItemBase extends Item{
	public IPOItemBase(){
		super(new Item.Properties().tab(ImmersivePosts.creativeTab));
	}
	
//	public IPOItemBase(String name){
//		super(new Item.Properties().group(ImmersivePosts.creativeTab));
//		setRegistryName(new ResourceLocation(IPOMod.ID, name));
//		
//		IPOContent.ITEMS.add(this);
//	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items){
		if(this.allowdedIn(group) && IPOConfig.MAIN.isEnabled(getRegistryName())){
			items.add(new ItemStack(this));
		}
	}
}
