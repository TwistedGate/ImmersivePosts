package twistedgate.immersiveposts.enums;

import java.util.function.Supplier;

import blusunrize.immersiveengineering.common.blocks.IEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import twistedgate.immersiveposts.IPOConfig;
import twistedgate.immersiveposts.IPOContent.Blocks.Fences;
import twistedgate.immersiveposts.IPOContent.Blocks.Posts;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.blocks.BlockPost;

/**
 * @author TwistedGate
 */
public enum EnumPostMaterial implements IStringSerializable{
	
	// Default
	WOOD("woodpost", ()->{return IEBlocks.WoodenDecoration.treatedFence;}),
	ALUMINIUM("aluminiumpost", ()->{return IEBlocks.MetalDecoration.aluFence;}),
	STEEL("steelpost", ()->{return IEBlocks.MetalDecoration.steelFence;}),
	
	// Custom
	NETHERBRICK("netherpost", Blocks.NETHER_BRICK_FENCE),
	IRON("ironpost", Fences.iron),
	GOLD("goldpost", Fences.gold),
	COPPER("copperpost", Fences.copper),
	LEAD("leadpost", Fences.lead),
	SILVER("silverpost", Fences.silver),
	NICKEL("nickelpost", Fences.nickel),
	CONSTANTAN("constantanpost", Fences.constantan),
	ELECTRUM("electrumpost", Fences.electrum),
	URANIUM("uraniumpost", Fences.uranium),
	CONCRETE("concretepost", ()->{return IEBlocks.toSlab.get(IEBlocks.StoneDecoration.concrete);}),
	CONCRETE_LEADED("leadedconcretepost", ()->{return IEBlocks.toSlab.get(IEBlocks.StoneDecoration.concreteLeaded);})
	;
	
	private String name;
	private Block block;
	private Supplier<Block> supplier;
	private boolean isFence;
	private Block.Properties props;
	private EnumPostMaterial(String name, Block block){
		this.name=name;
		this.block=block;
		this.isFence=(block instanceof FenceBlock);
	}
	
	private EnumPostMaterial(String name, Supplier<Block> supplier) {
		this.name=name;
		this.supplier=supplier;
	}
	
	/** Source-block itemstack */
	public ItemStack getItemStack(){
		Block block=getBlock();
		return block == null ? ItemStack.EMPTY : new ItemStack(block);
	}
	
	/** The texture for this material type */
	public ResourceLocation getTexture(){
		return new ResourceLocation(IPOMod.ID, "block/posts/post_"+this.toString().toLowerCase());
	}
	
	/** Source-block */
	public Block getBlock(){
		if(this.block==null){
			this.block=this.supplier.get();
			this.isFence=(this.block!=null && this.block instanceof FenceBlock);
		}
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
	
	
	private static final Material WOOD_LIKE	=new Material(MaterialColor.WOOD, false, true, true, true, true, false, false, PushReaction.BLOCK);
	private static final Material STONE_LIKE=new Material(MaterialColor.STONE, false, true, true, true, false, false, false, PushReaction.BLOCK);
	private static final Material METAL_LIKE=new Material(MaterialColor.IRON, false, true, true, true, false, false, false, PushReaction.BLOCK);
	
	public Block.Properties getProperties(){
		if(this.props==null) this.props=blockPropertiesFrom(this);
		return this.props;
	}
	
	public static Block.Properties blockPropertiesFrom(EnumPostMaterial postMaterial){
		Block.Properties prop=null;
		switch(postMaterial){
			case WOOD: // For the *one* post that wants to be different..
				prop=Block.Properties.create(WOOD_LIKE).sound(SoundType.WOOD).harvestTool(ToolType.AXE).harvestLevel(0).hardnessAndResistance(2.0F, 5.0F); break;
				
			case NETHERBRICK: case CONCRETE: case CONCRETE_LEADED:
				prop=Block.Properties.create(STONE_LIKE).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(1.5F, 6.0F); break;
				
			default:
				prop=Block.Properties.create(METAL_LIKE).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(3.0F, 15.0F);
		}
		
		prop.lightValue(postMaterial==EnumPostMaterial.URANIUM?8:0);
		
		return prop;
	}
	
	public static BlockState getPostStateFrom(ItemStack stack){
		Block block=null;
		switch(getFrom(stack)){
			case ALUMINIUM:		 block=Posts.aluminium;break;
			case CONSTANTAN:	 block=Posts.constantan;break;
			case COPPER:		 block=Posts.copper;break;
			case ELECTRUM:		 block=Posts.electrum;break;
			case GOLD:			 block=Posts.gold;break;
			case IRON:			 block=Posts.iron;break;
			case LEAD:			 block=Posts.lead;break;
			case NETHERBRICK:	 block=Posts.nether;break;
			case NICKEL:		 block=Posts.nickel;break;
			case SILVER:		 block=Posts.silver;break;
			case STEEL:			 block=Posts.steel;break;
			case URANIUM:		 block=Posts.uranium;break;
			case WOOD:			 block=Posts.wood;break;
			case CONCRETE:		 block=Posts.concrete;break;
			case CONCRETE_LEADED:block=Posts.concrete_leaded;break;
		}
		
		return block!=null?block.getDefaultState().with(BlockPost.TYPE, EnumPostType.POST_TOP):null;
	}
	
	public static EnumPostMaterial getFrom(ItemStack stack){
		for(EnumPostMaterial mat:values()){
			if(stack.isItemEqual(mat.getItemStack())) return mat;
		}
		
		return null;
	}
	
	public static boolean isValidItem(ItemStack stack){
		if(stack==null || stack.isEmpty()) return false;
		
		for(EnumPostMaterial mat:values()){
			if(mat.getItemStack()!=ItemStack.EMPTY && stack.isItemEqual(mat.getItemStack()) && IPOConfig.MAIN.isEnabled(mat)){
				return true;
			}
		}
		
		return false;
	}
}
