package twistedgate.immersiveposts.enums;

import blusunrize.immersiveengineering.common.blocks.IEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.common.blocks.BlockPost;

/**
 * @author TwistedGate
 */
public enum EnumPostMaterial implements IStringSerializable{
	
	// Default
	WOOD("woodpost", IEBlocks.WoodenDecoration.treatedFence),
	ALUMINIUM("aluminiumpost", IEBlocks.MetalDecoration.aluFence),
	STEEL("steelpost", IEBlocks.MetalDecoration.steelFence),
	
	// Custom
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
	CONCRETE("concretepost", IEBlocks.toSlab.get(IEBlocks.StoneDecoration.concrete)),
	CONCRETE_LEADED("leadedconcretepost", IEBlocks.toSlab.get(IEBlocks.StoneDecoration.concreteLeaded))
	;
	
	private final String name;
	private final Block block;
	private final boolean isFence;
	private EnumPostMaterial(String name, Block block){
		this.name=name;
		this.block=block;
		this.isFence=(block instanceof FenceBlock);
	}
	
	/** Source-block itemstack */
	public ItemStack getItemStack(){
		return new ItemStack(this.block.asItem(), 1);
	}
	
	public ResourceLocation getTexture(){
		return new ResourceLocation(IPOMod.ID, "posts/post_"+this.toString().toLowerCase()+".png");
	}
	
	/** Source-block*/
	public Block getBlock(){
		return this.block;
	}
	
	/** Is the source-block a fence or a different type of block. Used for manual. */
	public boolean isFence(){
		return this.isFence;
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
		Block block=null;
		switch(getFrom(stack)){
			case ALUMINIUM:		 block=IPOStuff.aluminiumPost;break;
			case CONSTANTAN:	 block=IPOStuff.constantanPost;break;
			case COPPER:		 block=IPOStuff.copperPost;break;
			case ELECTRUM:		 block=IPOStuff.electrumPost;break;
			case GOLD:			 block=IPOStuff.goldPost;break;
			case IRON:			 block=IPOStuff.ironPost;break;
			case LEAD:			 block=IPOStuff.leadPost;break;
			case NETHERBRICK:	 block=IPOStuff.netherPost;break;
			case NICKEL:		 block=IPOStuff.nickelPost;break;
			case SILVER:		 block=IPOStuff.silverPost;break;
			case STEEL:			 block=IPOStuff.steelPost;break;
			case URANIUM:		 block=IPOStuff.uraniumPost;break;
			case WOOD:			 block=IPOStuff.woodPost;break;
			case CONCRETE:		 block=IPOStuff.concretePost;break;
			case CONCRETE_LEADED:block=IPOStuff.leadedConcretePost;break;
		}
		
		return block!=null?block.getDefaultState().with(BlockPost.TYPE, EnumPostType.POST_TOP):null;
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
