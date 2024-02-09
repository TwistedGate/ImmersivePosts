package twistedgate.immersiveposts.client;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.client.model.PostBaseModel;
import twistedgate.immersiveposts.common.IPOContent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = IPOMod.ID, value = {Dist.CLIENT}, bus = Bus.MOD)
public class ClientEventHandler extends SimplePreparableReloadListener<Unit>{
	
	@Override
	protected @NotNull Unit prepare(@NotNull ResourceManager manager, @NotNull ProfilerFiller profiler){
		return Unit.INSTANCE;
	}
	
	@Override
	protected void apply(@NotNull Unit pObject, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler){
		PostBaseModel.CACHE.invalidateAll();
	}
	
	@SubscribeEvent
	public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event){
		event.register(PostBaseModel.Loader.LOCATION.getPath(), new PostBaseModel.Loader());
	}
	
	@SubscribeEvent
	public static void colorReg(RegisterColorHandlersEvent.Block event){
		event.register(new ColorHandler(), IPOContent.Blocks.POST_BASE.get());
	}
}
