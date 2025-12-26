package twistedgate.immersiveposts.util.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import twistedgate.immersiveposts.common.blocks.PostBlock;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

public class PostMaterialDropLootEntry extends LootPoolSingletonContainer{
	
	public static final MapCodec<PostMaterialDropLootEntry> CODEC = RecordCodecBuilder.mapCodec(inst -> singletonFields(inst).apply(inst, PostMaterialDropLootEntry::new));
	
	protected PostMaterialDropLootEntry(int weightIn, int qualityIn, List<LootItemCondition> conditionsIn, List<LootItemFunction> functionsIn){
		super(weightIn, qualityIn, conditionsIn, functionsIn);
	}
	
	@Override
	protected void createItemStack(@Nonnull Consumer<ItemStack> stackConsumer, LootContext context){
		if(context.hasParam(LootContextParams.BLOCK_STATE)){
			BlockState state = context.getParam(LootContextParams.BLOCK_STATE);
			
			if(state.hasProperty(PostBlock.TYPE) && state.getValue(PostBlock.TYPE).id() < 2){
				ItemStack stack = ((PostBlock) state.getBlock()).getPostMaterial().getItemStack();
				stackConsumer.accept(stack);
			}
		}
	}
	
	@Nonnull
	@Override
	public LootPoolEntryType getType(){
		return IPOLootFunctions.postDrop.value();
	}
	
	public static LootPoolSingletonContainer.Builder<?> builder(){
		return simpleBuilder(PostMaterialDropLootEntry::new);
	}
	
}
