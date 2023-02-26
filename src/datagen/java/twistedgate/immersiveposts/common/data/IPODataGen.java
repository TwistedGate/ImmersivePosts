package twistedgate.immersiveposts.common.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
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
			generator.addProvider(true, blocktags);
			generator.addProvider(true, new IPOItemTags(generator, blocktags, exhelper));
			generator.addProvider(true, new IPOBlockLoot(generator));
			generator.addProvider(true, new IPORecipes(generator));
			
		}
		
		if(event.includeClient()){
			generator.addProvider(true, new IPOBlockStates(generator, exhelper));
			generator.addProvider(true, new IPOItemModels(generator, exhelper));
		}
	}
}
