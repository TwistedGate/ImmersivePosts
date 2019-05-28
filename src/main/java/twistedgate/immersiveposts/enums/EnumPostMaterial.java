package twistedgate.immersiveposts.enums;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
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
	WOOD("woodpost", IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.FENCE.getMeta(), true, true),
	NETHERBRICK("netherpost", Blocks.NETHER_BRICK_FENCE, true, false),
	IRON("ironpost", IPOStuff.ironFence, true, true),
	GOLD("goldpost", IPOStuff.goldFence, true, true),
	COPPER("copperpost", IPOStuff.copperFence, true, true),
	LEAD("leadpost", IPOStuff.leadFence, true, true),
	SILVER("silverpost", IPOStuff.silverFence, true, true),
	NICKEL("nickelpost", IPOStuff.nickelFence, true, true),
	CONSTANTAN("constantanpost", IPOStuff.constantanFence, true, true),
	ELECTRUM("electrumpost", IPOStuff.electrumFence, true, true),
	URANIUM("uraniumpost", IPOStuff.uraniumFence, true, true),
	ALUMINIUM("aluminiumpost", IEContent.blockMetalDecoration1, BlockTypes_MetalDecoration1.ALUMINUM_FENCE.getMeta(), true, true),
	STEEL("steelpost", IEContent.blockMetalDecoration1, BlockTypes_MetalDecoration1.STEEL_FENCE.getMeta(), true, true),
	CONCRETE("concretepost", IEContent.blockStoneDecorationSlabs, BlockTypes_StoneDecoration.CONCRETE.getMeta(), false, false),
	CONCRETE_LEADED("leadedconcretepost", IEContent.blockStoneDecorationSlabs, BlockTypes_StoneDecoration.CONCRETE_LEADED.getMeta(), false, false)
	; // This is getting bigger and bigger..
	
	private final String name;
	private final Block block;
	private final int meta;
	private final boolean isFence;
	private final boolean needsMetalPress;
	private EnumPostMaterial(String name, Block block, boolean isFence, boolean needsMetalPress){
		this(name, block, 0, isFence, needsMetalPress);
	}
	private EnumPostMaterial(String name, Block block, int metadata, boolean isFence, boolean needsMetalPress){
		this.name=name;
		this.block=block;
		this.meta=metadata;
		this.isFence=isFence;
		this.needsMetalPress=needsMetalPress;
	}
	
	/** Source-block itemstack */
	public ItemStack getItemStack(){
		return new ItemStack(this.block, 1, this.meta);
	}
	
	/** Source-block*/
	public Block getBlock(){
		return this.block;
	}
	
	/** Source-block metadata */
	public int getBlockMeta(){
		return this.meta;
	}
	
	/** Is the source-block a fence or a different type of block. Used for manual. */
	public boolean isFence(){
		return this.isFence;
	}
	
	/** Used to decided if it has a rod that can be made in the Metal Press */
	public boolean needsMetalPress(){
		return this.needsMetalPress;
	}
	
	@Override
	public String getName(){
		return this.name;
	}
	
	
	public static IBlockState getPostStateFrom(ItemStack stack){
		IBlockState state=null;
		switch(getFrom(stack)){
			case ALUMINIUM:		 state=IPOStuff.aluPost.getDefaultState();break;
			case CONSTANTAN:	 state=IPOStuff.constantanPost.getDefaultState();break;
			case COPPER:		 state=IPOStuff.copperPost.getDefaultState();break;
			case ELECTRUM:		 state=IPOStuff.electrumPost.getDefaultState();break;
			case GOLD:			 state=IPOStuff.goldPost.getDefaultState();break;
			case IRON:			 state=IPOStuff.ironPost.getDefaultState();break;
			case LEAD:			 state=IPOStuff.leadPost.getDefaultState();break;
			case NETHERBRICK:	 state=IPOStuff.netherPost.getDefaultState();break;
			case NICKEL:		 state=IPOStuff.nickelPost.getDefaultState();break;
			case SILVER:		 state=IPOStuff.silverPost.getDefaultState();break;
			case STEEL:			 state=IPOStuff.steelPost.getDefaultState();break;
			case URANIUM:		 state=IPOStuff.uraniumPost.getDefaultState();break;
			case WOOD:			 state=IPOStuff.woodPost.getDefaultState();break;
			case CONCRETE:		 state=IPOStuff.concretePost.getDefaultState();break;
			case CONCRETE_LEADED:state=IPOStuff.leadedConcretePost.getDefaultState();break;
		}
		
		return state!=null?state.withProperty(BlockPost.TYPE, EnumPostType.POST_TOP):null;
	}
	
	public static EnumPostMaterial getFrom(ItemStack stack){
		for(EnumPostMaterial mat:values())
			if(stack.isItemEqual(mat.getItemStack())) return mat;
		
		return null;
	}
	
	public static boolean isFenceItem(ItemStack stack){
		if(stack==null || stack.isEmpty()) return false;
		
		for(EnumPostMaterial mat:values())
			if(stack.isItemEqual(mat.getItemStack())) return true;
		
		return false;
	}
}
