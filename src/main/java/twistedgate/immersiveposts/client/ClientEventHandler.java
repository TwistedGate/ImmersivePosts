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
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.client.model.PostBaseModel;
import twistedgate.immersiveposts.common.IPOContent;

import javax.annotation.Nonnull;

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
		event.register(PostBaseModel.Loader.LOCATION.getPath(), new PostBaseModel.Loader());
	}
	
	@SubscribeEvent
	public static void colorReg(RegisterColorHandlersEvent.Block event){
		event.register(new ColorHandler(), IPOContent.Blocks.POST_BASE.get());
	}
}
