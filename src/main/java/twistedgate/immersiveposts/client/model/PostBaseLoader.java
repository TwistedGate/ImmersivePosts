package twistedgate.immersiveposts.client.model;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.client.model.PostBaseLoader.PostBaseModelRaw;

public class PostBaseLoader implements IModelLoader<PostBaseModelRaw>{
	
	public static final ResourceLocation LOCATION = new ResourceLocation(IPOMod.ID, "postbase");
	
	@Override
	public void onResourceManagerReload(ResourceManager resourceManager){
	}
	
	@Override
	public PostBaseModelRaw read(JsonDeserializationContext deserializationContext, JsonObject modelContents){
		return new PostBaseModelRaw();
	}
	
	public static class PostBaseModelRaw implements IModelGeometry<PostBaseModelRaw>{
		@Override
		public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation){
			return new PostBaseModel();
		}
		
		@Override
		public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors){
			return ImmutableList.of();
		}
	}
}
