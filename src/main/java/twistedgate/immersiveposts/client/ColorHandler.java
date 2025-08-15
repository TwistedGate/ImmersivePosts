package twistedgate.immersiveposts.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;

import javax.annotation.Nonnull;

public class ColorHandler implements BlockColor{
	@Override
	public int getColor(@Nonnull BlockState state, BlockAndTintGetter world, BlockPos pos, int index){
		if(index >= 0){
			BlockEntity te = world.getBlockEntity(pos);
			if(te instanceof PostBaseTileEntity base && !base.getStack().isEmpty()){
				return Minecraft.getInstance().getBlockColors().getColor(base.getCoverState(), world, pos, index);
			}
		}
		
		return -1;
	}
}
