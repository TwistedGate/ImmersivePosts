package twistedgate.immersiveposts.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.IPOStuff;

public class IPOItemTags extends ItemTagsProvider{
	public IPOItemTags(DataGenerator generatorIn){
		super(generatorIn);
	}
	
	@Override
	protected void registerTags(){
		getBuilder(new ItemTags.Wrapper(new ResourceLocation("forge","rods/all_metal")))
			.add(IPOStuff.rod_Gold)
			.add(IPOStuff.rod_Copper)
			.add(IPOStuff.rod_Lead)
			.add(IPOStuff.rod_Silver)
			.add(IPOStuff.rod_Nickel)
			.add(IPOStuff.rod_Constantan)
			.add(IPOStuff.rod_Electrum)
			.add(IPOStuff.rod_Uranium)
			;
		
		addRod(IPOStuff.rod_Gold, "gold");
		addRod(IPOStuff.rod_Copper, "copper");
		addRod(IPOStuff.rod_Lead, "lead");
		addRod(IPOStuff.rod_Silver, "silver");
		addRod(IPOStuff.rod_Nickel, "nickel");
		addRod(IPOStuff.rod_Constantan, "constantan");
		addRod(IPOStuff.rod_Electrum, "electrum");
		addRod(IPOStuff.rod_Uranium, "uranium");
	}
	
	private void addRod(Item item, String tagName){
		getBuilder(new ItemTags.Wrapper(new ResourceLocation("forge","rods/"+tagName)))
		.add(item);
	}
}
