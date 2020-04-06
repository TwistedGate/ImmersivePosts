package twistedgate.immersiveposts.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;

/**
 * @author TwistedGate
 */
public class IPOBlockBase extends Block{
	public IPOBlockBase(String name, Properties properties){
		super(properties);
		
		setRegistryName(new ResourceLocation(IPOMod.ID, name));
		
		IPOStuff.BLOCKS.add(this);
	}
	
	@Override
	public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction){
		return state;
	}
}
