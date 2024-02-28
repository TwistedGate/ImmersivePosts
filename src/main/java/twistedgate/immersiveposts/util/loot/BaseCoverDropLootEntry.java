package twistedgate.immersiveposts.util.loot;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.blocks.PostBaseBlock;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;

public class BaseCoverDropLootEntry extends LootPoolSingletonContainer{
	public static final ResourceLocation ID = new ResourceLocation(IPOMod.ID, "base_cover_drop");
	
	protected BaseCoverDropLootEntry(int weightIn, int qualityIn, LootItemCondition[] conditionsIn, LootItemFunction[] functionsIn){
		super(weightIn, qualityIn, conditionsIn, functionsIn);
	}
	
	@Override
	protected void createItemStack(@NotNull Consumer<ItemStack> stackConsumer, LootContext context){
		if(context.hasParam(LootContextParams.BLOCK_STATE) && context.hasParam(LootContextParams.BLOCK_ENTITY)){
			BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);

			if(state != null && state.hasProperty(PostBaseBlock.HIDDEN) && state.getValue(PostBaseBlock.HIDDEN)){
				BlockEntity te = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);
				if(te instanceof PostBaseTileEntity){
					ItemStack teStack = ((PostBaseTileEntity) te).getStack();
					stackConsumer.accept(teStack);
				}
			}
		}
	}
	
	@Override
	public @NotNull LootPoolEntryType getType(){
		return IPOLootFunctions.baseCoverDrop;
	}
	
	public static LootPoolSingletonContainer.Builder<?> builder(){
		return simpleBuilder(BaseCoverDropLootEntry::new);
	}
	
	public static class Serializer extends LootPoolSingletonContainer.Serializer<BaseCoverDropLootEntry>{
		@Nonnull
		@Override
		protected BaseCoverDropLootEntry deserialize(@NotNull JsonObject object, @NotNull JsonDeserializationContext context, int weight, int quality, LootItemCondition @NotNull [] conditions, LootItemFunction @NotNull [] functions){
			return new BaseCoverDropLootEntry(weight, quality, conditions, functions);
		}
	}
}
