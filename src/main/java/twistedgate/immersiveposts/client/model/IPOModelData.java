package twistedgate.immersiveposts.client.model;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelProperty;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;

public class IPOModelData{
	public static final ModelProperty<PostBaseModelData> POSTBASE = new ModelProperty<>();
	
	public static class PostBaseModelData{
		public final BlockState state;
		public final Direction facing;
		
		public PostBaseModelData(PostBaseTileEntity te){
			this.state = te.getCoverState();
			this.facing = te.getFacing();
		}
	}
}
