package twistedgate.immersiveposts.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import twistedgate.immersiveposts.IPOMod;

@EventBusSubscriber(modid=IPOMod.ID, bus=Bus.MOD)
public class IPODataGen{
	
	@SubscribeEvent
	public static void generate(GatherDataEvent event){
		if(event.includeServer()){
			DataGenerator generator=event.getGenerator();
			
			generator.addProvider(new IPORecipes(generator));
			IPOModelLoader loadedModels=new IPOModelLoader(generator, event.getExistingFileHelper());
			IPOBlockStates blockStates=new IPOBlockStates(generator, event.getExistingFileHelper(), loadedModels);
			generator.addProvider(blockStates);
			generator.addProvider(loadedModels);
		}
	}
}
