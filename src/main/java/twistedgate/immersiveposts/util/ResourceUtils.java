package twistedgate.immersiveposts.util;

import blusunrize.immersiveengineering.api.Lib;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;
import twistedgate.immersiveposts.api.IPOMod;

public class ResourceUtils{
	
	public static ResourceLocation ipo(String path){
		return ResourceLocation.fromNamespaceAndPath(IPOMod.ID, path);
	}
	
	public static ResourceLocation ie(String path){
		return ResourceLocation.fromNamespaceAndPath(Lib.MODID, path);
	}
	
	public static ResourceLocation forge(String path){
		return ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, path);
	}
	
	public static ResourceLocation mc(String path){
		return ResourceLocation.withDefaultNamespace(path);
	}
}
