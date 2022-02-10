package twistedgate.immersiveposts.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;

public class ColorHandler implements BlockColor{
	@Override
	public int getColor(BlockState state, BlockAndTintGetter world, BlockPos pos, int index){
		if(index >= 0){
			BlockEntity te = world.getBlockEntity(pos);
			if(te instanceof PostBaseTileEntity && !((PostBaseTileEntity) te).getStack().isEmpty()){
				PostBaseTileEntity base = (PostBaseTileEntity) te;
				
				int color = Minecraft.getInstance().getBlockColors().getColor(base.getCoverState(), world, pos, index);
				return color;
			}
		}
		
		return -1;
	}
}
