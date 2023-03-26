package twistedgate.immersiveposts.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;

import blusunrize.immersiveengineering.client.utils.ModelUtils;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;

public class PostBaseModel extends IPOBakedModel{
	
	public static final Cache<Key, SpecialPostBaseModel> CACHE = CacheBuilder.newBuilder()
			.expireAfterAccess(2, TimeUnit.MINUTES)
			.maximumSize(100)
			.build();

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, @Nullable RenderType layer){
		BlockState hState = Blocks.DIRT.defaultBlockState();
		Int2IntFunction colorMul = i -> 0xffffffff;
		Direction facing = Direction.NORTH;
		
		if(extraData.has(IPOModelData.POSTBASE)){
			IPOModelData.PostBaseModelData data = extraData.get(IPOModelData.POSTBASE);
			hState = data.state;
			colorMul = data.color;
			facing = data.facing;
		}
		
		Key key = new Key(hState, colorMul, facing);
		SpecialPostBaseModel special = CACHE.getIfPresent(key);
		if(special == null){
			special = new SpecialPostBaseModel(key, colorMul);
			CACHE.put(key, special);
		}
		
		return special.getQuads(state, side, rand);
	}
	
	@Override
	public ModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData tileData){
		ModelData.Builder mData = super.getModelData(world, pos, state, tileData).derive();
		
		//mData.with(null, tileData); // TODO Is this needed still?
		
		BlockEntity te = world.getBlockEntity(pos);
		if(te instanceof PostBaseTileEntity base){
			IPOModelData.PostBaseModelData data = new IPOModelData.PostBaseModelData(base.getCoverState(), base.getFacing(), i -> i);
			mData.with(IPOModelData.POSTBASE, data);
		}
		
		return mData.build();
	}
	
	private static final ChunkRenderTypeSet RENDER_TYPE = ChunkRenderTypeSet.of(RenderType.cutout());
	@Override
	public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data){
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
	public TextureAtlasSprite getParticleIcon(){
		return getPostbaseSprite();
	}
	
	@Override
	public ItemOverrides getOverrides(){
		return ItemOverrides.EMPTY;
	}
	
	private static class SpecialPostBaseModel extends PostBaseModel{
		private static final RandomSource RANDOM = RandomSource.create();
		
		private static final Vec3[] verts = new Vec3[]{
				new Vec3(0.25F, 1.001F, 0.25F), new Vec3(0.25F, 1.001F, 0.75F),
				new Vec3(0.75F, 1.001F, 0.75F), new Vec3(0.75F, 1.001F, 0.25F),
		};
		
		private static final double[] uvs = new double[]{
				8,0,
				16,8
		};
		
		private static final float[] color = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
		
		List<List<BakedQuad>> quads = new ArrayList<>(6);
		public SpecialPostBaseModel(Key key, Int2IntFunction colorMul){
			build(key, colorMul);
		}
		
		private void build(Key key, Int2IntFunction colorMulBasic){
			if(colorMulBasic == null){
				ItemColors colors = Minecraft.getInstance().getItemColors();
				ItemStack stack = new ItemStack(key.state.getBlock());
				colorMulBasic = (i) -> colors.getColor(stack, i);
			}
			
			key.usedColorMultipliers = new Int2IntOpenHashMap();
			final Int2IntFunction f = colorMulBasic;
			Int2IntFunction colorMul = (i) -> {
				int v = f.get(i);
				key.usedColorMultipliers.put(i, v);
				return v;
			};
			
			// FIXME ! This is only temporary!!
			Function<BakedQuad, BakedQuad> tintTransformer = t -> t;
//			Function<BakedQuad, BakedQuad> tintTransformer = new QuadTransformer(Transformation.identity(), colorMul);
			
			BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(key.state);
			
			for(Direction side:Direction.values()){
				List<BakedQuad> quads = model.getQuads(key.state, side, RANDOM, ModelData.EMPTY, (RenderType) null).stream()
						.map(tintTransformer)
						.collect(Collectors.toCollection(ArrayList::new));
				
				if(side == Direction.UP){
					TextureAtlasSprite sprite = getPostbaseSprite();
					quads.add(ModelUtils.createBakedQuad(verts, side, sprite, uvs, color, false));
				}
				
				this.quads.add(quads);
			}
			this.quads.add(ImmutableList.of());
		}
		
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nullable RandomSource rand, @Nullable ModelData extraData, RenderType layer){
			return this.quads.get(side == null ? (this.quads.size() - 1) : side.get3DDataValue());
		}
	}
	
	private static class Key{
		final BlockState state;
		final Direction facing;
		@Nullable
		Int2IntMap usedColorMultipliers;
		@Nullable
		final Int2IntFunction allColorMultipliers;
		
		public Key(BlockState state, Int2IntFunction colorMul, Direction facing){
			this.state = state;
			this.facing = facing;
			this.allColorMultipliers = colorMul;
			this.usedColorMultipliers = null;
		}
		
		@Override
		public boolean equals(Object obj){
			if(this == obj){
				return true;
			}else if(obj != null && obj instanceof Key other){
				return this.state.equals(other.state) && this.facing == other.facing && sameColorMultipliersAs(other);
			}
			
			return false;
		}
		
		private boolean sameColorMultipliersAs(Key that){
			if(that.usedColorMultipliers != null && this.usedColorMultipliers != null)
				return this.usedColorMultipliers.equals(that.usedColorMultipliers);
			else if(that.usedColorMultipliers != null && this.allColorMultipliers != null){
				for(int i:that.usedColorMultipliers.keySet()){
					if(this.allColorMultipliers.get(i) != that.usedColorMultipliers.get(i)){
						return false;
					}
				}
				return true;
			}else if(that.allColorMultipliers != null && this.usedColorMultipliers != null){
				return that.sameColorMultipliersAs(this);
			}else{
				throw new IllegalStateException("Can't compare PostBaseModel. Key's that use functions!");
			}
		}
		
		@Override
		public int hashCode(){
			return 31 * this.state.hashCode() + Objects.hash(this.facing);
		}
	}
}
