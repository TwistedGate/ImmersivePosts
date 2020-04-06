package twistedgate.immersiveposts.common.data;

import java.util.function.Consumer;

import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.IPOTags;

/**
 * @author TwistedGate
 */
public class IPORecipes extends RecipeProvider{
	private Consumer<IFinishedRecipe> consumer;
	public IPORecipes(DataGenerator generatorIn){
		super(generatorIn);
	}
	
	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer){
		this.consumer=consumer;
		
		ShapedRecipeBuilder.shapedRecipe(IPOStuff.post_Base, 6)
			.key('w', Tags.Items.COBBLESTONE)
			.key('s', Blocks.STONE_BRICKS)
			.patternLine("s s")
			.patternLine("sws")
			.patternLine("sws")
			.addCriterion("has_cobblestone", hasItem(Blocks.COBBLESTONE))
			.addCriterion("has_stone_bricks", hasItem(ItemTags.STONE_BRICKS))
			.build(consumer);
		
		fenceAndStickRecipe(IPOStuff.fence_Iron, null, IPOTags.Rods.IRON, IPOTags.Ingots.IRON);
		fenceAndStickRecipe(IPOStuff.fence_Gold, IPOStuff.rod_Gold, IPOTags.Rods.GOLD, IPOTags.Ingots.GOLD);
		fenceAndStickRecipe(IPOStuff.fence_Copper, IPOStuff.rod_Copper, IPOTags.Rods.COPPER, IPOTags.Ingots.COPPER);
		fenceAndStickRecipe(IPOStuff.fence_Lead, IPOStuff.rod_Lead, IPOTags.Rods.LEAD, IPOTags.Ingots.LEAD);
		fenceAndStickRecipe(IPOStuff.fence_Silver, IPOStuff.rod_Silver, IPOTags.Rods.SILVER, IPOTags.Ingots.SILVER);
		fenceAndStickRecipe(IPOStuff.fence_Nickel, IPOStuff.rod_Nickel, IPOTags.Rods.NICKEL, IPOTags.Ingots.NICKEL);
		fenceAndStickRecipe(IPOStuff.fence_Constantan, IPOStuff.rod_Constantan, IPOTags.Rods.CONSTANTAN, IPOTags.Ingots.CONSTANTAN);
		fenceAndStickRecipe(IPOStuff.fence_Electrum, IPOStuff.rod_Electrum, IPOTags.Rods.ELECTRUM, IPOTags.Ingots.ELECTRUM);
		fenceAndStickRecipe(IPOStuff.fence_Uranium, IPOStuff.rod_Uranium, IPOTags.Rods.URANIUM, IPOTags.Ingots.URANIUM);
	}
	
	/** Creates both a recipe for fences and the stick needed */
	private void fenceAndStickRecipe(FenceBlock fence, Item rod, Tag<Item> stickTag, Tag<Item> ingotTag){
		String stickMat=stickTag.getId().getPath();
		stickMat=stickMat.substring(stickMat.indexOf('/')+1);
		
		String ingotMat=ingotTag.getId().getPath();
		ingotMat=ingotMat.substring(ingotMat.indexOf('/')+1);
		
		if(fence!=IPOStuff.fence_Iron)
			ShapedRecipeBuilder.shapedRecipe(rod, 4)
				.patternLine("i")
				.patternLine("i")
				.key('i', ingotTag)
				.addCriterion("has_"+ingotMat+"_ingot", hasItem(ingotTag))
				.build(this.consumer, new ResourceLocation(IPOMod.ID, "has_"+stickMat+"_rod"));
		
		ShapedRecipeBuilder.shapedRecipe(fence, 3)
			.patternLine("isi")
			.patternLine("isi")
			.key('i', ingotTag)
			.key('s', stickTag)
			.addCriterion("has_"+stickMat+"_rod", hasItem(stickTag))
			.addCriterion("has_"+ingotMat+"_ingot", hasItem(ingotTag))
			.build(this.consumer);
	}
}
