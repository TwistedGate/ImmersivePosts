package twistedgate.immersiveposts.common.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import twistedgate.immersiveposts.IPOMod;

/**
 * @author TwistedGate
 */
@EventBusSubscriber(modid=IPOMod.ID, bus=Bus.MOD)
public class IPODataGen{
	public static final Logger log=LogManager.getLogger(IPOMod.ID+"/DataGenerator");
	
	
	@SubscribeEvent
	public static void generate(GatherDataEvent event){
		if(event.includeServer()){
			DataGenerator generator=event.getGenerator();
			ExistingFileHelper exhelper=event.getExistingFileHelper();
			
			IPOBlockTags blocktags=new IPOBlockTags(generator);
			generator.addProvider(blocktags);
			generator.addProvider(new IPOItemTags(generator, blocktags));
			generator.addProvider(new IPORecipes(generator));
			
			IPOLoadedModels loadedModels=new IPOLoadedModels(generator, exhelper);
			IPOBlockStates blockStates=new IPOBlockStates(generator, exhelper, loadedModels);
			
			generator.addProvider(blockStates);
			generator.addProvider(loadedModels);
			generator.addProvider(new IPOItemModels(generator, exhelper, blockStates));
		}
	}
}
