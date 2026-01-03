package twistedgate.immersiveposts.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import twistedgate.immersiveposts.client.model.PostBaseModel;
import twistedgate.immersiveposts.common.IPOContent;

public class ClientEventHandler{
	@SubscribeEvent
	public void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event){
		event.register(PostBaseModel.Loader.LOCATION, new PostBaseModel.Loader());
	}
	
	@SubscribeEvent
	public void colorReg(RegisterColorHandlersEvent.Block event){
		event.register(new ColorHandler(), IPOContent.Blocks.POST_BASE.get());
	}
}
