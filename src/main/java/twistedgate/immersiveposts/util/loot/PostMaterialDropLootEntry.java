package twistedgate.immersiveposts.util.loot;

import java.util.function.Consumer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.blocks.PostBlock;

public class PostMaterialDropLootEntry extends LootPoolSingletonContainer{
	public static final ResourceLocation ID = new ResourceLocation(IPOMod.ID, "post_material_drop");
	
	protected PostMaterialDropLootEntry(int weightIn, int qualityIn, LootItemCondition[] conditionsIn, LootItemFunction[] functionsIn){
		super(weightIn, qualityIn, conditionsIn, functionsIn);
	}
	
	@Override
	protected void createItemStack(@NotNull Consumer<ItemStack> stackConsumer, LootContext context){
		if(context.hasParam(LootContextParams.BLOCK_STATE)){
			BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
			if(state != null && state.hasProperty(PostBlock.TYPE) && state.getValue(PostBlock.TYPE).id() < 2){
				ItemStack stack = ((PostBlock) state.getBlock()).getPostMaterial().getItemStack();
				stackConsumer.accept(stack);
			}
		}
	}
	
	@Override
	public @NotNull LootPoolEntryType getType(){
		return IPOLootFunctions.postDrop;
	}
	
	public static LootPoolSingletonContainer.Builder<?> builder(){
		return simpleBuilder(PostMaterialDropLootEntry::new);
	}
	
	public static class Serializer extends LootPoolSingletonContainer.Serializer<PostMaterialDropLootEntry>{
		@Override
		protected @NotNull PostMaterialDropLootEntry deserialize(@NotNull JsonObject object, @NotNull JsonDeserializationContext context, int weight, int quality, LootItemCondition @NotNull [] conditions, LootItemFunction @NotNull [] functions){
			return new PostMaterialDropLootEntry(weight, quality, conditions, functions);
		}
	}
}
