package twistedgate.immersiveposts.enums;

import java.util.function.Supplier;

import blusunrize.immersiveengineering.common.register.IEBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
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
	URANIUM("uranium", PostBlockProperties.metal().lightLevel(s -> 8), Fences.URANIUM::get),
	CONCRETE("concrete", PostBlockProperties.STONE, () -> IEBlocks.TO_SLAB.get(IEBlocks.StoneDecoration.CONCRETE.getId()).get()),
	CONCRETE_LEADED("leadedconcrete", PostBlockProperties.STONE, () -> IEBlocks.TO_SLAB.get(IEBlocks.StoneDecoration.CONCRETE_LEADED.getId()).get())
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
		private static final Material METAL_LIKE = material(MaterialColor.METAL, false, true, true, true, false, false, PushReaction.BLOCK);
		
		public static final Properties WOOD = Properties.of(WOOD_LIKE)
				.sound(SoundType.WOOD)
				//.harvestTool(ToolType.AXE)
				.strength(2.0F, 5.0F)
				.noOcclusion()
				.isViewBlocking((s, r, p) -> false);
		
		public static final Properties STONE = Properties.of(STONE_LIKE)
				.sound(SoundType.STONE)
				.requiresCorrectToolForDrops()
				//.harvestTool(ToolType.PICKAXE)
				.strength(1.5F, 6.0F)
				.noOcclusion()
				.isViewBlocking((s, r, p) -> false);
		
		public static final Properties METAL = metal();
		
		private static Properties metal(){
			return Properties.of(METAL_LIKE)
					.sound(SoundType.METAL)
					.requiresCorrectToolForDrops()
					//.harvestTool(ToolType.PICKAXE)
					.strength(3.0F, 15.0F)
					.noOcclusion()
					.isViewBlocking((s, r, p) -> false);
		}
	}
}
