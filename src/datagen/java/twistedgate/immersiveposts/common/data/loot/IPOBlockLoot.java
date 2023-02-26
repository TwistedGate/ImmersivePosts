package twistedgate.immersiveposts.common.data.loot;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;
import twistedgate.immersiveposts.common.IPOContent;
import twistedgate.immersiveposts.common.blocks.PostBlock;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.util.loot.BaseCoverDropLootEntry;
import twistedgate.immersiveposts.util.loot.PostMaterialDropLootEntry;

public class IPOBlockLoot extends LootTableProvider{
	public IPOBlockLoot(DataGenerator gen){
		super(gen);
	}
	
	@Override
	public String getName(){
		return "IPOLootTables";
	}
	
	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> getTables(){
		return ImmutableList.of(Pair.of(LootyLoot::new, LootContextParamSets.BLOCK));
	}
	
	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker){
		map.forEach((id, table) -> {
			LootTables.validate(validationtracker, id, table);
		});
	}
	
	public class LootyLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>{
		private final Set<ResourceLocation> generatedTables = new HashSet<>();
		private BiConsumer<ResourceLocation, LootTable.Builder> out;
		
		@Override
		public void accept(BiConsumer<ResourceLocation, Builder> t){
			this.out = t;
			
			// Fences
			for(RegistryObject<FenceBlock> b:IPOContent.Blocks.Fences.ALL_FENCES){
				registerSelfDropping(b);
			}
			
			// Posts
			for(EnumPostMaterial mat:EnumPostMaterial.values()){
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
			for(LootPool.Builder pool:pools)
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
