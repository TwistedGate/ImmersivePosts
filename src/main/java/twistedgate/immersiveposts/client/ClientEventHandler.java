package twistedgate.immersiveposts.client;

import java.util.function.Predicate;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.client.model.PostBaseModel;
import twistedgate.immersiveposts.common.IPOContent;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

@EventBusSubscriber(modid = IPOMod.ID, value = {Dist.CLIENT}, bus = Bus.MOD)
public class ClientEventHandler implements ISelectiveResourceReloadListener{
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate){
		if(resourcePredicate.test(VanillaResourceType.TEXTURES)){
			PostBaseModel.CACHE.invalidateAll();
		}
	}
	
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event){
		RenderTypeLookup.setRenderLayer(IPOContent.Blocks.POST_BASE.get(), RenderType.getCutout());
		
		Predicate<RenderType> type = t -> {
			return t == RenderType.getSolid() || t == RenderType.getCutout();
		};
		for(EnumPostMaterial mat:EnumPostMaterial.values()){
			RenderTypeLookup.setRenderLayer(IPOContent.Blocks.Posts.get(mat), type);
			RenderTypeLookup.setRenderLayer(IPOContent.Blocks.HorizontalTruss.get(mat), type);
		}
	}
}
