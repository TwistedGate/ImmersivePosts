package twistedgate.immersiveposts.utils.enums;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDecoration;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

/**
 * Who knows, maybe i'll trying add custom ones too, like gold or iron
 * @author TwistedGate
 */
public enum EnumPostMaterial implements IStringSerializable{
	WOOD("woodpost", IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.FENCE.getMeta()),
	ALUMINIUM("aluminiumpost", IEContent.blockMetalDecoration1, BlockTypes_MetalDecoration1.ALUMINUM_FENCE.getMeta()),
	STEEL("steelpost", IEContent.blockMetalDecoration1, BlockTypes_MetalDecoration1.STEEL_FENCE.getMeta())
	;
	
	private final String name;
	private final Block fenceBlock;
	private final int fenceMeta;
	private EnumPostMaterial(String name, Block fenceBlock, int fenceMeta){
		this.name=name;
		this.fenceBlock=fenceBlock;
		this.fenceMeta=fenceMeta;
	}
	
	public ItemStack getFenceItem(){
		return new ItemStack(this.fenceBlock, 1, this.fenceMeta);
	}
	
	public Block getFenceBlock(){
		return this.fenceBlock;
	}
	
	public int getFenceMeta(){
		return this.fenceMeta;
	}
	
	@Override
	public String getName(){
		return this.name;
	}
	
	public static boolean isFenceItem(ItemStack stack){
		if(stack==null || stack.isEmpty()) return false;
		
		for(EnumPostMaterial mat:values())
			if(stack.isItemEqual(mat.getFenceItem())) return true;
		
		return false;
	}
}
