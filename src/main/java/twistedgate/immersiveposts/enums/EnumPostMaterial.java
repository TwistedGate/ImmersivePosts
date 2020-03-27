package twistedgate.immersiveposts.enums;

import blusunrize.immersiveengineering.common.blocks.IEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.ToolType;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.common.blocks.BlockPost;

/**
 * @author TwistedGate
 */
public enum EnumPostMaterial implements IStringSerializable{
	
	// Default
	WOOD("woodpost", IEBlocks.WoodenDecoration.treatedFence, true, true),
	ALUMINIUM("aluminiumpost", IEBlocks.MetalDecoration.aluFence, true, true),
	STEEL("steelpost", IEBlocks.MetalDecoration.steelFence, true, true),
	
	// Custom
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
	CONCRETE("concretepost", IEBlocks.toSlab.get(IEBlocks.StoneDecoration.concrete), false, false),
	CONCRETE_LEADED("leadedconcretepost", IEBlocks.toSlab.get(IEBlocks.StoneDecoration.concreteLeaded), false, false)
	;
	
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
		return new ItemStack(this.block.asItem(), 1);
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
	
	
	private static final Material WOOD_LIKE = new Material.Builder(MaterialColor.WOOD)
			.doesNotBlockMovement()
			.notSolid()
			.build();
	
	private static final Material STONE_LIKE = new Material.Builder(MaterialColor.STONE)
			.doesNotBlockMovement()
			.notSolid()
			.build();
	
	private static final Material METAL_LIKE=new Material.Builder(MaterialColor.IRON)
			.doesNotBlockMovement()
			.notSolid()
			.build();
	
	public Block.Properties getProperties(){
		return blockPropertiesFrom(this);
	}
	
	public static Block.Properties blockPropertiesFrom(EnumPostMaterial postMaterial){
		Block.Properties prop=null;
		switch(postMaterial){
			case WOOD: // For the *one* post that wants to be different..
				prop=Block.Properties.create(WOOD_LIKE).harvestTool(ToolType.AXE).harvestLevel(0).sound(SoundType.WOOD); break;
			case NETHERBRICK: case CONCRETE: case CONCRETE_LEADED:
				prop=Block.Properties.create(STONE_LIKE).harvestTool(ToolType.PICKAXE).harvestLevel(1).sound(SoundType.STONE); break;
			default:
				prop=Block.Properties.create(METAL_LIKE).harvestTool(ToolType.PICKAXE).harvestLevel(1).sound(SoundType.METAL);
		}
		
		if(postMaterial==EnumPostMaterial.URANIUM)
			prop.lightValue(8);
		
		prop.hardnessAndResistance(3.0F, 5.0F);
		
		return prop;
	}
	
	public static BlockState getPostStateFrom(ItemStack stack){
		BlockState state=null;
		switch(getFrom(stack)){
			case ALUMINIUM:		 state=IPOStuff.aluminiumPost.getDefaultState();break;
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
		
		return state!=null?state.with(BlockPost.TYPE, EnumPostType.POST_TOP):null;
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
