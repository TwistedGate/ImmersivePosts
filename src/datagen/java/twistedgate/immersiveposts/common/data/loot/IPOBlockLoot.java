package twistedgate.immersiveposts.common.data.loot;

import java.util.Arrays;

import blusunrize.immersiveengineering.data.loot.LootGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IDataProvider;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.IPOContent;
import twistedgate.immersiveposts.common.blocks.BlockPost;
import twistedgate.immersiveposts.util.loot.BaseCoverDropLootEntry;
import twistedgate.immersiveposts.util.loot.PostMaterialDropLootEntry;

public class IPOBlockLoot extends LootGenerator implements IDataProvider{
	public IPOBlockLoot(DataGenerator gen){
		super(gen);
	}
	
	@Override
	public String getName(){
		return "IPOLootTables";
	}
	
	@Override
	protected void registerTables(){
		
		// Fences
		for(FenceBlock b:IPOContent.Blocks.Fences.ALL){
			registerSelfDropping(b);
		}
		
		// Posts
		for(BlockPost b:IPOContent.Blocks.Posts.ALL){
			register(b, createPoolBuilder().addEntry(PostMaterialDropLootEntry.builder()));
		}
		
		registerSelfDropping(IPOContent.Blocks.post_Base, createPoolBuilder().addEntry(BaseCoverDropLootEntry.builder()));
	}
	
	private void registerSelfDropping(Block b, LootPool.Builder... pool){
		LootPool.Builder[] withSelf = Arrays.copyOf(pool, pool.length + 1);
		withSelf[withSelf.length - 1] = singleItem(b);
		register(b, withSelf);
	}
	
	private LootPool.Builder singleItem(IItemProvider in){
		return createPoolBuilder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(in));
	}
	
	private void register(Block b, LootPool.Builder... pools){
		LootTable.Builder builder = LootTable.builder();
		for(LootPool.Builder pool:pools)
			builder.addLootPool(pool);
		register(b, builder);
	}
	
	private void register(Block b, LootTable.Builder table){
		register(b.getRegistryName(), table);
	}
	
	private void register(ResourceLocation name, LootTable.Builder table){
		if(tables.put(toTableLoc(name), table.setParameterSet(LootParameterSets.BLOCK).build()) != null)
			throw new IllegalStateException("Duplicate loot table " + name);
	}
	
	private LootPool.Builder createPoolBuilder(){
		return LootPool.builder().acceptCondition(SurvivesExplosion.builder());
	}
	
	private ResourceLocation toTableLoc(ResourceLocation in){
		return new ResourceLocation(in.getNamespace(), "blocks/" + in.getPath());
	}
}
