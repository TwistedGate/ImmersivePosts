package twistedgate.immersiveposts.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.IPOContent.Blocks.Fences;
import twistedgate.immersiveposts.common.IPOContent.Blocks.HorizontalPosts;
import twistedgate.immersiveposts.common.IPOContent.Blocks.Posts;
import twistedgate.immersiveposts.common.blocks.BlockMetalFence;
import twistedgate.immersiveposts.common.blocks.BlockPost;
import twistedgate.immersiveposts.common.blocks.BlockPostBase;
import twistedgate.immersiveposts.common.blocks.HorizontalPostBlock;
import twistedgate.immersiveposts.common.items.IPOItemBase;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

/**
 * @author TwistedGate
 */
@Mod.EventBusSubscriber(modid=IPOMod.ID, bus=Bus.MOD)
public class IPOContent{
	public static final Logger log=LogManager.getLogger(IPOMod.ID+"/Stuff");
	
	public static final ArrayList<Block> BLOCKS=new ArrayList<>(0);
	public static final ArrayList<Item> ITEMS=new ArrayList<>(0);
	
	public static TileEntityType<PostBaseTileEntity> TE_POSTBASE;
	
	public static class Blocks{
		public static BlockPostBase post_Base;
		
		public static class Fences{
			/** Contains (or should) all Fence Blocks added by IPO */
			public static FenceBlock[] ALL;
			
			public static FenceBlock iron;
			public static FenceBlock gold;
			public static FenceBlock copper;
			public static FenceBlock lead;
			public static FenceBlock silver;
			public static FenceBlock nickel;
			public static FenceBlock constantan;
			public static FenceBlock electrum;
			public static FenceBlock uranium;
		}
		
		public static class Posts{
			/** Contains (or should) all Post Blocks added by IPO */
			static Map<EnumPostMaterial, BlockPost> MAP;
			
			public static BlockPost get(EnumPostMaterial material){
				return MAP.get(material);
			}
		}
		
		public static class HorizontalPosts{
			static Map<EnumPostMaterial, HorizontalPostBlock> MAP;
			
			public static HorizontalPostBlock get(EnumPostMaterial material){
				return MAP.get(material);
			}
		}
	}
	
	public static class Items{
		public static Item rod_Gold;
		public static Item rod_Copper;
		public static Item rod_Lead;
		public static Item rod_Silver;
		public static Item rod_Nickel;
		public static Item rod_Constantan;
		public static Item rod_Electrum;
		public static Item rod_Uranium;
	}
	
	public static final void populate(){
		Blocks.post_Base = new BlockPostBase();
		
		// =========================================================================
		// Fences
		
		Fences.ALL = new FenceBlock[]{
			Fences.iron			= new BlockMetalFence("iron"),
			Fences.gold			= new BlockMetalFence("gold"),
			Fences.copper		= new BlockMetalFence("copper"),
			Fences.lead			= new BlockMetalFence("lead"),
			Fences.silver		= new BlockMetalFence("silver"),
			Fences.nickel		= new BlockMetalFence("nickel"),
			Fences.constantan	= new BlockMetalFence("constantan"),
			Fences.electrum		= new BlockMetalFence("electrum"),
			Fences.uranium		= new BlockMetalFence("uranium")
		};
		
		// =========================================================================
		// Posts
		
		EnumPostMaterial[] values = EnumPostMaterial.values();
		Map<EnumPostMaterial, BlockPost> posts = new HashMap<>();
		Map<EnumPostMaterial, HorizontalPostBlock> h_posts = new HashMap<>();
		
		for(EnumPostMaterial mat:values){
			posts.put(mat, new BlockPost(mat));
			h_posts.put(mat, new HorizontalPostBlock(mat));
		}
		
		Posts.MAP = Collections.unmodifiableMap(posts);
		HorizontalPosts.MAP = Collections.unmodifiableMap(h_posts);
		
		// =========================================================================
		// Items
		
		Items.rod_Gold		= new IPOItemBase("stick_gold");
		Items.rod_Copper	= new IPOItemBase("stick_copper");
		Items.rod_Lead		= new IPOItemBase("stick_lead");
		Items.rod_Silver	= new IPOItemBase("stick_silver");
		Items.rod_Nickel	= new IPOItemBase("stick_nickel");
		Items.rod_Constantan= new IPOItemBase("stick_constantan");
		Items.rod_Electrum	= new IPOItemBase("stick_electrum");
		Items.rod_Uranium	= new IPOItemBase("stick_uranium");
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event){
		for(Block block:BLOCKS){
			try{
				event.getRegistry().register(block);
			}catch(Throwable e) {
				log.error("Failed to register a block. ({})", block);
				throw e;
			}
		}
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event){
		for(Item item:ITEMS){
			try{
				event.getRegistry().register(item);
			}catch(Throwable e) {
				log.error("Failed to register an item. ({}, {})", item, item.getRegistryName());
				throw e;
			}
		}
	}
	
	@SubscribeEvent
	public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event){
		try{
			event.getRegistry().register(TE_POSTBASE=create("postbase", PostBaseTileEntity::new, Blocks.post_Base));
		}catch(Throwable e){
			log.error("Failed to register postbase tileentity. {}", e.getMessage());
			throw e;
		}
	}
	
	private static <T extends TileEntity> TileEntityType<T> create(String name, Supplier<T> factory, Block... validBlocks){
		TileEntityType<T> te=TileEntityType.Builder.create(factory, validBlocks).build(null);
		te.setRegistryName(new ResourceLocation(IPOMod.ID, name));
		return te;
	}
}
