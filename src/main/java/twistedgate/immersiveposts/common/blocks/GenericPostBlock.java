package twistedgate.immersiveposts.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import twistedgate.immersiveposts.api.posts.IPostMaterial;

import javax.annotation.Nonnull;

public class GenericPostBlock extends IPOBlockBase{
	protected final IPostMaterial postMaterial;
	
	public GenericPostBlock(IPostMaterial material){
		super(material.getBlockProperties());
		this.postMaterial = material;
	}
	
	public final IPostMaterial getPostMaterial(){
		return this.postMaterial;
	}
	
	/**
	 * Replaces itself with Air or with Water if Waterlogged. (Convenience
	 * Method)
	 */
	protected void replaceSelf(BlockState stateIn, Level world, BlockPos pos){
		if(stateIn.hasProperty(BlockStateProperties.WATERLOGGED)){
			world.setBlockAndUpdate(pos, stateIn.getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
		}
	}
}
