package twistedgate.immersiveposts.common.data;

import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
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
		CompletableFuture<Provider> provider = event.getLookupProvider();
		ExistingFileHelper exhelper = event.getExistingFileHelper();
		PackOutput output = generator.getPackOutput();
		
		if(event.includeServer()){
			IPOBlockTags blocktags = new IPOBlockTags(output, provider, exhelper);
			generator.addProvider(true, blocktags);
			generator.addProvider(true, new IPOItemTags(output, provider, blocktags, exhelper));
			generator.addProvider(true, new IPOBlockLoot(output));
			generator.addProvider(true, new IPORecipes(output));
			
		}
		
		if(event.includeClient()){
			generator.addProvider(true, new IPOBlockStates(output, exhelper));
			generator.addProvider(true, new IPOItemModels(output, exhelper));
		}
	}
}
