package twistedgate.immersiveposts.common;

import blusunrize.immersiveengineering.api.Lib;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ExternalModContent{
	public static RegistryObject<Block> IE_TREATED_FENCE = RegistryObject.of(new ResourceLocation(Lib.MODID, "treated_fence"), ForgeRegistries.BLOCKS);
	public static RegistryObject<Block> IE_ALUMINIUM_FENCE = RegistryObject.of(new ResourceLocation(Lib.MODID, "alu_fence"), ForgeRegistries.BLOCKS);
	public static RegistryObject<Block> IE_STEEL_FENCE = RegistryObject.of(new ResourceLocation(Lib.MODID, "steel_fence"), ForgeRegistries.BLOCKS);
	
	public static final void init(){
//		IE_TREATED_FENCE = RegistryObject.of(new ResourceLocation(Lib.MODID, "treated_fence"), ForgeRegistries.BLOCKS);
//		IE_ALUMINIUM_FENCE = RegistryObject.of(new ResourceLocation(Lib.MODID, "alu_fence"), ForgeRegistries.BLOCKS);
//		IE_STEEL_FENCE = RegistryObject.of(new ResourceLocation(Lib.MODID, "steel_fence"), ForgeRegistries.BLOCKS);
	}
}
