package twistedgate.immersiveposts.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.IPOContent;

/**
 * @author TwistedGate
 */
public class IPOBlockBase extends Block{
	public IPOBlockBase(String name, Properties properties){
		super(properties);
		
		setRegistryName(new ResourceLocation(IPOMod.ID, name));
		
		IPOContent.BLOCKS.add(this);
	}
	
	@Override
	public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction){
		return state;
	}
	
	/** convenience method */
	public static Block getBlockFrom(IBlockReader world, BlockPos pos){
		if(world==null || pos==null) return null;
		
		return world.getBlockState(pos).getBlock();
	}
}
