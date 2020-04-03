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
		
		fenceAndStickRecipe(IPOStuff.fence_Iron, null, "iron");
		fenceAndStickRecipe(IPOStuff.fence_Gold, IPOStuff.rod_Gold, "gold");
		fenceAndStickRecipe(IPOStuff.fence_Copper, IPOStuff.rod_Copper, "copper");
		fenceAndStickRecipe(IPOStuff.fence_Lead, IPOStuff.rod_Lead, "lead");
		fenceAndStickRecipe(IPOStuff.fence_Silver, IPOStuff.rod_Silver, "silver");
		fenceAndStickRecipe(IPOStuff.fence_Nickel, IPOStuff.rod_Nickel, "nickel");
		fenceAndStickRecipe(IPOStuff.fence_Constantan, IPOStuff.rod_Constantan, "constantan");
		fenceAndStickRecipe(IPOStuff.fence_Electrum, IPOStuff.rod_Electrum, "electrum");
		fenceAndStickRecipe(IPOStuff.fence_Uranium, IPOStuff.rod_Uranium, "uranium");
	}
	
	/** Creates both a recipe for fences and the stick needed */
	private void fenceAndStickRecipe(FenceBlock fence, Item rod, String material){
		Tag<Item> stickTag=new ItemTags.Wrapper(new ResourceLocation("forge", "rod/"+material));
		Tag<Item> ingotTag=new ItemTags.Wrapper(new ResourceLocation("forge", "ingot/"+material));
		
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
				.build(this.consumer, new ResourceLocation(IPOMod.ID, "has_"+ingotMat+"_rod"));
		
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
