package twistedgate.immersiveposts.client.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class IPOBakedModel implements BakedModel{
	@Override
	@Nonnull
	public abstract List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, @Nullable RenderType layer);
	
	@Override
	@Nonnull
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand){
		return getQuads(state, side, rand, ModelData.EMPTY, null);
	}
	
	@Override
	public boolean usesBlockLight(){
		return true;
	}
}
