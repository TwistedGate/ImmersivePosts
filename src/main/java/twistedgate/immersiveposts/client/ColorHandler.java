package twistedgate.immersiveposts.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ColorHandler implements BlockColor{
	@Override
	public int getColor(@Nonnull BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int index){
		if(index < 0 || world == null || pos == null || !(world.getBlockEntity(pos) instanceof PostBaseTileEntity base) || base.getStack().isEmpty())
			return 0xFFFFFF;
		
		return Minecraft.getInstance().getBlockColors().getColor(base.getCoverState(), world, pos, index);
	}
}
