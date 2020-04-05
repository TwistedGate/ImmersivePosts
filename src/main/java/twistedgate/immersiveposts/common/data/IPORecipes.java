package twistedgate.immersiveposts.common.data;

import java.util.function.Consumer;

import blusunrize.immersiveengineering.api.IETags;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraftforge.common.Tags;
import twistedgate.immersiveposts.IPOStuff;

public class IPORecipes extends RecipeProvider{
	public IPORecipes(DataGenerator generatorIn){
		super(generatorIn);
	}
	
	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer){
		ShapedRecipeBuilder.shapedRecipe(IPOStuff.postBase, 6)
			.key('w', Tags.Items.COBBLESTONE)
			.key('s', Blocks.STONE_BRICKS)
			.patternLine("s s")
			.patternLine("sws")
			.patternLine("sws")
			.addCriterion("has_clay", hasItem(IETags.clay))
			.build(consumer);
	}
}