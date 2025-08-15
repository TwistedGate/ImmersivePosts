package twistedgate.immersiveposts.util.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import twistedgate.immersiveposts.common.blocks.PostBlock;

import java.util.function.Consumer;

public class PostMaterialDropLootEntry extends LootPoolSingletonContainer{
	protected PostMaterialDropLootEntry(int weightIn, int qualityIn, LootItemCondition[] conditionsIn, LootItemFunction[] functionsIn){
		super(weightIn, qualityIn, conditionsIn, functionsIn);
	}
	
	@Override
	protected void createItemStack(Consumer<ItemStack> stackConsumer, LootContext context){
		if(context.hasParam(LootContextParams.BLOCK_STATE)){
			BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
			if(state.hasProperty(PostBlock.TYPE) && state.getValue(PostBlock.TYPE).id() < 2){
				ItemStack stack = ((PostBlock) state.getBlock()).getPostMaterial().getItemStack();
				stackConsumer.accept(stack);
			}
		}
	}
	
	@Override
	public LootPoolEntryType getType(){
		return IPOLootFunctions.postDrop.get();
	}
	
	public static LootPoolSingletonContainer.Builder<?> builder(){
		return simpleBuilder(PostMaterialDropLootEntry::new);
	}
	
	public static class Serializer extends LootPoolSingletonContainer.Serializer<PostMaterialDropLootEntry>{
		@Override
		protected PostMaterialDropLootEntry deserialize(JsonObject object, JsonDeserializationContext context, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions){
			return new PostMaterialDropLootEntry(weight, quality, conditions, functions);
		}
	}
}
