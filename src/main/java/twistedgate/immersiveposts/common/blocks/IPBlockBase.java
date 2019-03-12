package twistedgate.immersiveposts.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.IPStuff;
import twistedgate.immersiveposts.ImmersivePosts;
import twistedgate.immersiveposts.ModInfo;

public class IPBlockBase extends Block{
	public IPBlockBase(Material material, String name){
		super(material);
		setRegistryName(new ResourceLocation(ModInfo.ID, name));
		setTranslationKey(ModInfo.ID+"."+name);
		setCreativeTab(ImmersivePosts.ipCreativeTab);
		
		IPStuff.BLOCKS.add(this);
	}
	
	public boolean hasItem(){
		return false;
	}
}
