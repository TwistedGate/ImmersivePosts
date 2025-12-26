package twistedgate.immersiveposts.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import twistedgate.immersiveposts.api.IPOMod;
import twistedgate.immersiveposts.common.IPOContent.Items;
import twistedgate.immersiveposts.common.IPOTags;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * @author TwistedGate
 */
public class IPOItemTags extends ItemTagsProvider{
	public IPOItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper exFileHelper){
		super(output, lookupProvider, blockTags, IPOMod.ID, exFileHelper);
	}
	
	@Override
	protected void addTags(@Nonnull HolderLookup.Provider provider){
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
