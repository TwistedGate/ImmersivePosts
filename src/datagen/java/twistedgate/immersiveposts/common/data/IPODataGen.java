package twistedgate.immersiveposts.common.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.data.loot.IPOBlockLoot;

/**
 * @author TwistedGate
 */
@EventBusSubscriber(modid = IPOMod.ID, bus = Bus.MOD)
public class IPODataGen{
	public static final Logger log = LogManager.getLogger(IPOMod.ID + "/DataGenerator");
	
	@SubscribeEvent
	public static void generate(GatherDataEvent event){
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper exhelper = event.getExistingFileHelper();
		
		if(event.includeServer()){
			IPOBlockTags blocktags = new IPOBlockTags(generator, exhelper);
			generator.addProvider(blocktags);
			generator.addProvider(new IPOItemTags(generator, blocktags, exhelper));
			generator.addProvider(new IPOBlockLoot(generator));
			generator.addProvider(new IPORecipes(generator));
			
		}
		
		if(event.includeClient()){
			generator.addProvider(new IPOBlockStates(generator, exhelper));
			generator.addProvider(new IPOItemModels(generator, exhelper));
		}
	}
}
