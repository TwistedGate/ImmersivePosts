package twistedgate.immersiveposts.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import twistedgate.immersiveposts.IPOMod;

public class IPOModTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IPOMod.ID);

    static {
        CREATIVE_MODE_TAB.register(
                "tab",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup.immersiveposts"))
                        .icon(() -> new ItemStack(IPOContent.Blocks.POST_BASE.get()))
                        .displayItems((pars, event) -> {
                            event.accept(IPOContent.Blocks.POST_BASE.get());
                            IPOContent.Blocks.Fences.ALL_FENCES.forEach(fence -> event.accept(fence.get()));

                            event.accept(IPOContent.Items.ROD_GOLD.get());
                            event.accept(IPOContent.Items.ROD_COPPER.get());
                            event.accept(IPOContent.Items.ROD_LEAD.get());
                            event.accept(IPOContent.Items.ROD_SILVER.get());
                            event.accept(IPOContent.Items.ROD_NICKEL.get());
                            event.accept(IPOContent.Items.ROD_CONSTANTAN.get());
                            event.accept(IPOContent.Items.ROD_ELECTRUM.get());
                            event.accept(IPOContent.Items.ROD_URANIUM.get());
                        })
                        .build()
        );
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
