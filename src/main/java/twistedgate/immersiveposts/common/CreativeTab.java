package twistedgate.immersiveposts.common;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import twistedgate.immersiveposts.IPOMod;

@Mod.EventBusSubscriber(modid = IPOMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeTab{
	public static final RegistryObject<CreativeModeTab> TAB = makeTab();
	
	private static RegistryObject<CreativeModeTab> makeTab(){
		return IPORegistries.CTAB_REGISTER.register(IPOMod.ID, () -> {
			//@formatter:off
			return new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
					.icon(() -> new ItemStack(IPOContent.Blocks.POST_BASE.get()))
					.title(Component.translatable("itemGroup.immersiveposts"))
					.displayItems(CreativeTab::fill)
					.build();
			//@formatter:on
		});
	}
	
	private static void fill(CreativeModeTab.ItemDisplayParameters parms, CreativeModeTab.Output out){
		for(RegistryObject<Item> holder:IPORegistries.ITEM_REGISTER.getEntries()){
			out.accept(holder.get());
		}
	}
}
