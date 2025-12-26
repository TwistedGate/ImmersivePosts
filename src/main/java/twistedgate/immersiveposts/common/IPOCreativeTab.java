package twistedgate.immersiveposts.common;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import twistedgate.immersiveposts.api.IPOMod;

public class IPOCreativeTab{
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = makeTab();
	
	private static DeferredHolder<CreativeModeTab, CreativeModeTab> makeTab(){
		return IPORegistries.CTAB_REGISTER.register(IPOMod.ID, () -> {
			//@formatter:off
			return new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
					.icon(() -> new ItemStack(IPOContent.Blocks.POST_BASE.get()))
					.title(Component.translatable("itemGroup.immersiveposts"))
					.displayItems(IPOCreativeTab::fill)
					.build();
			//@formatter:on
		});
	}
	
	private static void fill(CreativeModeTab.ItemDisplayParameters parms, CreativeModeTab.Output out){
		for(DeferredHolder<Item, ? extends Item> holder:IPORegistries.ITEM_REGISTER.getEntries()){
			out.accept(holder.get());
		}
	}
	
	public static void forceClassLoad(){
	}
}
