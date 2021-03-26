package twistedgate.immersiveposts.common.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import twistedgate.immersiveposts.common.IPOContent.Blocks.Fences;
import twistedgate.immersiveposts.common.IPOMod;
import twistedgate.immersiveposts.common.IPOTags;

public class IPOBlockTags extends BlockTagsProvider{
	public IPOBlockTags(DataGenerator dataGen, ExistingFileHelper exFileHelper) {
		super(dataGen, IPOMod.ID, exFileHelper);
	}
	
	@Override
	protected void registerTags(){
		getOrCreateBuilder(IPOTags.Fences.ALL)
			.add(Fences.iron)
			.add(Fences.gold)
			.add(Fences.copper)
			.add(Fences.lead)
			.add(Fences.silver)
			.add(Fences.nickel)
			.add(Fences.constantan)
			.add(Fences.electrum)
			.add(Fences.uranium);
	}
}
