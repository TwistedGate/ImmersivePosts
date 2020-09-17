package twistedgate.immersiveposts.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import twistedgate.immersiveposts.IPOContent.Items;
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
			.add(Items.rod_Gold)
			.add(Items.rod_Copper)
			.add(Items.rod_Lead)
			.add(Items.rod_Silver)
			.add(Items.rod_Nickel)
			.add(Items.rod_Constantan)
			.add(Items.rod_Electrum)
			.add(Items.rod_Uranium);
		
		getBuilder(IPOTags.Rods.GOLD)
			.add(Items.rod_Gold);
		
		getBuilder(IPOTags.Rods.COPPER)
			.add(Items.rod_Copper);
		
		getBuilder(IPOTags.Rods.LEAD)
			.add(Items.rod_Lead);
		
		getBuilder(IPOTags.Rods.SILVER)
			.add(Items.rod_Silver);
		
		getBuilder(IPOTags.Rods.NICKEL)
			.add(Items.rod_Nickel);
		
		getBuilder(IPOTags.Rods.CONSTANTAN)
			.add(Items.rod_Constantan);
		
		getBuilder(IPOTags.Rods.ELECTRUM)
			.add(Items.rod_Electrum);
		
		getBuilder(IPOTags.Rods.URANIUM)
			.add(Items.rod_Uranium);
	}
}
