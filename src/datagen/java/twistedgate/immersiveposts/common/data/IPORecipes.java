package twistedgate.immersiveposts.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.neoforged.neoforge.common.Tags;
import twistedgate.immersiveposts.common.IPOContent;
import twistedgate.immersiveposts.common.IPOContent.Blocks.Fences;
import twistedgate.immersiveposts.common.IPOContent.Items;
import twistedgate.immersiveposts.api.IPOTags;
import twistedgate.immersiveposts.util.ResourceUtils;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * @author TwistedGate
 */
public class IPORecipes extends RecipeProvider{
	private RecipeOutput out;
	public IPORecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> provider){
		super(output, provider);
	}
	
	@Override
	protected void buildRecipes(@Nonnull RecipeOutput out){
		this.out = out;
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IPOContent.Blocks.POST_BASE.get(), 6)
			.define('w', Tags.Items.COBBLESTONES)
			.define('s', Blocks.STONE_BRICKS)
			.pattern("s s")
			.pattern("sws")
			.pattern("sws")
			.unlockedBy("has_cobblestone", has(Blocks.COBBLESTONE))
			.unlockedBy("has_stone_bricks", has(ItemTags.STONE_BRICKS))
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
		
		if(rod != null){
			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, rod, 4)
				.pattern("i")
				.pattern("i")
				.define('i', ingotTag)
				.unlockedBy("has_" + ingotMat + "_ingot", has(ingotTag))
				.save(this.out, ResourceUtils.ipo("has_" + stickMat + "_rod"));
		}
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, fence, 3)
			.pattern("isi")
			.pattern("isi")
			.define('i', ingotTag)
			.define('s', stickTag)
			.unlockedBy("has_" + stickMat + "_rod", has(stickTag))
			.unlockedBy("has_" + ingotMat + "_ingot", has(ingotTag))
			.save(this.out);
	}
	
	private String getMaterialName(ResourceLocation in){
		return in.getPath().substring(in.getPath().indexOf('/') + 1);
	}
}
