package twistedgate.immersiveposts.common.data;

import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.IPOContent.Blocks.Fences;

public class IPOLootTables extends LootTableProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    protected final Map<ResourceLocation, LootTable> lootTables = new HashMap<>();
    private final DataGenerator dataGen; // DataGen in superclass is private. PR to Forge soon...

    public IPOLootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
        this.dataGen = dataGeneratorIn;
    }

    // Register all loot tables here
    private void addTables() {
        // Fences
        fenceTable("fence_iron", Fences.iron);
        fenceTable("fence_gold", Fences.gold);
        fenceTable("fence_copper", Fences.copper);
        fenceTable("fence_lead", Fences.lead);
        fenceTable("fence_silver", Fences.silver);
        fenceTable("fence_nickel", Fences.nickel);
        fenceTable("fence_constantan", Fences.constantan);
        fenceTable("fence_electrum", Fences.electrum);
        fenceTable("fence_uranium", Fences.uranium);
    }

    private void fenceTable(String name, Block block) {
        LootPool.Builder builder = LootPool.builder().name("blocks/" + name).rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block)).acceptCondition(SurvivesExplosion.builder());
        lootTables.put(block.getLootTable(), LootTable.builder().addLootPool(builder).setParameterSet(LootParameterSets.BLOCK).build());
    }

    @Override
    public void act(DirectoryCache cache) {
        addTables();
        ValidationTracker validator = new ValidationTracker(LootParameterSets.GENERIC, p -> null, lootTables::get);
        lootTables.forEach((name, table) -> LootTableManager.validateLootTable(validator, name, table));
        Multimap<String, String> problems = validator.getProblems();
        if(!problems.isEmpty()) {
            problems.forEach((name, table) -> IPODataGen.log.warn("Failed to validate loot table \"{}\": {}", name, table));
            throw new IllegalStateException("Failed to validate loot tables");
        } else {
            Path outputFolder = this.dataGen.getOutputFolder();
            lootTables.forEach((key, table) -> {
                Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
                try {
                    IDataProvider.save(GSON, cache, LootTableManager.toJson(table), path);
                } catch (IOException e) {
                    IPODataGen.log.error("Failed to write loot table {}", path, e);
                }
            });
        }
    }

    @Override
    public String getName() {
        return "IPOLootTables";
    }
}
