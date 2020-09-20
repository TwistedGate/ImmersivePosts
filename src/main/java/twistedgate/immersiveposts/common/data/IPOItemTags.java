package twistedgate.immersiveposts.common.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import twistedgate.immersiveposts.IPOContent.Items;
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
			.add(Items.rod_Gold)
			.add(Items.rod_Copper)
			.add(Items.rod_Lead)
			.add(Items.rod_Silver)
			.add(Items.rod_Nickel)
			.add(Items.rod_Constantan)
			.add(Items.rod_Electrum)
			.add(Items.rod_Uranium);
		
		getOrCreateBuilder(IPOTags.Rods.GOLD)
			.add(Items.rod_Gold);
		
		getOrCreateBuilder(IPOTags.Rods.COPPER)
			.add(Items.rod_Copper);
		
		getOrCreateBuilder(IPOTags.Rods.LEAD)
			.add(Items.rod_Lead);
		
		getOrCreateBuilder(IPOTags.Rods.SILVER)
			.add(Items.rod_Silver);
		
		getOrCreateBuilder(IPOTags.Rods.NICKEL)
			.add(Items.rod_Nickel);
		
		getOrCreateBuilder(IPOTags.Rods.CONSTANTAN)
			.add(Items.rod_Constantan);
		
		getOrCreateBuilder(IPOTags.Rods.ELECTRUM)
			.add(Items.rod_Electrum);
		
		getOrCreateBuilder(IPOTags.Rods.URANIUM)
			.add(Items.rod_Uranium);
	}
}
