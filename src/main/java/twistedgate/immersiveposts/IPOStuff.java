package twistedgate.immersiveposts;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import twistedgate.immersiveposts.common.blocks.BlockMetalFence;
import twistedgate.immersiveposts.common.blocks.BlockPost;
import twistedgate.immersiveposts.common.blocks.BlockPostBase;
import twistedgate.immersiveposts.common.items.IPOItemBase;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

/**
 * @author TwistedGate
 */
@Mod.EventBusSubscriber(modid=IPOMod.ID, bus=Bus.MOD)
public class IPOStuff{
	public static final Logger log=LogManager.getLogger(IPOMod.ID+"/Stuff");
	
	public static final ArrayList<Block> BLOCKS=new ArrayList<>(0);
	public static final ArrayList<Item> ITEMS=new ArrayList<>(0);
	
	public static BlockPostBase post_Base;
	
	public static FenceBlock fence_Iron;
	public static FenceBlock fence_Gold;
	public static FenceBlock fence_Copper;
	public static FenceBlock fence_Lead;
	public static FenceBlock fence_Silver;
	public static FenceBlock fence_Nickel;
	public static FenceBlock fence_Constantan;
	public static FenceBlock fence_Electrum;
	public static FenceBlock fence_Uranium;
	
	public static BlockPost post_Wood;
	public static BlockPost post_Iron;
	public static BlockPost post_Gold;
	public static BlockPost post_Copper;
	public static BlockPost post_Lead;
	public static BlockPost post_Silver;
	public static BlockPost post_Nickel;
	public static BlockPost post_Constantan;
	public static BlockPost post_Electrum;
	public static BlockPost post_Uranium;
	public static BlockPost post_Nether;
	public static BlockPost post_Aluminium;
	public static BlockPost post_Steel;
	public static BlockPost post_Concrete;
	public static BlockPost post_ConcreteLeaded;
	
	public static Item rod_Gold;
	public static Item rod_Copper;
	public static Item rod_Lead;
	public static Item rod_Silver;
	public static Item rod_Nickel;
	public static Item rod_Constantan;
	public static Item rod_Electrum;
	public static Item rod_Uranium;
	
	public static final void populate(){
		post_Base=new BlockPostBase();
		
		// =========================================================================
		// Fences
		
		fence_Iron		=new BlockMetalFence("fence_iron");
		fence_Gold		=new BlockMetalFence("fence_gold");
		fence_Copper	=new BlockMetalFence("fence_copper");
		fence_Lead		=new BlockMetalFence("fence_lead");
		fence_Silver	=new BlockMetalFence("fence_silver");
		fence_Nickel	=new BlockMetalFence("fence_nickel");
		fence_Constantan=new BlockMetalFence("fence_constantan");
		fence_Electrum	=new BlockMetalFence("fence_electrum");
		fence_Uranium	=new BlockMetalFence("fence_uranium");
		
		// =========================================================================
		// Posts
		
		post_Wood			=new BlockPost(EnumPostMaterial.WOOD);
		post_Iron			=new BlockPost(EnumPostMaterial.IRON);
		post_Gold			=new BlockPost(EnumPostMaterial.GOLD);
		post_Copper			=new BlockPost(EnumPostMaterial.COPPER);
		post_Lead			=new BlockPost(EnumPostMaterial.LEAD);
		post_Silver			=new BlockPost(EnumPostMaterial.SILVER);
		post_Nickel			=new BlockPost(EnumPostMaterial.NICKEL);
		post_Constantan		=new BlockPost(EnumPostMaterial.CONSTANTAN);
		post_Electrum		=new BlockPost(EnumPostMaterial.ELECTRUM);
		post_Uranium		=new BlockPost(EnumPostMaterial.URANIUM);
		post_Nether			=new BlockPost(EnumPostMaterial.NETHERBRICK);
		post_Aluminium		=new BlockPost(EnumPostMaterial.ALUMINIUM);
		post_Steel			=new BlockPost(EnumPostMaterial.STEEL);
		post_Concrete		=new BlockPost(EnumPostMaterial.CONCRETE);
		post_ConcreteLeaded	=new BlockPost(EnumPostMaterial.CONCRETE_LEADED);
		
		// =========================================================================
		// Items
		
		rod_Gold		=new IPOItemBase("stick_gold");
		rod_Copper		=new IPOItemBase("stick_copper");
		rod_Lead		=new IPOItemBase("stick_lead");
		rod_Silver		=new IPOItemBase("stick_silver");
		rod_Nickel		=new IPOItemBase("stick_nickel");
		rod_Constantan	=new IPOItemBase("stick_constantan");
		rod_Electrum	=new IPOItemBase("stick_electrum");
		rod_Uranium		=new IPOItemBase("stick_uranium");
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
}
