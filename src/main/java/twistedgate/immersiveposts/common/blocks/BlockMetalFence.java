package twistedgate.immersiveposts.common.blocks;

import net.minecraft.block.FenceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;

/**
 * @author TwistedGate
 */
public class BlockMetalFence extends FenceBlock{
	public final String rawName;
	public BlockMetalFence(String name){
		this(name, Material.IRON);
		
	}
	private BlockMetalFence(String name, Material mat){
		super(Properties.create(mat).hardnessAndResistance(3.5F, 15.0F));
		
		this.rawName=name;
		setRegistryName(new ResourceLocation(IPOMod.ID, name));
//		setTranslationKey(IPOMod.ID+"."+name);
//		setHardness(3.0F);
//		setResistance(15.0F);
//		setCreativeTab(ImmersivePosts.creativeTab);
		
		IPOStuff.BLOCKS.add(this);
//		IPOStuff.ITEMS.add(new BlockItem(this).setRegistryName(this.getRegistryName()));
	}
}
