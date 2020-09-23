package twistedgate.immersiveposts.client.model;

import net.minecraft.block.BlockState;
import net.minecraftforge.client.model.data.ModelProperty;

public class IPOModelData{
	public static final ModelProperty<PostBaseModelData> POSTBASE=new ModelProperty<>();
	
	public static class PostBaseModelData{
		public final BlockState state;
		public PostBaseModelData(BlockState state){
			this.state=state;
		}
	}
}
