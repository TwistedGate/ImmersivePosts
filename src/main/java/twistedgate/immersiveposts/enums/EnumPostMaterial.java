package twistedgate.immersiveposts.enums;

import java.util.function.Supplier;

import blusunrize.immersiveengineering.common.blocks.IEBlocks;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.api.posts.IPostMaterial;
import twistedgate.immersiveposts.common.ExternalModContent;
import twistedgate.immersiveposts.common.IPOContent.Blocks.Fences;

/**
 * @author TwistedGate
 */
public enum EnumPostMaterial implements IPostMaterial{
	
	// Default
	WOOD("wood", PostBlockProperties.WOOD, ExternalModContent.IE_TREATED_FENCE::get),
	ALUMINIUM("aluminium", PostBlockProperties.METAL, ExternalModContent.IE_ALUMINIUM_FENCE::get),
	STEEL("steel", PostBlockProperties.METAL, ExternalModContent.IE_STEEL_FENCE::get),
	
	// Custom
	NETHERBRICK("nether", PostBlockProperties.STONE, Blocks.NETHER_BRICK_FENCE),
	IRON("iron", PostBlockProperties.METAL, Fences.IRON::get),
	GOLD("gold", PostBlockProperties.METAL, Fences.GOLD::get),
	COPPER("copper", PostBlockProperties.METAL, Fences.COPPER::get),
	LEAD("lead", PostBlockProperties.METAL, Fences.LEAD::get),
	SILVER("silver", PostBlockProperties.METAL, Fences.SILVER::get),
	NICKEL("nickel", PostBlockProperties.METAL, Fences.NICKEL::get),
	CONSTANTAN("constantan", PostBlockProperties.METAL, Fences.CONSTANTAN::get),
	ELECTRUM("electrum", PostBlockProperties.METAL, Fences.ELECTRUM::get),
	URANIUM("uranium", PostBlockProperties.metal().setLightLevel(s -> 8), Fences.URANIUM::get),
	CONCRETE("concrete", PostBlockProperties.STONE, () -> IEBlocks.toSlab.get(IEBlocks.StoneDecoration.concrete)),
	CONCRETE_LEADED("leadedconcrete", PostBlockProperties.STONE, () -> IEBlocks.toSlab.get(IEBlocks.StoneDecoration.concreteLeaded))
	;
	
	private boolean isFence;
	private String name;
	private Block sourceBlock;
	private Supplier<Block> sourceBlockSupplier;
	private Properties properties;
	private ResourceLocation texture;
	
	private EnumPostMaterial(String name, Properties properties, Block sourceBlock){
		this(name, properties, null, sourceBlock);
	}
	
	private EnumPostMaterial(String name, Properties properties, Supplier<Block> sourceBlockSupplier){
		this(name, properties, null, sourceBlockSupplier);
	}
	
	private EnumPostMaterial(String name, Properties properties, ResourceLocation texture, Block sourceBlock){
		this.name = name;
		this.properties = properties;
		this.sourceBlock = sourceBlock;
		this.isFence = (sourceBlock instanceof FenceBlock);
	}
	
	private EnumPostMaterial(String name, Properties properties, ResourceLocation texture, Supplier<Block> sourceBlockSupplier){
		this.name = name;
		this.properties = properties;
		this.texture = texture;
		this.sourceBlockSupplier = sourceBlockSupplier;
	}
	
	@Override
	public ItemStack getItemStack(){
		Block block = getSourceBlock();
		return block == null ? ItemStack.EMPTY : new ItemStack(block);
	}
	
	@Override
	public ResourceLocation getTexture(){
		if(this.texture == null){
			return new ResourceLocation(IPOMod.ID, "block/posts/post_" + this.getName());
		}
		return this.texture;
	}
	
	@Override
	public Block getSourceBlock(){
		if(this.sourceBlock == null){
			this.sourceBlock = this.sourceBlockSupplier.get();
			this.isFence = (this.sourceBlock != null && this.sourceBlock instanceof FenceBlock);
		}
		return this.sourceBlock;
	}
	
	@Override
	public boolean isFence(){
		return this.isFence;
	}
	
	@Override
	public String getName(){
		return this.name;
	}
	
	@Override
	public String getBlockName(){
		return this.name + "post";
	}
	
	@Override
	public Properties getBlockProperties(){
		return this.properties;
	}
	
	public static class PostBlockProperties{
		/** Sources are not being read properly for some reason, so this is just so i know what the hell is what */
		private static Material material(MaterialColor color, boolean isLiquid, boolean isSolid, boolean blocksMovement, boolean isOpaque, boolean flammable, boolean replaceable, PushReaction pushReaction){
			return new Material(color, isLiquid, isSolid, blocksMovement, isOpaque, flammable, replaceable, pushReaction);
		}
		
		private static final Material WOOD_LIKE = material(MaterialColor.WOOD, false, true, true, true, false, false, PushReaction.BLOCK);
		private static final Material STONE_LIKE = material(MaterialColor.STONE, false, true, true, true, false, false, PushReaction.BLOCK);
		private static final Material METAL_LIKE = material(MaterialColor.IRON, false, true, true, true, false, false, PushReaction.BLOCK);
		
		public static final Properties WOOD = Properties.create(WOOD_LIKE)
				.sound(SoundType.WOOD)
				.harvestTool(ToolType.AXE)
				.hardnessAndResistance(2.0F, 5.0F)
				.notSolid()
				.setBlocksVision((s, r, p) -> false);
		
		public static final Properties STONE = Properties.create(STONE_LIKE)
				.sound(SoundType.STONE)
				.setRequiresTool()
				.harvestTool(ToolType.PICKAXE)
				.hardnessAndResistance(1.5F, 6.0F)
				.notSolid()
				.setBlocksVision((s, r, p) -> false);
		
		public static final Properties METAL = metal();
		
		private static Properties metal(){
			return Properties.create(METAL_LIKE)
					.sound(SoundType.METAL)
					.setRequiresTool()
					.harvestTool(ToolType.PICKAXE)
					.hardnessAndResistance(3.0F, 15.0F)
					.notSolid()
					.setBlocksVision((s, r, p) -> false);
		}
	}
}
