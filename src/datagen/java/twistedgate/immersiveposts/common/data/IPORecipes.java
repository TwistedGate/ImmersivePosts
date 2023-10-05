package twistedgate.immersiveposts.common.data;

import java.util.function.Consumer;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraftforge.common.Tags;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.IPOContent;
import twistedgate.immersiveposts.common.IPOContent.Blocks.Fences;
import twistedgate.immersiveposts.common.IPOContent.Items;
import twistedgate.immersiveposts.common.IPOTags;

/**
 * @author TwistedGate
 */
public class IPORecipes extends RecipeProvider{
	private Consumer<FinishedRecipe> out;
	public IPORecipes(PackOutput output){
		super(output);
	}
	
	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> out){
		this.out = out;
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IPOContent.Blocks.POST_BASE.get(), 6)
			.define('w', Tags.Items.COBBLESTONE)
			.define('s', Blocks.STONE_BRICKS)
			.pattern("s s")
			.pattern("sws")
			.pattern("sws")
			.unlockedBy("has_cobblestone", has(Blocks.COBBLESTONE))
			.unlockedBy("has_stone_bricks", hasTag(ItemTags.STONE_BRICKS))
			.save(out);
		
		fenceAndStickRecipe(Fences.IRON.get(), null, IPOTags.Rods.IRON, IPOTags.Ingots.IRON);
		fenceAndStickRecipe(Fences.GOLD.get(), Items.ROD_GOLD.get(), IPOTags.Rods.GOLD, IPOTags.Ingots.GOLD);
		fenceAndStickRecipe(Fences.COPPER.get(), Items.ROD_COPPER.get(), IPOTags.Rods.COPPER, IPOTags.Ingots.COPPER);
		fenceAndStickRecipe(Fences.LEAD.get(), Items.ROD_LEAD.get(), IPOTags.Rods.LEAD, IPOTags.Ingots.LEAD);
		fenceAndStickRecipe(Fences.SILVER.get(), Items.ROD_SILVER.get(), IPOTags.Rods.SILVER, IPOTags.Ingots.SILVER);
		fenceAndStickRecipe(Fences.NICKEL.get(), Items.ROD_NICKEL.get(), IPOTags.Rods.NICKEL, IPOTags.Ingots.NICKEL);
		fenceAndStickRecipe(Fences.CONSTANTAN.get(), Items.ROD_CONSTANTAN.get(), IPOTags.Rods.CONSTANTAN, IPOTags.Ingots.CONSTANTAN);
		fenceAndStickRecipe(Fences.ELECTRUM.get(), Items.ROD_ELECTRUM.get(), IPOTags.Rods.ELECTRUM, IPOTags.Ingots.ELECTRUM);
		fenceAndStickRecipe(Fences.URANIUM.get(), Items.ROD_URANIUM.get(), IPOTags.Rods.URANIUM, IPOTags.Ingots.URANIUM);
	}
	
	/** Creates both a recipe for fences and the stick needed */
	private void fenceAndStickRecipe(FenceBlock fence, Item rod, TagKey<Item> stickTag, TagKey<Item> ingotTag){
		String stickMat = getMaterialName(stickTag.location()); // Stick Material
		String ingotMat = getMaterialName(ingotTag.location()); // Ingot Material
		
		if(fence != Fences.IRON.get()){
			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, rod, 4)
				.pattern("i")
				.pattern("i")
				.define('i', ingotTag)
				.unlockedBy("has_" + ingotMat + "_ingot", hasTag(ingotTag))
				.save(this.out, new ResourceLocation(IPOMod.ID, "has_" + stickMat + "_rod"));
		}
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, fence, 3)
			.pattern("isi")
			.pattern("isi")
			.define('i', ingotTag)
			.define('s', stickTag)
			.unlockedBy("has_" + stickMat + "_rod", hasTag(stickTag))
			.unlockedBy("has_" + ingotMat + "_ingot", hasTag(ingotTag))
			.save(this.out);
	}
	
	private String getMaterialName(ResourceLocation in){
		return in.getPath().substring(in.getPath().indexOf('/') + 1);
	}
	
	// Private in RecipeProvider
	private static InventoryChangeTrigger.TriggerInstance hasTag(TagKey<Item> tag) {
		return inventoryTrigger(ItemPredicate.Builder.item().of(tag).build());
	}
}
