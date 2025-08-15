package twistedgate.immersiveposts.util.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import twistedgate.immersiveposts.common.blocks.PostBaseBlock;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class BaseCoverDropLootEntry extends LootPoolSingletonContainer{
	protected BaseCoverDropLootEntry(int weightIn, int qualityIn, LootItemCondition[] conditionsIn, LootItemFunction[] functionsIn){
		super(weightIn, qualityIn, conditionsIn, functionsIn);
	}
	
	@Override
	protected void createItemStack(Consumer<ItemStack> stackConsumer, LootContext context){
		if(context.hasParam(LootContextParams.BLOCK_STATE) && context.hasParam(LootContextParams.BLOCK_ENTITY)){
			BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
			
			if(state.hasProperty(PostBaseBlock.HIDDEN) && state.getValue(PostBaseBlock.HIDDEN)){
				BlockEntity te = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);
				if(te instanceof PostBaseTileEntity){
					ItemStack teStack = ((PostBaseTileEntity) te).getStack();
					stackConsumer.accept(teStack);
				}
			}
		}
	}
	
	@Override
	public LootPoolEntryType getType(){
		return IPOLootFunctions.baseCoverDrop.get();
	}
	
	public static LootPoolSingletonContainer.Builder<?> builder(){
		return simpleBuilder(BaseCoverDropLootEntry::new);
	}
	
	public static class Serializer extends LootPoolSingletonContainer.Serializer<BaseCoverDropLootEntry>{
		@Nonnull
		@Override
		protected BaseCoverDropLootEntry deserialize(JsonObject object, JsonDeserializationContext context, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions){
			return new BaseCoverDropLootEntry(weight, quality, conditions, functions);
		}
	}
}
