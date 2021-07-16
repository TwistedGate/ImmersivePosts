package twistedgate.immersiveposts.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;

import blusunrize.immersiveengineering.api.utils.QuadTransformer;
import blusunrize.immersiveengineering.api.utils.client.CombinedModelData;
import blusunrize.immersiveengineering.api.utils.client.SinglePropertyModelData;
import blusunrize.immersiveengineering.client.utils.ModelUtils;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;

public class PostBaseModel extends IPOBakedModel{
	
	public static final Cache<Key, SpecialPostBaseModel> CACHE=CacheBuilder.newBuilder()
			.expireAfterAccess(2, TimeUnit.MINUTES)
			.maximumSize(100)
			.build();
	
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData){
		BlockState hState=Blocks.DIRT.getDefaultState();
		Int2IntFunction colorMul=i->0xffffffff;
		Direction facing=Direction.NORTH;
		
		if(extraData.hasProperty(IPOModelData.POSTBASE)){
			IPOModelData.PostBaseModelData data=extraData.getData(IPOModelData.POSTBASE);
			hState=data.state;
			colorMul=data.color;
			facing=data.facing;
		}
		
		Key key=new Key(hState, colorMul, facing);
		SpecialPostBaseModel special=CACHE.getIfPresent(key);
		if(special==null){
			special=new SpecialPostBaseModel(key, colorMul);
			CACHE.put(key, special);
		}
		
		return special.getQuads(state, side, rand);
	}
	
	@Override
	public IModelData getModelData(IBlockDisplayReader world, BlockPos pos, BlockState state, IModelData tileData){
		List<IModelData> list=new ArrayList<>();
		list.add(tileData);
		
		TileEntity te=world.getTileEntity(pos);
		if(te instanceof PostBaseTileEntity){
			PostBaseTileEntity base=(PostBaseTileEntity)te;
			
			IPOModelData.PostBaseModelData data=new IPOModelData.PostBaseModelData(base.getCoverState(), base.getFacing(), i->i);
			
			list.add(new SinglePropertyModelData<>(data, IPOModelData.POSTBASE));
		}
		
		return CombinedModelData.combine(list.toArray(new IModelData[0]));
	}
	
	@Override
	public boolean isAmbientOcclusion(){
		return true;
	}
	
	@Override
	public boolean isGui3d(){
		return true;
	}
	
	@Override
	public boolean isBuiltInRenderer(){
		return false;
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture(){
		return ModelLoader.White.instance();
	}
	
	@Override
	public ItemOverrideList getOverrides(){
		return ItemOverrideList.EMPTY;
	}
	
	private static class SpecialPostBaseModel extends PostBaseModel{
		private static final Random RANDOM=new Random();
		
		private static final Vector3d[] verts=new Vector3d[]{
				new Vector3d(0.25F, 1.001F, 0.25F), new Vector3d(0.25F, 1.001F, 0.75F),
				new Vector3d(0.75F, 1.001F, 0.75F), new Vector3d(0.75F, 1.001F, 0.25F),
		};
		
		private static final double[] uvs=new double[]{
				8,0,
				16,8
		};
		
		private static final float[] color=new float[]{1.0F, 1.0F, 1.0F, 1.0F};
		
		List<List<BakedQuad>> quads=new ArrayList<>(6);
		public SpecialPostBaseModel(Key key, Int2IntFunction colorMul){
			build(key, colorMul);
		}
		
		private void build(Key key, Int2IntFunction colorMulBasic){
			if(colorMulBasic==null){
				ItemColors colors=Minecraft.getInstance().getItemColors();
				ItemStack stack=new ItemStack(key.state.getBlock());
				colorMulBasic=(i)->colors.getColor(stack, i);
			}
			
			key.usedColorMultipliers=new Int2IntOpenHashMap();
			final Int2IntFunction f=colorMulBasic;
			Int2IntFunction colorMul=(i)->{
				int v=f.get(i);
				key.usedColorMultipliers.put(i, v);
				return v;
			};
			
			Function<BakedQuad, BakedQuad> tintTransformer=new QuadTransformer(TransformationMatrix.identity(), colorMul);
			IBakedModel model=Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModel(key.state);
			
			for(Direction side:Direction.values()){
				List<BakedQuad> quads=model.getQuads(key.state, side, RANDOM, EmptyModelData.INSTANCE).stream()
						.map(tintTransformer)
						.collect(Collectors.toCollection(ArrayList::new));
				
				if(side==Direction.UP){
					TextureAtlasSprite sprite=Minecraft.getInstance()
						.getModelManager()
						.getAtlasTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE)
						.getSprite(new ResourceLocation(IPOMod.ID, "block/postbase"));
					
					quads.add(ModelUtils.createBakedQuad(DefaultVertexFormats.BLOCK, verts, side, sprite, uvs, color, false));
				}
				
				this.quads.add(quads);
			}
			this.quads.add(ImmutableList.of());
		}
		
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData){
			return this.quads.get(side == null ? (this.quads.size()-1) : side.getIndex());
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
			this.state=state;
			this.facing=facing;
			this.allColorMultipliers=colorMul;
			this.usedColorMultipliers=null;
		}
		
		@Override
		public boolean equals(Object obj){
			if(this == obj){
				return true;
			}
			if(obj == null || this.getClass() != obj.getClass()){
				return false;
			}
			
			Key other=(Key)obj;
			return this.state.equals(other.state) && this.facing==other.facing && sameColorMultipliersAs(other);
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
