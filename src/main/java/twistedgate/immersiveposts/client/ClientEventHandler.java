package twistedgate.immersiveposts.client;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import twistedgate.immersiveposts.api.IPOMod;
import twistedgate.immersiveposts.client.model.PostBaseModel;
import twistedgate.immersiveposts.common.IPOContent;

import javax.annotation.Nonnull;

import static net.neoforged.fml.common.EventBusSubscriber.Bus;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = IPOMod.ID, value = {Dist.CLIENT}, bus = Bus.MOD)
public class ClientEventHandler extends SimplePreparableReloadListener<Unit>{
	
	@Override
	@Nonnull
	protected Unit prepare(@Nonnull ResourceManager pResourceManager, @Nonnull ProfilerFiller pProfiler){
		return Unit.INSTANCE;
	}
	
	@Override
	protected void apply(@Nonnull Unit pObject, @Nonnull ResourceManager pResourceManager, @Nonnull ProfilerFiller pProfiler){
		PostBaseModel.CACHE.invalidateAll();
	}
	
	@SubscribeEvent
	public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event){
		event.register(PostBaseModel.Loader.LOCATION, new PostBaseModel.Loader());
	}
	
	@SubscribeEvent
	public static void colorReg(RegisterColorHandlersEvent.Block event){
		event.register(new ColorHandler(), IPOContent.Blocks.POST_BASE.get());
	}
}
