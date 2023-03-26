package twistedgate.immersiveposts.client.model;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.client.model.PostBaseLoader.PostBaseModelRaw;

public class PostBaseLoader implements IGeometryLoader<PostBaseModelRaw>{
	public static final ResourceLocation LOCATION = new ResourceLocation(IPOMod.ID, "postbase");
	
	@Override
	public PostBaseModelRaw read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException{
		return new PostBaseModelRaw();
	}
	
	public static class PostBaseModelRaw implements IUnbakedGeometry<PostBaseModelRaw>{
		@Override
		public BakedModel bake(IGeometryBakingContext context, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation){
			return new PostBaseModel();
		}
		
		@Override
		public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors){
			return ImmutableList.of();
		}
	}
	
}
