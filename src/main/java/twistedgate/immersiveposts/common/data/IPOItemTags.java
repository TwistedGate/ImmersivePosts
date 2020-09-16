package twistedgate.immersiveposts.common.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.IPOTags;

/**
 * @author TwistedGate
 */
public class IPOItemTags extends ItemTagsProvider{
	
	@SuppressWarnings("deprecation")
	public IPOItemTags(DataGenerator generatorIn, BlockTagsProvider exhelper){
		super(generatorIn, exhelper);
	}
	
	@Override
	protected void registerTags(){
		getOrCreateBuilder(IPOTags.Rods.ALL)
			.add(IPOStuff.rod_Gold)
			.add(IPOStuff.rod_Copper)
			.add(IPOStuff.rod_Lead)
			.add(IPOStuff.rod_Silver)
			.add(IPOStuff.rod_Nickel)
			.add(IPOStuff.rod_Constantan)
			.add(IPOStuff.rod_Electrum)
			.add(IPOStuff.rod_Uranium);
		
		getOrCreateBuilder(IPOTags.Rods.GOLD)
			.add(IPOStuff.rod_Gold);
		
		getOrCreateBuilder(IPOTags.Rods.COPPER)
			.add(IPOStuff.rod_Copper);
		
		getOrCreateBuilder(IPOTags.Rods.LEAD)
			.add(IPOStuff.rod_Lead);
		
		getOrCreateBuilder(IPOTags.Rods.SILVER)
			.add(IPOStuff.rod_Silver);
		
		getOrCreateBuilder(IPOTags.Rods.NICKEL)
			.add(IPOStuff.rod_Nickel);
		
		getOrCreateBuilder(IPOTags.Rods.CONSTANTAN)
			.add(IPOStuff.rod_Constantan);
		
		getOrCreateBuilder(IPOTags.Rods.ELECTRUM)
			.add(IPOStuff.rod_Electrum);
		
		getOrCreateBuilder(IPOTags.Rods.URANIUM)
			.add(IPOStuff.rod_Uranium);
	}
}
