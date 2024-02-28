package twistedgate.immersiveposts.client.model;

import blusunrize.immersiveengineering.client.utils.ModelUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.NotNull;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.client.model.PostBaseModel.Loader.PostBaseModelRaw;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class PostBaseModel extends IPOBakedModel{
	
	public static final Cache<Key, SpecialPostBaseModel> CACHE = CacheBuilder.newBuilder()
			.expireAfterAccess(2, TimeUnit.MINUTES)
			.maximumSize(100)
			.build();
	
	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, @Nullable RenderType layer){
		BlockState hState = Blocks.DIRT.defaultBlockState();
		Direction facing = Direction.NORTH;
		
		if(extraData.has(IPOModelData.POSTBASE)){
			IPOModelData.PostBaseModelData data = extraData.get(IPOModelData.POSTBASE);
			if(data != null) {
				hState = data.state;
				facing = data.facing;
			}
		}
		
		Key key = new Key(hState, facing);
		SpecialPostBaseModel special = CACHE.getIfPresent(key);
		if(special == null){
			special = new SpecialPostBaseModel(key);
			CACHE.put(key, special);
		}
		
		return special.getQuads(state, side, rand);
	}
	
	@Override
	public @NotNull ModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData tileData){
		ModelData.Builder mData = super.getModelData(world, pos, state, tileData).derive();
		
		if(world.getBlockEntity(pos) instanceof PostBaseTileEntity base){
			IPOModelData.PostBaseModelData data = new IPOModelData.PostBaseModelData(base);
			mData.with(IPOModelData.POSTBASE, data);
		}
		
		return mData.build();
	}
	
	private static final ChunkRenderTypeSet RENDER_TYPE = ChunkRenderTypeSet.of(RenderType.cutout());
	@Override
	public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data){
		return RENDER_TYPE;
	}
	
	@Override
	public boolean useAmbientOcclusion(){
		return true;
	}
	
	@Override
	public boolean isGui3d(){
		return true;
	}
	
	@Override
	public boolean isCustomRenderer(){
		return false;
	}
	
	static TextureAtlasSprite postbaseSprite;
	
	static TextureAtlasSprite getPostbaseSprite(){
		if(postbaseSprite == null){
			postbaseSprite = Minecraft.getInstance()
				.getModelManager()
				.getAtlas(InventoryMenu.BLOCK_ATLAS)
				.getSprite(new ResourceLocation(IPOMod.ID, "block/postbase"));
		}
		
		return postbaseSprite;
	}
	
	@Override
	public @NotNull TextureAtlasSprite getParticleIcon(){
		return getPostbaseSprite();
	}
	
	@Override
	public @NotNull ItemOverrides getOverrides(){
		return ItemOverrides.EMPTY;
	}
	
	// ########################################################################################################
	
	public static class Loader implements IGeometryLoader<PostBaseModelRaw>{
		public static final ResourceLocation LOCATION = new ResourceLocation(IPOMod.ID, "postbase");
		
		@Override
		public PostBaseModelRaw read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException{
			return new PostBaseModelRaw();
		}
		
		public static class PostBaseModelRaw implements IUnbakedGeometry<PostBaseModelRaw>{

			@Override
			public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
				return new PostBaseModel();
			}
		}
		
	}
	
	private static class SpecialPostBaseModel extends PostBaseModel{
		private static final RandomSource RANDOM = RandomSource.create();
		
		private static final Vec3[] vertices = new Vec3[]{
				new Vec3(0.25F, 1.001F, 0.25F), new Vec3(0.25F, 1.001F, 0.75F),
				new Vec3(0.75F, 1.001F, 0.75F), new Vec3(0.75F, 1.001F, 0.25F),
		};
		
		private static final double[] uvs = new double[]{
				8,0,
				16,8
		};
		
		private static final float[] color = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
		
		List<List<BakedQuad>> quads = new ArrayList<>(6);
		public SpecialPostBaseModel(Key key){
			build(key);
		}
		
		private void build(Key key){
			BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(key.state);
			for(Direction side:Direction.values()){

				List<BakedQuad> quads = new ArrayList<>(model.getQuads(key.state, side, RANDOM, ModelData.EMPTY, null));
				
				if(side == Direction.UP){
					TextureAtlasSprite sprite = getPostbaseSprite();
					quads.add(ModelUtils.createBakedQuad(vertices, side, sprite, uvs, color, false));
				}
				
				this.quads.add(quads);
			}
			this.quads.add(ImmutableList.of());
		}
		
		@Override
		public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nullable RandomSource rand, @Nullable ModelData extraData, RenderType layer){
			return this.quads.get(side == null ? (this.quads.size() - 1) : side.get3DDataValue());
		}
	}

	private record Key(BlockState state, Direction facing) {

		@Override
			public boolean equals(Object obj) {
				if (this == obj) {
					return true;
				} else if (obj instanceof Key other) {
					return this.state.equals(other.state) && this.facing == other.facing;
				}

			return false;
			}

	}
}
