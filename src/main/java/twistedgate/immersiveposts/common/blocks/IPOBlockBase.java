package twistedgate.immersiveposts.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author TwistedGate
 */
public class IPOBlockBase extends Block{
	public IPOBlockBase(Properties properties){
		super(properties);
	}
	
	@Override
	public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction){
		return state;
	}
	
	/** convenience method */
	public static Block getBlockFrom(BlockGetter world, BlockPos pos){
		if(world == null || pos == null)
			return null;
		
		return world.getBlockState(pos).getBlock();
	}
}
