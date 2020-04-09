package twistedgate.immersiveposts.common.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.IPOTags;

public class IPOBlockTags extends BlockTagsProvider{
	public IPOBlockTags(DataGenerator generatorIn){
		super(generatorIn);
	}
	
	@Override
	protected void registerTags(){
		getBuilder(IPOTags.Fences.ALL)
			.add(IPOStuff.fence_Iron)
			.add(IPOStuff.fence_Gold)
			.add(IPOStuff.fence_Copper)
			.add(IPOStuff.fence_Lead)
			.add(IPOStuff.fence_Silver)
			.add(IPOStuff.fence_Nickel)
			.add(IPOStuff.fence_Constantan)
			.add(IPOStuff.fence_Electrum)
			.add(IPOStuff.fence_Uranium);
	}
}
