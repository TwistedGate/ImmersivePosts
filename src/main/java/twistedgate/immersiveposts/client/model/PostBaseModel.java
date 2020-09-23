package twistedgate.immersiveposts.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.utils.CombinedModelData;
import blusunrize.immersiveengineering.client.utils.SinglePropertyModelData;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ILightReader;
import net.minecraftforge.client.model.ModelLoader;
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
		
		if(extraData.hasProperty(IPOModelData.POSTBASE)){
			IPOModelData.PostBaseModelData data=extraData.getData(IPOModelData.POSTBASE);
			hState=data.state;
		}
		
		Key key=new Key(hState);
		SpecialPostBaseModel special=CACHE.getIfPresent(key);
		if(special==null){
			special=new SpecialPostBaseModel(key);
			CACHE.put(key, special);
		}
		
		return special.getQuads(state, side, rand);
	}
	
	@Override
	public IModelData getModelData(@Nonnull ILightReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData){
		List<IModelData> list=new ArrayList<>();
		list.add(tileData);
		
		TileEntity te=world.getTileEntity(pos);
		if(te instanceof PostBaseTileEntity){
			PostBaseTileEntity base=(PostBaseTileEntity)te;
			
			IPOModelData.PostBaseModelData data=new IPOModelData.PostBaseModelData(Block.getBlockFromItem(base.getStack().getItem()).getDefaultState());
			
			list.add(new SinglePropertyModelData<>(data, IPOModelData.POSTBASE));
		}
		
		return new CombinedModelData(list.toArray(new IModelData[0]));
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
		
		private static final Vec3d[] verts=new Vec3d[]{
				new Vec3d(0.25F, 1.001F, 0.25F), new Vec3d(0.25F, 1.001F, 0.75F),
				new Vec3d(0.75F, 1.001F, 0.75F), new Vec3d(0.75F, 1.001F, 0.25F),
		};
		
		private static final double[] uvs=new double[]{
				8,0,
				16,8
		};
		
		private static final float[] color=new float[]{1.0F, 1.0F, 1.0F, 1.0F};
		
		List<List<BakedQuad>> quads=new ArrayList<>(6);
		public SpecialPostBaseModel(Key key){
			build(key);
		}
		
		@SuppressWarnings("deprecation")
		private void build(Key key){
			IBakedModel model=Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModel(key.state);
			
			for(Direction side:Direction.values()){
				List<BakedQuad> quads=new ArrayList<>(model.getQuads(key.state, side, RANDOM));
				
				if(side==Direction.UP){
					TextureAtlasSprite sprite=Minecraft.getInstance()
						.getModelManager()
						.getAtlasTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE)
						.getSprite(new ResourceLocation(IPOMod.ID, "block/postbase"));
					
					quads.add(ClientUtils.createBakedQuad(DefaultVertexFormats.BLOCK, verts, side, sprite, uvs, color, false));
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
		
		public Key(BlockState state){
			this.state=state;
		}
		
		@Override
		public boolean equals(Object obj){
			if(this==obj){
				return true;
			}
			if(obj==null || this.getClass()!=obj.getClass()){
				return false;
			}
			
			Key other=(Key)obj;
			return this.state.equals(other.state);
		}
		
		@Override
		public int hashCode(){
			return 31 * Utils.hashBlockstate(this.state);
		}
	}
}
