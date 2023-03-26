package twistedgate.immersiveposts.client.model;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelProperty;

public class IPOModelData{
	public static final ModelProperty<PostBaseModelData> POSTBASE = new ModelProperty<>();
	
	public static class PostBaseModelData{
		public final BlockState state;
		public final Direction facing;
		@Deprecated
		public final Int2IntFunction color;
		public PostBaseModelData(BlockState state, Direction facing, Int2IntFunction color){
			this.state = state;
			this.color = color;
			this.facing = facing;
		}
	}
}
