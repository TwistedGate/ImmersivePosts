package twistedgate.immersiveposts.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Just a few little convenience methods
 * @author TwistedGate
 */
public class BlockUtilities{
	public static Block getBlockFrom(IBlockAccess world, BlockPos pos){
		return getBlockFromDirection(world, pos, null);
	}
	
	public static Block getBlockFromDirection(IBlockAccess world, BlockPos pos, EnumFacing dir){
		if(world==null) return null;
		
		return world.getBlockState(dir!=null?pos.offset(dir):pos).getBlock();
	}
	
	public static void setBlockStateAtDirection(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing dir, int flags){
		if(world==null) return;
		
		((World)world).setBlockState(dir!=null?pos.offset(dir):pos, state, flags);
	}
	
	public static void setBlockStateAtDirection(World world, BlockPos pos, IBlockState state, EnumFacing dir, int flags){
		if(world==null) return;
		
		world.setBlockState(dir!=null?pos.offset(dir):pos, state, flags);
	}
}
