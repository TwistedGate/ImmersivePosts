package twistedgate.immersiveposts.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;

public class ColorHandler implements IBlockColor{
	@Override
	public int getColor(BlockState state, ILightReader world, BlockPos pos, int index){
		if(index>=0){
			TileEntity te=world.getTileEntity(pos);
			if(te instanceof PostBaseTileEntity && !((PostBaseTileEntity)te).getStack().isEmpty()){
				PostBaseTileEntity base=(PostBaseTileEntity)te;
				
				int color=Minecraft.getInstance().getBlockColors().getColor(base.getCoverState(), world, pos, index);
				return color;
			}
		}
		
		return -1;
	}
}
