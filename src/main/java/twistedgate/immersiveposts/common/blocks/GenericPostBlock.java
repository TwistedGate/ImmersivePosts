package twistedgate.immersiveposts.common.blocks;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twistedgate.immersiveposts.api.posts.IPostMaterial;

public class GenericPostBlock extends IPOBlockBase{
	protected final IPostMaterial postMaterial;
	
	public GenericPostBlock(IPostMaterial material){
		super(material.getBlockProperties());
		this.postMaterial = material;
	}
	
	public GenericPostBlock(IPostMaterial material, @Nonnull String name_post){
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
	protected void replaceSelf(BlockState stateIn, World world, BlockPos pos){
		if(stateIn.hasProperty(BlockStateProperties.WATERLOGGED)){
			world.setBlockState(pos, stateIn.get(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState());
		}
	}
}
