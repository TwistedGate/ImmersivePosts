package twistedgate.immersiveposts.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.IPOTags;

/**
 * @author TwistedGate
 */
public class IPOItemTags extends ItemTagsProvider{
	public IPOItemTags(DataGenerator generatorIn){
		super(generatorIn);
	}
	
	@Override
	protected void registerTags(){
		getBuilder(IPOTags.Rods.ALL)
			.add(IPOStuff.rod_Gold)
			.add(IPOStuff.rod_Copper)
			.add(IPOStuff.rod_Lead)
			.add(IPOStuff.rod_Silver)
			.add(IPOStuff.rod_Nickel)
			.add(IPOStuff.rod_Constantan)
			.add(IPOStuff.rod_Electrum)
			.add(IPOStuff.rod_Uranium);
		
		getBuilder(IPOTags.Rods.GOLD)
			.add(IPOStuff.rod_Gold);
		
		getBuilder(IPOTags.Rods.COPPER)
			.add(IPOStuff.rod_Copper);
		
		getBuilder(IPOTags.Rods.LEAD)
			.add(IPOStuff.rod_Lead);
		
		getBuilder(IPOTags.Rods.SILVER)
			.add(IPOStuff.rod_Silver);
		
		getBuilder(IPOTags.Rods.NICKEL)
			.add(IPOStuff.rod_Nickel);
		
		getBuilder(IPOTags.Rods.CONSTANTAN)
			.add(IPOStuff.rod_Constantan);
		
		getBuilder(IPOTags.Rods.ELECTRUM)
			.add(IPOStuff.rod_Electrum);
		
		getBuilder(IPOTags.Rods.URANIUM)
			.add(IPOStuff.rod_Uranium);
	}
}
