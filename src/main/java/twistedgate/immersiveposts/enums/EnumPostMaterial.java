package twistedgate.immersiveposts.enums;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDecoration;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.common.blocks.BlockPost;

/**
 * @author TwistedGate
 */
public enum EnumPostMaterial implements IStringSerializable{
	WOOD("woodpost", IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.FENCE.getMeta()),
	NETHERBRICK("netherpost", Blocks.NETHER_BRICK_FENCE),
	IRON("ironpost", IPOStuff.ironFence),
	GOLD("goldpost", IPOStuff.goldFence),
	COPPER("copperpost", IPOStuff.copperFence),
	LEAD("leadpost", IPOStuff.leadFence),
	SILVER("silverpost", IPOStuff.silverFence),
	NICKEL("nickelpost", IPOStuff.nickelFence),
	CONSTANTAN("constantanpost", IPOStuff.constantanFence),
	ELECTRUM("electrumpost", IPOStuff.electrumFence),
	URANIUM("uraniumpost", IPOStuff.uraniumFence),
//	DIAMOND("diamondpost", IPOStuff.diamondFence),
	ALUMINIUM("aluminiumpost", IEContent.blockMetalDecoration1, BlockTypes_MetalDecoration1.ALUMINUM_FENCE.getMeta()),
	STEEL("steelpost", IEContent.blockMetalDecoration1, BlockTypes_MetalDecoration1.STEEL_FENCE.getMeta()),
	;
	
	private final String name;
	private final Block fenceBlock;
	private final int fenceMeta;
	private EnumPostMaterial(String name, Block fenceBlock){this(name, fenceBlock, 0);}
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
	
	public static IBlockState getPostStateFrom(ItemStack stack){
		IBlockState state=null;
		switch(getFrom(stack)){
			case ALUMINIUM:	 state=IPOStuff.aluPost.getDefaultState();break;
			case CONSTANTAN: state=IPOStuff.constantanPost.getDefaultState();break;
			case COPPER:	 state=IPOStuff.copperPost.getDefaultState();break;
			case ELECTRUM:	 state=IPOStuff.electrumPost.getDefaultState();break;
			case GOLD:		 state=IPOStuff.goldPost.getDefaultState();break;
			case IRON:		 state=IPOStuff.ironPost.getDefaultState();break;
			case LEAD:		 state=IPOStuff.leadPost.getDefaultState();break;
			case NETHERBRICK:state=IPOStuff.netherPost.getDefaultState();break;
			case NICKEL:	 state=IPOStuff.nickelPost.getDefaultState();break;
			case SILVER:	 state=IPOStuff.silverPost.getDefaultState();break;
			case STEEL:		 state=IPOStuff.steelPost.getDefaultState();break;
			case URANIUM:	 state=IPOStuff.uraniumPost.getDefaultState();break;
			case WOOD:		 state=IPOStuff.woodPost.getDefaultState();break;
		}
		
		return state!=null?state.withProperty(BlockPost.TYPE, EnumPostType.POST_TOP):null;
	}
	
	public static EnumPostMaterial getFrom(ItemStack stack){
		for(EnumPostMaterial mat:values())
			if(stack.isItemEqual(mat.getFenceItem())) return mat;
		
		return null;
	}
	
	public static boolean isFenceItem(ItemStack stack){
		if(stack==null || stack.isEmpty()) return false;
		
		for(EnumPostMaterial mat:values())
			if(stack.isItemEqual(mat.getFenceItem())) return true;
		
		return false;
	}
}
