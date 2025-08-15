package twistedgate.immersiveposts.common.data.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;
import twistedgate.immersiveposts.common.IPOContent;
import twistedgate.immersiveposts.common.blocks.PostBlock;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.util.loot.BaseCoverDropLootEntry;
import twistedgate.immersiveposts.util.loot.PostMaterialDropLootEntry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class IPOBlockLoot extends LootTableProvider{
	
	public IPOBlockLoot(PackOutput output){
		super(output, Set.of(), List.of());
	}
	
	/* // Why in the world is it a Final method now?!
	@Override
	public String getName(){
		return "IPOLootTables";
	}
	*/
	
	@Override
	public List<SubProviderEntry> getTables(){
		return List.of(new SubProviderEntry(LootyLoot::new, LootContextParamSets.BLOCK));
	}
	
	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationcontext){
		map.forEach((id, table) -> {
			table.validate(validationcontext);
		});
	}
	
	public static class LootyLoot implements LootTableSubProvider{
		private final Set<ResourceLocation> generatedTables = new HashSet<>();
		private BiConsumer<ResourceLocation, LootTable.Builder> out;
		
		@Override
		public void generate(BiConsumer<ResourceLocation, Builder> t){
			this.out = t;
			
			// Fences
			for(RegistryObject<FenceBlock> b: IPOContent.Blocks.Fences.ALL_FENCES){
				registerSelfDropping(b);
			}
			
			// Posts
			for(EnumPostMaterial mat: EnumPostMaterial.values()){
				RegistryObject<PostBlock> b = IPOContent.Blocks.Posts.getRegObject(mat);
				register(b, createPoolBuilder().add(PostMaterialDropLootEntry.builder()));
			}
			
			registerSelfDropping(IPOContent.Blocks.POST_BASE, createPoolBuilder().add(BaseCoverDropLootEntry.builder()));
		}
		
		private <B extends Block> void registerSelfDropping(RegistryObject<B> b, LootPool.Builder... pool){
			LootPool.Builder[] withSelf = Arrays.copyOf(pool, pool.length + 1);
			withSelf[withSelf.length - 1] = singleItem(b.get());
			register(b, withSelf);
		}
		
		private LootPool.Builder singleItem(ItemLike in){
			return createPoolBuilder().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(in));
		}
		
		private <B extends Block> void register(RegistryObject<B> b, LootPool.Builder... pools){
			LootTable.Builder builder = LootTable.lootTable();
			for(LootPool.Builder pool: pools)
				builder.withPool(pool);
			register(b, builder);
		}
		
		private <B extends Block> void register(RegistryObject<B> b, LootTable.Builder table){
			register(b.getId(), table);
		}
		
		private void register(ResourceLocation name, LootTable.Builder table){
			ResourceLocation loc = toTableLoc(name);
			if(!generatedTables.add(loc))
				throw new IllegalStateException("Duplicate loot table " + name);
			out.accept(loc, table.setParamSet(LootContextParamSets.BLOCK));
		}
		
		private LootPool.Builder createPoolBuilder(){
			return LootPool.lootPool().when(ExplosionCondition.survivesExplosion());
		}
		
		private ResourceLocation toTableLoc(ResourceLocation in){
			return new ResourceLocation(in.getNamespace(), "blocks/" + in.getPath());
		}
	}
}
