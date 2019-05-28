package twistedgate.immersiveposts.client;

import java.util.ArrayList;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.IManualPage;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.common.CommonProxy;
import twistedgate.immersiveposts.common.blocks.BlockPost;
import twistedgate.immersiveposts.common.items.MultiMetaItem;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.utils.StringUtils;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy{
	@Override
	public void preInitStart(FMLPreInitializationEvent event){
		OBJLoader.INSTANCE.addDomain(IPOMod.ID);
	}
	
	@Override
	public void postInitEnd(FMLPostInitializationEvent event){
		setupManualPage();
	}
	
	@SubscribeEvent
	public static void regModels(ModelRegistryEvent event){
		for(Block block:IPOStuff.BLOCKS){
			if(!(block instanceof BlockPost)){ // Prevent posts from getting an item
				Item item=Item.getItemFromBlock(block);
				ModelResourceLocation loc=new ModelResourceLocation(block.getRegistryName(), "inventory");
				ModelLoader.setCustomModelResourceLocation(item, 0, loc);
			}
		}
		
		for(Item item:IPOStuff.ITEMS){
			if(item instanceof ItemBlock) continue;
			
			if(item instanceof MultiMetaItem){
				MultiMetaItem mItem=(MultiMetaItem)item;
				for(int i=0;i<mItem.getSubItemCount();i++){
					ResourceLocation loc=new ResourceLocation(IPOMod.ID, mItem.regName+"/"+mItem.getName(i));
					ModelBakery.registerItemVariants(mItem, loc);
					ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(loc, "inventory"));
				}
				
			}else{
				ModelResourceLocation loc=new ModelResourceLocation(item.getRegistryName(), "inventory");
				
				for(int i=0;i<15;i++)
					ModelLoader.setCustomModelResourceLocation(item, i, loc);
			}
		}
	}
	
	public void setupManualPage(){
		ManualInstance man=ManualHelper.getManual();
		
		ManualHelper.addEntry(IPOMod.ID+".postbase", IPOMod.ID,
				new ManualPages.Crafting(man, IPOMod.ID+".postbase0", new ItemStack(IPOStuff.postBase)),
				new ManualPages.Text(man, IPOMod.ID+".postbase1"));
		
		setupAcceptedBlocksCategory(man);
	}
	
	private void setupAcceptedBlocksCategory(ManualInstance man){
		// every 13 entries a new index page
		ArrayList<IManualPage> fencePages=new ArrayList<>();
		
		{ // I feel dirty doing it manualy..
			String[][] index0=new String[][]{
				{"Page  1","Index Page 1"},
				{"Page  2","Index Page 2"},
				{"Page  3","Treated Wood"},
				{"Page  4","Netherbrick"},
				{"Page  5","Iron"},
				{"Page  6","Gold"},
				{"Page  7","Copper"},
				{"Page  8","Lead"},
				{"Page  9","Silver"},
				{"Page 10","Nickel"},
				{"Page 11","Constantan"},
				{"Page 12","Electrum"},
				{"Page 13","Uranium"},
			};
			fencePages.add(new ManualPages.Table(man, IPOMod.ID+".posts_index0", index0, true));
			
			String[][] index1=new String[][]{
				{"Page 14","Aluminium"},
				{"Page 15","Steel"},
				{"Page 16","Concrete"},
				{"Page 17","Concrete (Leaded)"},
			};
			fencePages.add(new ManualPages.Table(man, IPOMod.ID+".posts_index1", index1, true));
		}
		
		for(EnumPostMaterial mat:EnumPostMaterial.values()){
			String s=mat.toString();
			
			Object[] items;
			if(mat.isFence()){
				String top="stick";
				switch(mat){
					case WOOD:		top+="TreatedWood";break;
					case ALUMINIUM:	top+="Aluminum";break;
					default:		top+=StringUtils.upperCaseFirst(s);break;
				}
				items=new Object[]{top, mat.getItemStack()};
				
			}else{
				items=new Object[]{mat.getItemStack()};
			}
			
			ManualPages.Crafting page=new ManualPages.Crafting(man, IPOMod.ID+".posts_"+(s.toLowerCase()), items);
			fencePages.add(page);
		}
		
		IManualPage[] array=fencePages.toArray(new IManualPage[fencePages.size()]);
		fencePages.clear();
		
		ManualHelper.addEntry(IPOMod.ID+".posts", IPOMod.ID, array);
	}
}
