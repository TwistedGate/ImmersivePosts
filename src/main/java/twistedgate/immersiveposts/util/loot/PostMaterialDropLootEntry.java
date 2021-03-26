package twistedgate.immersiveposts.util.loot;

import java.util.function.Consumer;

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
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.common.IPOMod;
import twistedgate.immersiveposts.common.blocks.BlockPost;

public class PostMaterialDropLootEntry extends StandaloneLootEntry{
	public static final ResourceLocation ID = new ResourceLocation(IPOMod.ID, "post_material_drop");
	
	protected PostMaterialDropLootEntry(int weightIn, int qualityIn, ILootCondition[] conditionsIn, ILootFunction[] functionsIn){
		super(weightIn, qualityIn, conditionsIn, functionsIn);
	}
	
	@Override
	protected void func_216154_a(Consumer<ItemStack> stackConsumer, LootContext context){
		if(context.has(LootParameters.BLOCK_STATE)){
			BlockState state = context.get(LootParameters.BLOCK_STATE);
			if(state.hasProperty(BlockPost.TYPE) && state.get(BlockPost.TYPE).id() < 2){
				ItemStack stack = ((BlockPost)state.getBlock()).getPostMaterial().getItemStack();
				stackConsumer.accept(stack);
			}
		}
	}
	
	@Override
	public LootPoolEntryType func_230420_a_(){
		return IPOLootFunctions.postDrop;
	}
	
	public static StandaloneLootEntry.Builder<?> builder(){
		return builder(PostMaterialDropLootEntry::new);
	}
	
	
	public static class Serializer extends StandaloneLootEntry.Serializer<PostMaterialDropLootEntry>{
		@Override
		protected PostMaterialDropLootEntry deserialize(JsonObject object, JsonDeserializationContext context, int weight, int quality, ILootCondition[] conditions, ILootFunction[] functions){
			return new PostMaterialDropLootEntry(weight, quality, conditions, functions);
		}
	}
}
