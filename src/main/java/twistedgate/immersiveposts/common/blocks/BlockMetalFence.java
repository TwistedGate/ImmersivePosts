package twistedgate.immersiveposts.common.blocks;

import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.ImmersivePosts;

public class BlockMetalFence extends BlockFence{
	public final String rawName;
	public BlockMetalFence(String name){
		this(name, Material.IRON);
	}
	private BlockMetalFence(String name, Material mat){
		super(mat, mat.getMaterialMapColor());
		this.rawName=name;
		setRegistryName(new ResourceLocation(IPOMod.ID, name));
		setTranslationKey(IPOMod.ID+"."+name);
		setHardness(5.0F);
		setResistance(10.0F);
		setCreativeTab(ImmersivePosts.creativeTab);
		
		IPOStuff.BLOCKS.add(this);
		IPOStuff.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
}
