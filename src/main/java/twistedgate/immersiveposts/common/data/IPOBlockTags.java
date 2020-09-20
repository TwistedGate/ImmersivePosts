package twistedgate.immersiveposts.common.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import twistedgate.immersiveposts.IPOContent.Blocks.Fences;
import twistedgate.immersiveposts.IPOTags;

public class IPOBlockTags extends BlockTagsProvider{
	
	@SuppressWarnings("deprecation")
	public IPOBlockTags(DataGenerator generatorIn){
		super(generatorIn);
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
