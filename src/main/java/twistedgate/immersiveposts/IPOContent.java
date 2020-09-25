package twistedgate.immersiveposts;

import java.util.ArrayList;
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
import twistedgate.immersiveposts.IPOContent.Blocks.Fences;
import twistedgate.immersiveposts.IPOContent.Blocks.Posts;
import twistedgate.immersiveposts.common.blocks.BlockMetalFence;
import twistedgate.immersiveposts.common.blocks.BlockPost;
import twistedgate.immersiveposts.common.blocks.BlockPostBase;
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
			public static BlockPost wood;
			public static BlockPost iron;
			public static BlockPost gold;
			public static BlockPost copper;
			public static BlockPost lead;
			public static BlockPost silver;
			public static BlockPost nickel;
			public static BlockPost constantan;
			public static BlockPost electrum;
			public static BlockPost uranium;
			public static BlockPost nether;
			public static BlockPost aluminium;
			public static BlockPost steel;
			public static BlockPost concrete;
			public static BlockPost concrete_leaded;
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
		Blocks.post_Base=new BlockPostBase();
		
		// =========================================================================
		// Fences
		
		Fences.iron			=new BlockMetalFence("iron");
		Fences.gold			=new BlockMetalFence("gold");
		Fences.copper		=new BlockMetalFence("copper");
		Fences.lead			=new BlockMetalFence("lead");
		Fences.silver		=new BlockMetalFence("silver");
		Fences.nickel		=new BlockMetalFence("nickel");
		Fences.constantan	=new BlockMetalFence("constantan");
		Fences.electrum		=new BlockMetalFence("electrum");
		Fences.uranium		=new BlockMetalFence("uranium");
		
		// =========================================================================
		// Posts
		
		Posts.wood				=new BlockPost(EnumPostMaterial.WOOD);
		Posts.iron				=new BlockPost(EnumPostMaterial.IRON);
		Posts.gold				=new BlockPost(EnumPostMaterial.GOLD);
		Posts.copper			=new BlockPost(EnumPostMaterial.COPPER);
		Posts.lead				=new BlockPost(EnumPostMaterial.LEAD);
		Posts.silver			=new BlockPost(EnumPostMaterial.SILVER);
		Posts.nickel			=new BlockPost(EnumPostMaterial.NICKEL);
		Posts.constantan		=new BlockPost(EnumPostMaterial.CONSTANTAN);
		Posts.electrum			=new BlockPost(EnumPostMaterial.ELECTRUM);
		Posts.uranium			=new BlockPost(EnumPostMaterial.URANIUM);
		Posts.nether			=new BlockPost(EnumPostMaterial.NETHERBRICK);
		Posts.aluminium			=new BlockPost(EnumPostMaterial.ALUMINIUM);
		Posts.steel				=new BlockPost(EnumPostMaterial.STEEL);
		Posts.concrete			=new BlockPost(EnumPostMaterial.CONCRETE);
		Posts.concrete_leaded	=new BlockPost(EnumPostMaterial.CONCRETE_LEADED);
		
		// =========================================================================
		// Items
		
		Items.rod_Gold		=new IPOItemBase("stick_gold");
		Items.rod_Copper	=new IPOItemBase("stick_copper");
		Items.rod_Lead		=new IPOItemBase("stick_lead");
		Items.rod_Silver	=new IPOItemBase("stick_silver");
		Items.rod_Nickel	=new IPOItemBase("stick_nickel");
		Items.rod_Constantan=new IPOItemBase("stick_constantan");
		Items.rod_Electrum	=new IPOItemBase("stick_electrum");
		Items.rod_Uranium	=new IPOItemBase("stick_uranium");
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
