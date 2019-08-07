package twistedgate.immersiveposts;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import twistedgate.immersiveposts.common.blocks.BlockMetalFence;
import twistedgate.immersiveposts.common.blocks.BlockPost;
import twistedgate.immersiveposts.common.blocks.BlockPostBase;
import twistedgate.immersiveposts.common.items.MetalRods;
import twistedgate.immersiveposts.common.items.MultiMetaItem;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.utils.StringUtils;

@Mod.EventBusSubscriber(modid=IPOMod.ID)
public class IPOStuff{
	public static final ArrayList<Block> BLOCKS=new ArrayList<>();
	public static final ArrayList<Item> ITEMS=new ArrayList<>();
	
	public static BlockPostBase postBase;
	
	public static BlockFence ironFence;
	public static BlockFence goldFence;
	public static BlockFence copperFence;
	public static BlockFence leadFence;
	public static BlockFence silverFence;
	public static BlockFence nickelFence;
	public static BlockFence constantanFence;
	public static BlockFence electrumFence;
	public static BlockFence uraniumFence;
	
	public static BlockPost woodPost;
	public static BlockPost ironPost;
	public static BlockPost goldPost;
	public static BlockPost copperPost;
	public static BlockPost leadPost;
	public static BlockPost silverPost;
	public static BlockPost nickelPost;
	public static BlockPost constantanPost;
	public static BlockPost electrumPost;
	public static BlockPost uraniumPost;
	public static BlockPost netherPost;
	public static BlockPost aluminiumPost;
	public static BlockPost steelPost;
	public static BlockPost concretePost;
	public static BlockPost leadedConcretePost;
	
	public static MultiMetaItem multiItemTest;
	
	public static final void initBlocks(){
		postBase=new BlockPostBase();
		
		// =========================================================================
		// Fences
		
		ironFence		=new BlockMetalFence("fence_iron");
		goldFence		=new BlockMetalFence("fence_gold");
		copperFence		=new BlockMetalFence("fence_copper");
		leadFence		=new BlockMetalFence("fence_lead");
		silverFence		=new BlockMetalFence("fence_silver");
		nickelFence		=new BlockMetalFence("fence_nickel");
		constantanFence	=new BlockMetalFence("fence_constantan");
		electrumFence	=new BlockMetalFence("fence_electrum");
		uraniumFence	=new BlockMetalFence("fence_uranium");
		
		// =========================================================================
		// Posts
		
		woodPost			=new BlockPost(Material.WOOD, EnumPostMaterial.WOOD);
		ironPost			=createMetalPost(EnumPostMaterial.IRON);
		goldPost			=createMetalPost(EnumPostMaterial.GOLD);
		copperPost			=createMetalPost(EnumPostMaterial.COPPER);
		leadPost			=createMetalPost(EnumPostMaterial.LEAD);
		silverPost			=createMetalPost(EnumPostMaterial.SILVER);
		nickelPost			=createMetalPost(EnumPostMaterial.NICKEL);
		constantanPost		=createMetalPost(EnumPostMaterial.CONSTANTAN);
		electrumPost		=createMetalPost(EnumPostMaterial.ELECTRUM);
		uraniumPost			=createMetalPost(EnumPostMaterial.URANIUM);
		netherPost			=createMetalPost(EnumPostMaterial.NETHERBRICK);
		aluminiumPost				=createMetalPost(EnumPostMaterial.ALUMINIUM);
		steelPost			=createMetalPost(EnumPostMaterial.STEEL);
		concretePost		=new BlockPost(Material.ROCK, EnumPostMaterial.CONCRETE);
		leadedConcretePost	=new BlockPost(Material.ROCK, EnumPostMaterial.CONCRETE_LEADED);
		
		// =========================================================================
		// Items
		
		multiItemTest=new MetalRods();
		
	}
	
	private static BlockPost createMetalPost(EnumPostMaterial postMat){
		return new BlockPost(Material.IRON, postMat);
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event){
		for(Block block:BLOCKS){
			event.getRegistry().register(block);
		}
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event){
		for(Item item:ITEMS){
			event.getRegistry().register(item);
		}
		
		registerFenceOres();
		registerStickOres();
	}
	
	private static void registerFenceOres(){
		String prefix="fence";
		for(EnumPostMaterial mat:EnumPostMaterial.values()){
			switch(mat){
				case WOOD:case NETHERBRICK:case IRON:case ALUMINIUM:case STEEL:continue;
				default:
					OreDictionary.registerOre(prefix+StringUtils.upperCaseFirst(mat.toString()), mat.getBlock());
			}
		}
	}
	
	private static void registerStickOres(){
		String prefix="stick";
		String rep="stick_";
		for(Item item:ITEMS){
			if(item instanceof MetalRods){
				MultiMetaItem mItem=(MultiMetaItem)item;
				for(int i=0;i<mItem.getSubItemCount();i++){
					if(mItem.getName(i).contains(rep)){
						String oreName=prefix+StringUtils.upperCaseFirst(mItem.getName(i).substring(rep.length()));
						
						OreDictionary.registerOre(oreName, new ItemStack(mItem, 1, i));
					}
				}
			}
		}
	}
}
