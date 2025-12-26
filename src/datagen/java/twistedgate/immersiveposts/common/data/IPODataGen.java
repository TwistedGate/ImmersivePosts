package twistedgate.immersiveposts.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twistedgate.immersiveposts.api.IPOMod;
import twistedgate.immersiveposts.common.data.loot.IPOBlockLoot;

import java.util.concurrent.CompletableFuture;

/**
 * @author TwistedGate
 */
@EventBusSubscriber(modid = IPOMod.ID, bus = Bus.MOD)
public class IPODataGen{
	public static final Logger log = LogManager.getLogger(IPOMod.ID + "/DataGenerator");
	
	@SubscribeEvent
	public static void generate(GatherDataEvent event){
		final ExistingFileHelper exHelper = event.getExistingFileHelper();
		final DataGenerator generator = event.getGenerator();
		final PackOutput output = generator.getPackOutput();
		final CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
		
		if(event.includeServer()){
			IPOBlockTags blockTags = new IPOBlockTags(output, provider, exHelper);
			generator.addProvider(true, blockTags);
			generator.addProvider(true, new IPOItemTags(output, provider, blockTags.contentsGetter(), exHelper));
			generator.addProvider(true, new IPOBlockLoot(output, provider));
			generator.addProvider(true, new IPORecipes(output, provider));
			
		}
		
		if(event.includeClient()){
			generator.addProvider(true, new IPOBlockStates(output, exHelper));
			generator.addProvider(true, new IPOItemModels(output, exHelper));
		}
	}
}
