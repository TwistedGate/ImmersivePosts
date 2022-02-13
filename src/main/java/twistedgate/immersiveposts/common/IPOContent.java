package twistedgate.immersiveposts.common;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.Util;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.ImmersivePosts;
import twistedgate.immersiveposts.api.posts.IPostMaterial;
import twistedgate.immersiveposts.common.IPOContent.Blocks.Fences;
import twistedgate.immersiveposts.common.blocks.HorizontalTrussBlock;
import twistedgate.immersiveposts.common.blocks.MetalFenceBlock;
import twistedgate.immersiveposts.common.blocks.PostBaseBlock;
import twistedgate.immersiveposts.common.blocks.PostBlock;
import twistedgate.immersiveposts.common.items.IPOItemBase;
import twistedgate.immersiveposts.common.tileentity.IPOTileTypes;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

/**
 * @author TwistedGate
 */
public class IPOContent{
	public static final Logger log = LogManager.getLogger(IPOMod.ID + "/Stuff");
	
	private static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, IPOMod.ID);
	private static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, IPOMod.ID);
	
	public static final void addRegistersToEventBus(IEventBus eventBus){
		BLOCK_REGISTER.register(eventBus);
		ITEM_REGISTER.register(eventBus);
		IPOTileTypes.REGISTER.register(eventBus);
	}
	
	protected static final <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> constructor){
		return BLOCK_REGISTER.register(name, constructor);
	}
	
	protected static final RegistryObject<PostBlock> registerPostBlock(EnumPostMaterial material){
		return BLOCK_REGISTER.register(material.getBlockName(), () -> new PostBlock(material));
	}
	
	protected static final RegistryObject<HorizontalTrussBlock> registerTrussBlock(EnumPostMaterial material){
		return BLOCK_REGISTER.register(material.getBlockName() + "_truss", () -> new HorizontalTrussBlock(material));
	}
	
	protected static final RegistryObject<FenceBlock> registerMetalFence(String name){
		name = "fence_" + name;
		
		RegistryObject<FenceBlock> block = BLOCK_REGISTER.register(name, MetalFenceBlock::new);
		ITEM_REGISTER.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(ImmersivePosts.creativeTab)));
		Fences.ALL_FENCES.add(block);
		return block;
	}
	
	protected static final <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> constructor){
		return ITEM_REGISTER.register(name, constructor);
	}
	
	public static class Blocks{
		public static final RegistryObject<PostBaseBlock> POST_BASE;
		
		static{
			POST_BASE = registerBlock("postbase", PostBaseBlock::new);
			registerItem("postbase", () -> new PostBaseBlock.ItemPostBase(POST_BASE.get()));
		}
		
		private static void forceClassLoad(){
		}
		
		public static class Fences{
			
			/** Contains (or should) all Fence Blocks added by IPO */
			public static final List<RegistryObject<FenceBlock>> ALL_FENCES = new ArrayList<>();
			
			public final static RegistryObject<FenceBlock> IRON = registerMetalFence("iron");
			public final static RegistryObject<FenceBlock> GOLD = registerMetalFence("gold");
			public final static RegistryObject<FenceBlock> COPPER = registerMetalFence("copper");
			public final static RegistryObject<FenceBlock> LEAD = registerMetalFence("lead");
			public final static RegistryObject<FenceBlock> SILVER = registerMetalFence("silver");
			public final static RegistryObject<FenceBlock> NICKEL = registerMetalFence("nickel");
			public final static RegistryObject<FenceBlock> CONSTANTAN = registerMetalFence("constantan");
			public final static RegistryObject<FenceBlock> ELECTRUM = registerMetalFence("electrum");
			public final static RegistryObject<FenceBlock> URANIUM = registerMetalFence("uranium");
			
			private static void forceClassLoad(){
			}
		}
		
		public static class Posts{
			/** Contains all Post Blocks added by IPO */
			static final EnumMap<EnumPostMaterial, RegistryObject<PostBlock>> ALL = Util.make(new EnumMap<>(EnumPostMaterial.class), map -> {
				for(EnumPostMaterial material:EnumPostMaterial.values()){
					map.put(material, registerPostBlock(material));
				}
			});
			
			public static PostBlock get(@Nonnull IPostMaterial material){
				if(!ALL.containsKey(material)) return null;
				return ALL.get(material).get();
			}
			
			private static void forceClassLoad(){
			}
		}
		
		public static class HorizontalTruss{
			/** Contains all Truss Blocks added by IPO */
			static EnumMap<EnumPostMaterial, RegistryObject<HorizontalTrussBlock>> ALL = Util.make(new EnumMap<>(EnumPostMaterial.class), map -> {
				for(EnumPostMaterial material:EnumPostMaterial.values()){
					map.put(material, registerTrussBlock(material));
				}
			});
			
			public static HorizontalTrussBlock get(@Nonnull IPostMaterial material){
				if(!ALL.containsKey(material)) return null;
				return ALL.get(material).get();
			}
			
			private static void forceClassLoad(){
			}
		}
	}
	
	public static class Items{
		public final static RegistryObject<Item> ROD_GOLD = registerItem("stick_gold", IPOItemBase::new);
		public final static RegistryObject<Item> ROD_COPPER = registerItem("stick_copper", IPOItemBase::new);
		public final static RegistryObject<Item> ROD_LEAD = registerItem("stick_lead", IPOItemBase::new);
		public final static RegistryObject<Item> ROD_SILVER = registerItem("stick_silver", IPOItemBase::new);
		public final static RegistryObject<Item> ROD_NICKEL = registerItem("stick_nickel", IPOItemBase::new);
		public final static RegistryObject<Item> ROD_CONSTANTAN = registerItem("stick_constantan", IPOItemBase::new);
		public final static RegistryObject<Item> ROD_ELECTRUM = registerItem("stick_electrum", IPOItemBase::new);
		public final static RegistryObject<Item> ROD_URANIUM = registerItem("stick_uranium", IPOItemBase::new);
		
		private static void forceClassLoad(){
		}
	}
	
	public static final void populate(){
		Blocks.forceClassLoad();
		Blocks.Fences.forceClassLoad();
		Blocks.Posts.forceClassLoad();
		Blocks.HorizontalTruss.forceClassLoad();
		Items.forceClassLoad();
	}
}
