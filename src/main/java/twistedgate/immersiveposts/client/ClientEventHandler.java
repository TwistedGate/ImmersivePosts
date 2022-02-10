package twistedgate.immersiveposts.client;

import java.util.function.Predicate;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.client.model.PostBaseModel;
import twistedgate.immersiveposts.common.IPOContent;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

@EventBusSubscriber(modid = IPOMod.ID, value = {Dist.CLIENT}, bus = Bus.MOD)
public class ClientEventHandler extends SimplePreparableReloadListener<Unit>{
	
	@Override
	protected Unit prepare(ResourceManager pResourceManager, ProfilerFiller pProfiler){
		return Unit.INSTANCE;
	}
	
	@Override
	protected void apply(Unit pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler){
		PostBaseModel.CACHE.invalidateAll();
	}
	
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event){
		ItemBlockRenderTypes.setRenderLayer(IPOContent.Blocks.POST_BASE.get(), RenderType.cutout());
		
		Predicate<RenderType> type = t -> {
			return t == RenderType.solid() || t == RenderType.cutout();
		};
		for(EnumPostMaterial mat:EnumPostMaterial.values()){
			ItemBlockRenderTypes.setRenderLayer(IPOContent.Blocks.Posts.get(mat), type);
			ItemBlockRenderTypes.setRenderLayer(IPOContent.Blocks.HorizontalTruss.get(mat), type);
		}
	}
}
