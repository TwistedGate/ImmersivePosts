package twistedgate.immersiveposts.common.data.loot;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.block.Block;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.IPOContent;

public class BlockLootTables implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
    private final Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();

    private void addTables() {
        // Fences
        fenceTable("fence_iron", IPOContent.Blocks.Fences.iron);
        fenceTable("fence_gold", IPOContent.Blocks.Fences.gold);
        fenceTable("fence_copper", IPOContent.Blocks.Fences.copper);
        fenceTable("fence_lead", IPOContent.Blocks.Fences.lead);
        fenceTable("fence_silver", IPOContent.Blocks.Fences.silver);
        fenceTable("fence_nickel", IPOContent.Blocks.Fences.nickel);
        fenceTable("fence_constantan", IPOContent.Blocks.Fences.constantan);
        fenceTable("fence_electrum", IPOContent.Blocks.Fences.electrum);
        fenceTable("fence_uranium", IPOContent.Blocks.Fences.uranium);
    }

    private void fenceTable(String name, Block block) {
        LootPool.Builder builder = LootPool.builder().name("blocks/" + name).rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block)).acceptCondition(SurvivesExplosion.builder());
        lootTables.put(block.getLootTable(), LootTable.builder().addLootPool(builder));
    }

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        addTables();
        lootTables.forEach(consumer);
    }
}
