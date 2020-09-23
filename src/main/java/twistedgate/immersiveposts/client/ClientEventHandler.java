package twistedgate.immersiveposts.client;

import java.util.function.Predicate;

import net.minecraft.resources.IResourceManager;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;
import twistedgate.immersiveposts.client.model.PostBaseModel;

public class ClientEventHandler implements ISelectiveResourceReloadListener{
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate){
		if(resourcePredicate.test(VanillaResourceType.TEXTURES)){
			PostBaseModel.CACHE.invalidateAll();
		}
	}
}
