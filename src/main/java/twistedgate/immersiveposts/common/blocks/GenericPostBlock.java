package twistedgate.immersiveposts.common.blocks;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

public class GenericPostBlock extends IPOBlockBase{
	protected final EnumPostMaterial postMaterial;
	public GenericPostBlock(EnumPostMaterial postMaterial, @Nonnull String name_post){
		super(postMaterial.getString() + name_post, postMaterial.getProperties());
		this.postMaterial = postMaterial;
	}
	
	public final EnumPostMaterial getPostMaterial(){
		return this.postMaterial;
	}
	
	/** Replaces itself with Air or with Water if Waterlogged. (Convenience Method) */
	protected void replaceSelf(BlockState stateIn, World world, BlockPos pos){
		if(stateIn.hasProperty(BlockStateProperties.WATERLOGGED)){
			world.setBlockState(pos, stateIn.get(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState());
		}
	}
}
