package twistedgate.immersiveposts.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.ImmersivePosts;

/**
 * @author TwistedGate
 */
public class IPOBlockBase extends Block{
	public IPOBlockBase(Material material, String name){
		super(material);
		setRegistryName(new ResourceLocation(IPOMod.ID, name));
		setTranslationKey(IPOMod.ID+"."+name);
		setCreativeTab(ImmersivePosts.creativeTab);
		
		IPOStuff.BLOCKS.add(this);
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis){
		return false; // So far nothing needs it, and it'll only be a nuisance
	}
	
	/** convenience method */
	public static Block getBlockFrom(IBlockAccess world, BlockPos pos){
		if(world==null || pos==null) return null;
		
		return world.getBlockState(pos).getBlock();
	}
}
