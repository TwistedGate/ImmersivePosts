package twistedgate.immersiveposts.common;

import java.util.ArrayList;
import java.util.EnumMap;
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
import twistedgate.immersiveposts.common.IPOContent.Blocks.HorizontalTruss;
import twistedgate.immersiveposts.common.IPOContent.Blocks.Posts;
import twistedgate.immersiveposts.common.blocks.HorizontalTrussBlock;
import twistedgate.immersiveposts.common.blocks.MetalFenceBlock;
import twistedgate.immersiveposts.common.blocks.PostBaseBlock;
import twistedgate.immersiveposts.common.blocks.PostBlock;
import twistedgate.immersiveposts.common.items.IPOItemBase;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

/**
 * @author TwistedGate
 */
@Mod.EventBusSubscriber(modid=IPOMod.ID, bus=Bus.MOD)
public class IPOContent{
	public static final Logger log = LogManager.getLogger(IPOMod.ID + "/Stuff");
	
	public static final ArrayList<Block> BLOCKS = new ArrayList<>(0);
	public static final ArrayList<Item> ITEMS = new ArrayList<>(0);
	
	public static TileEntityType<PostBaseTileEntity> TE_POSTBASE;
	
	public static class Blocks{
		public static PostBaseBlock post_Base;
		
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
			static EnumMap<EnumPostMaterial, PostBlock> MAP;
			
			public static PostBlock get(EnumPostMaterial material){
				return MAP.get(material);
			}
		}
		
		public static class HorizontalTruss{
			static EnumMap<EnumPostMaterial, HorizontalTrussBlock> MAP;
			
			public static HorizontalTrussBlock get(EnumPostMaterial material){
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
		Blocks.post_Base = new PostBaseBlock();
		
		// =========================================================================
		// Fences
		
		Fences.ALL = new FenceBlock[]{
			Fences.iron			= new MetalFenceBlock("iron"),
			Fences.gold			= new MetalFenceBlock("gold"),
			Fences.copper		= new MetalFenceBlock("copper"),
			Fences.lead			= new MetalFenceBlock("lead"),
			Fences.silver		= new MetalFenceBlock("silver"),
			Fences.nickel		= new MetalFenceBlock("nickel"),
			Fences.constantan	= new MetalFenceBlock("constantan"),
			Fences.electrum		= new MetalFenceBlock("electrum"),
			Fences.uranium		= new MetalFenceBlock("uranium")
		};
		
		// =========================================================================
		// Posts
		
		EnumPostMaterial[] values = EnumPostMaterial.values();
		EnumMap<EnumPostMaterial, PostBlock> posts = new EnumMap<>(EnumPostMaterial.class);
		EnumMap<EnumPostMaterial, HorizontalTrussBlock> trusses = new EnumMap<>(EnumPostMaterial.class);
		
		for(EnumPostMaterial mat:values){
			posts.put(mat, new PostBlock(mat));
			trusses.put(mat, new HorizontalTrussBlock(mat));
		}
		
		Posts.MAP = posts;
		HorizontalTruss.MAP = trusses;
		
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
			}catch(Throwable e){
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
			}catch(Throwable e){
				log.error("Failed to register an item. ({}, {})", item, item.getRegistryName());
				throw e;
			}
		}
	}
	
	@SubscribeEvent
	public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event){
		try{
			event.getRegistry().register(TE_POSTBASE = create("postbase", PostBaseTileEntity::new, Blocks.post_Base));
		}catch(Throwable e){
			log.error("Failed to register postbase tileentity. {}", e.getMessage());
			throw e;
		}
	}
	
	private static <T extends TileEntity> TileEntityType<T> create(String name, Supplier<T> factory, Block... validBlocks){
		TileEntityType<T> te = TileEntityType.Builder.create(factory, validBlocks).build(null);
		te.setRegistryName(new ResourceLocation(IPOMod.ID, name));
		return te;
	}
}
