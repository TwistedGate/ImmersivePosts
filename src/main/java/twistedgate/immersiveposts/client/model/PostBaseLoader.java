package twistedgate.immersiveposts.client.model;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.client.model.PostBaseLoader.PostBaseModelRaw;

public class PostBaseLoader implements IModelLoader<PostBaseModelRaw>{
	
	public static final ResourceLocation LOCATION=new ResourceLocation(IPOMod.ID, "postbase");
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager){
	}
	
	@Override
	public PostBaseModelRaw read(JsonDeserializationContext deserializationContext, JsonObject modelContents){
		return new PostBaseModelRaw();
	}
	
	public static class PostBaseModelRaw implements IModelGeometry<PostBaseModelRaw>{
		@Override
		public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation){
			return new PostBaseModel();
		}
		
		@Override
		public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors){
			return ImmutableList.of();
		}
	}
}
