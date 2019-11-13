package twistedgate.immersiveposts.utils;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Just a few little convenience methods
 * @author TwistedGate
 */
public class BlockHelper{
	public static Block getBlockFrom(IBlockAccess world, BlockPos pos){
		if(world==null) return null;
		
		return world.getBlockState(pos).getBlock();
	}
}
