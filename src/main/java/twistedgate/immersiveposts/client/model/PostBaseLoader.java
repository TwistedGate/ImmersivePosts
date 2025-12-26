package twistedgate.immersiveposts.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import twistedgate.immersiveposts.client.model.PostBaseLoader.PostBaseModelRaw;
import twistedgate.immersiveposts.util.ResourceUtils;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class PostBaseLoader implements IGeometryLoader<PostBaseModelRaw>{
	public static final ResourceLocation LOCATION = ResourceUtils.ipo("postbase");
	
	@Nonnull
	@Override
	public PostBaseModelRaw read(@Nonnull JsonObject jsonObject, @Nonnull JsonDeserializationContext deserializationContext) throws JsonParseException{
		return new PostBaseModelRaw();
	}
	
	public static class PostBaseModelRaw implements IUnbakedGeometry<PostBaseModelRaw>{
		@Nonnull
		@Override
		public BakedModel bake(@Nonnull IGeometryBakingContext iGeometryBakingContext, @Nonnull ModelBaker modelBaker, @Nonnull Function<Material, TextureAtlasSprite> function, @Nonnull ModelState modelState, @Nonnull ItemOverrides itemOverrides){
			return new PostBaseModel();
		}
	}
	
}
