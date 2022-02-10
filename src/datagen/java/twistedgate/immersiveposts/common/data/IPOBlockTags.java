package twistedgate.immersiveposts.common.data;

import blusunrize.immersiveengineering.common.register.IEBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.IPOContent.Blocks.Fences;
import twistedgate.immersiveposts.common.IPOTags;

public class IPOBlockTags extends BlockTagsProvider{
	public IPOBlockTags(DataGenerator dataGen, ExistingFileHelper exFileHelper) {
		super(dataGen, IPOMod.ID, exFileHelper);
	}
	
	@Override
	protected void addTags(){
		tag(IPOTags.IGNORED_BY_POSTARM)
			.add(IEBlocks.Connectors.POST_TRANSFORMER.get())
			.add(IEBlocks.Connectors.TRANSFORMER.get())
			.add(IEBlocks.Connectors.TRANSFORMER_HV.get());
		
		tag(IPOTags.Fences.ALL)
			.add(Fences.IRON.get())
			.add(Fences.GOLD.get())
			.add(Fences.COPPER.get())
			.add(Fences.LEAD.get())
			.add(Fences.SILVER.get())
			.add(Fences.NICKEL.get())
			.add(Fences.CONSTANTAN.get())
			.add(Fences.ELECTRUM.get())
			.add(Fences.URANIUM.get());
	}
}
