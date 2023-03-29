package twistedgate.immersiveposts.common;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.RegistryObject;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.items.IPOBlockItem;
import twistedgate.immersiveposts.common.items.IPOItem;

@EventBusSubscriber(modid = IPOMod.ID, bus = Bus.MOD)
public class CreativeTab{
	private static CreativeModeTab TAB;
	
	@SubscribeEvent
	public static void registerTab(CreativeModeTabEvent.Register ev){
		ResourceLocation rl = new ResourceLocation(IPOMod.ID, "main");
		TAB = ev.registerCreativeModeTab(rl, t -> t.icon(() -> new ItemStack(IPOContent.Blocks.POST_BASE.get())).title(Component.literal(IPOMod.NAME)));
	}
	
	@SubscribeEvent
	public static void fillTab(CreativeModeTabEvent.BuildContents ev){
		if(ev.getTab() != TAB){
			return;
		}
		
		for(RegistryObject<Item> regObj:IPORegistries.ITEM_REGISTER.getEntries()){
			final Item item = regObj.get();
			if(item instanceof IPOItem ipoItem){
				ipoItem.fillCreativeTab(ev);
			}else if(item instanceof IPOBlockItem ipoBlockItem){
				ipoBlockItem.fillCreativeTab(ev);
			}else{
				ev.accept(regObj);
			}
		}
	}
}
