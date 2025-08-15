package twistedgate.immersiveposts.common.data;

import blusunrize.immersiveengineering.common.register.IEBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.IPOContent.Blocks;
import twistedgate.immersiveposts.common.IPOTags;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class IPOBlockTags extends BlockTagsProvider{
	public IPOBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper exFileHelper){
		super(output, lookupProvider, IPOMod.ID, exFileHelper);
	}
	
	@Override
	public String getName(){
		return getClass().getSimpleName();
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider){
		miningLevels();
		
		tag(IPOTags.IGNORED_BY_POSTARM)
			.add(IEBlocks.Connectors.POST_TRANSFORMER.get())
			.add(IEBlocks.Connectors.TRANSFORMER.get())
			.add(IEBlocks.Connectors.TRANSFORMER_HV.get());
		
		tag(IPOTags.Fences.ALL)
			.add(Blocks.Fences.IRON.get())
			.add(Blocks.Fences.GOLD.get())
			.add(Blocks.Fences.COPPER.get())
			.add(Blocks.Fences.LEAD.get())
			.add(Blocks.Fences.SILVER.get())
			.add(Blocks.Fences.NICKEL.get())
			.add(Blocks.Fences.CONSTANTAN.get())
			.add(Blocks.Fences.ELECTRUM.get())
			.add(Blocks.Fences.URANIUM.get());
	}
	
	private void miningLevels(){
		setMiningLevel(Blocks.POST_BASE, Tiers.STONE);
		
		for(RegistryObject<FenceBlock> object:Blocks.Fences.ALL_FENCES){
			setMiningLevel(object, Tiers.STONE);
		}
		
		setMiningLevel(EnumPostMaterial.WOOD, Tiers.WOOD);
		setMiningLevel(EnumPostMaterial.ALUMINIUM, Tiers.IRON);
		setMiningLevel(EnumPostMaterial.STEEL, Tiers.IRON);
		setMiningLevel(EnumPostMaterial.NETHERBRICK, Tiers.STONE);
		setMiningLevel(EnumPostMaterial.IRON, Tiers.IRON);
		setMiningLevel(EnumPostMaterial.GOLD, Tiers.IRON);
		setMiningLevel(EnumPostMaterial.COPPER, Tiers.IRON);
		setMiningLevel(EnumPostMaterial.LEAD, Tiers.IRON);
		setMiningLevel(EnumPostMaterial.SILVER, Tiers.IRON);
		setMiningLevel(EnumPostMaterial.NICKEL, Tiers.IRON);
		setMiningLevel(EnumPostMaterial.CONSTANTAN, Tiers.IRON);
		setMiningLevel(EnumPostMaterial.ELECTRUM, Tiers.IRON);
		setMiningLevel(EnumPostMaterial.URANIUM, Tiers.IRON);
		setMiningLevel(EnumPostMaterial.CONCRETE, Tiers.STONE);
		setMiningLevel(EnumPostMaterial.CONCRETE_LEADED, Tiers.STONE);
	}
	
	private void setMiningLevel(EnumPostMaterial mat, Tiers tier){
		setMiningLevel(() -> Blocks.Posts.get(mat), tier);
		setMiningLevel(() -> Blocks.HorizontalTruss.get(mat), tier);
	}
	
	private void setMiningLevel(Supplier<? extends Block> block, Tiers tier){
		TagKey<Block> with = switch(tier){
			case WOOD -> BlockTags.MINEABLE_WITH_AXE;
			case STONE, GOLD, IRON, DIAMOND, NETHERITE -> BlockTags.MINEABLE_WITH_PICKAXE;
			default -> throw new IllegalArgumentException("Unexpected value: " + tier);
		};
		tag(with).add(block.get());
		
		if(tier == Tiers.WOOD)
			return;
		
		TagKey<Block> type = switch(tier){
			case STONE -> BlockTags.NEEDS_STONE_TOOL;
			case IRON, GOLD -> BlockTags.NEEDS_IRON_TOOL;
			case DIAMOND, NETHERITE -> BlockTags.NEEDS_DIAMOND_TOOL;
			default -> throw new IllegalArgumentException("Unexpected value: " + tier);
		};
		
		tag(type).add(block.get());
	}
}
