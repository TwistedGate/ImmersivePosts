package twistedgate.immersiveposts.common.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.IPOContent.Items;
import twistedgate.immersiveposts.common.IPOTags;

/**
 * @author TwistedGate
 */
public class IPOItemTags extends ItemTagsProvider{
	public IPOItemTags(DataGenerator dataGen, BlockTagsProvider blockTags, ExistingFileHelper exFileHelper) {
		super(dataGen, blockTags, IPOMod.ID, exFileHelper);
	}
	
	@Override
	protected void registerTags(){
		getOrCreateBuilder(IPOTags.Rods.ALL)
			.add(Items.ROD_GOLD.get())
			.add(Items.ROD_COPPER.get())
			.add(Items.ROD_LEAD.get())
			.add(Items.ROD_SILVER.get())
			.add(Items.ROD_NICKEL.get())
			.add(Items.ROD_CONSTANTAN.get())
			.add(Items.ROD_ELECTRUM.get())
			.add(Items.ROD_URANIUM.get());
		
		getOrCreateBuilder(IPOTags.Rods.GOLD)
			.add(Items.ROD_GOLD.get());
		
		getOrCreateBuilder(IPOTags.Rods.COPPER)
			.add(Items.ROD_COPPER.get());
		
		getOrCreateBuilder(IPOTags.Rods.LEAD)
			.add(Items.ROD_LEAD.get());
		
		getOrCreateBuilder(IPOTags.Rods.SILVER)
			.add(Items.ROD_SILVER.get());
		
		getOrCreateBuilder(IPOTags.Rods.NICKEL)
			.add(Items.ROD_NICKEL.get());
		
		getOrCreateBuilder(IPOTags.Rods.CONSTANTAN)
			.add(Items.ROD_CONSTANTAN.get());
		
		getOrCreateBuilder(IPOTags.Rods.ELECTRUM)
			.add(Items.ROD_ELECTRUM.get());
		
		getOrCreateBuilder(IPOTags.Rods.URANIUM)
			.add(Items.ROD_URANIUM.get());
	}
}
