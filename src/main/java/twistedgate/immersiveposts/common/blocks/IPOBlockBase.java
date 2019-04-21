package twistedgate.immersiveposts.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.ImmersivePosts;

public class IPOBlockBase extends Block{
	public IPOBlockBase(Material material, String name){
		super(material);
		setRegistryName(new ResourceLocation(IPOMod.ID, name));
		setTranslationKey(IPOMod.ID+"."+name);
		setCreativeTab(ImmersivePosts.ipCreativeTab);
		
		IPOStuff.BLOCKS.add(this);
	}
	
	public boolean hasItem(){
		return false;
	}
}
