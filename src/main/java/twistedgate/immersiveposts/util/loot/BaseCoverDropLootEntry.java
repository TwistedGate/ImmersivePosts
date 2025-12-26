package twistedgate.immersiveposts.util.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import java.util.List;
import java.util.function.Consumer;

public class BaseCoverDropLootEntry extends LootPoolSingletonContainer{
	
	public static final MapCodec<BaseCoverDropLootEntry> CODEC = RecordCodecBuilder.mapCodec(inst -> singletonFields(inst).apply(inst, BaseCoverDropLootEntry::new));
	
	protected BaseCoverDropLootEntry(int weightIn, int qualityIn, List<LootItemCondition> conditionsIn, List<LootItemFunction> functionsIn){
		super(weightIn, qualityIn, conditionsIn, functionsIn);
	}
	
	@Override
	protected void createItemStack(@Nonnull Consumer<ItemStack> stackConsumer, LootContext context){
		if(context.hasParam(LootContextParams.BLOCK_STATE) && context.hasParam(LootContextParams.BLOCK_ENTITY)){
			BlockState state = context.getParam(LootContextParams.BLOCK_STATE);
			
			if(state.hasProperty(PostBaseBlock.HIDDEN) && state.getValue(PostBaseBlock.HIDDEN)){
				BlockEntity te = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);
				if(te instanceof PostBaseTileEntity){
					ItemStack teStack = ((PostBaseTileEntity) te).getStack();
					stackConsumer.accept(teStack);
				}
			}
		}
	}
	
	@Nonnull
	@Override
	public LootPoolEntryType getType(){
		return IPOLootFunctions.baseCoverDrop.value();
	}
	
	public static LootPoolSingletonContainer.Builder<?> builder(){
		return simpleBuilder(BaseCoverDropLootEntry::new);
	}
}
