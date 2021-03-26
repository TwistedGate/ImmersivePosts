package twistedgate.immersiveposts.util.loot;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootPoolEntryType;
import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.common.IPOMod;
import twistedgate.immersiveposts.common.blocks.BlockPostBase;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;

public class BaseCoverDropLootEntry extends StandaloneLootEntry{
	public static final ResourceLocation ID = new ResourceLocation(IPOMod.ID, "base_cover_drop");
	
	protected BaseCoverDropLootEntry(int weightIn, int qualityIn, ILootCondition[] conditionsIn, ILootFunction[] functionsIn){
		super(weightIn, qualityIn, conditionsIn, functionsIn);
	}
	
	@Override
	protected void func_216154_a(Consumer<ItemStack> stackConsumer, LootContext context){
		if(context.has(LootParameters.BLOCK_STATE) && context.has(LootParameters.BLOCK_ENTITY)){
			BlockState state = context.get(LootParameters.BLOCK_STATE);
			
			if(state.hasProperty(BlockPostBase.HIDDEN) && state.get(BlockPostBase.HIDDEN)){
				TileEntity te = context.get(LootParameters.BLOCK_ENTITY);
				if(te instanceof PostBaseTileEntity){
					ItemStack teStack = ((PostBaseTileEntity) te).getStack();
					stackConsumer.accept(teStack);
				}
			}
		}
	}
	
	@Override
	public LootPoolEntryType func_230420_a_(){
		return IPOLootFunctions.baseCoverDrop;
	}
	
	public static StandaloneLootEntry.Builder<?> builder(){
		return builder(BaseCoverDropLootEntry::new);
	}
	
	
	public static class Serializer extends StandaloneLootEntry.Serializer<BaseCoverDropLootEntry>{
		@Nonnull
		@Override
		protected BaseCoverDropLootEntry deserialize(JsonObject object, JsonDeserializationContext context, int weight, int quality, ILootCondition[] conditions, ILootFunction[] functions){
			return new BaseCoverDropLootEntry(weight, quality, conditions, functions);
		}
	}
}
