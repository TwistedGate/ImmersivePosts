package twistedgate.immersiveposts.common.data;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.IPOContent.Items;
import twistedgate.immersiveposts.common.IPOTags;

/**
 * @author TwistedGate
 */
public class IPOItemTags extends ItemTagsProvider{
	public IPOItemTags(PackOutput output, CompletableFuture<Provider> provider, TagsProvider<Block> tagProvider, @Nullable ExistingFileHelper exFileHelper){
		super(output, provider, tagProvider, IPOMod.ID, exFileHelper);
	}
	
	@Override
	protected void addTags(Provider provider){
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
