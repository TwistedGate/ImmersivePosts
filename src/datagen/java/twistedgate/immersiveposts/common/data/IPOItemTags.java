package twistedgate.immersiveposts.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
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
	protected void addTags(){
		tag(IPOTags.Rods.ALL)
			.add(Items.ROD_GOLD.get())
			.add(Items.ROD_COPPER.get())
			.add(Items.ROD_LEAD.get())
			.add(Items.ROD_SILVER.get())
			.add(Items.ROD_NICKEL.get())
			.add(Items.ROD_CONSTANTAN.get())
			.add(Items.ROD_ELECTRUM.get())
			.add(Items.ROD_URANIUM.get());
		
		tag(IPOTags.Rods.GOLD)
			.add(Items.ROD_GOLD.get());
		
		tag(IPOTags.Rods.COPPER)
			.add(Items.ROD_COPPER.get());
		
		tag(IPOTags.Rods.LEAD)
			.add(Items.ROD_LEAD.get());
		
		tag(IPOTags.Rods.SILVER)
			.add(Items.ROD_SILVER.get());
		
		tag(IPOTags.Rods.NICKEL)
			.add(Items.ROD_NICKEL.get());
		
		tag(IPOTags.Rods.CONSTANTAN)
			.add(Items.ROD_CONSTANTAN.get());
		
		tag(IPOTags.Rods.ELECTRUM)
			.add(Items.ROD_ELECTRUM.get());
		
		tag(IPOTags.Rods.URANIUM)
			.add(Items.ROD_URANIUM.get());
	}
}
