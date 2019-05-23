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
				ModelLoader.setCustomModelResourceLocation(item, 0, loc);
			}
		}
	}
	
	public void setupManualPage(){
		ManualInstance man=ManualHelper.getManual();
		
		ManualHelper.addEntry("postbase", IPOMod.ID,
				new ManualPages.Crafting(man, "postbase0", new ItemStack(IPOStuff.postBase)),
				new ManualPages.Text(man, "postbase1"));
		
		ArrayList<IManualPage> fencePages=new ArrayList<>();
		ArrayList<String[]> names=new ArrayList<>();
		names.add(new String[]{"Page 1", "Index Page."});
		int i=2;
		for(EnumPostMaterial mat:EnumPostMaterial.values()){
			String s=mat.toString();
			Object[] items=new Object[]{
				"stick"+(mat==EnumPostMaterial.WOOD?"TreatedWood":(mat==EnumPostMaterial.ALUMINIUM?"Aluminum":StringUtils.upperCaseFirst(s))),
				mat.getFenceItem()
			};
			
			ManualPages.Crafting page=new ManualPages.Crafting(man, "fences_"+(s.toLowerCase()), items);
			
			if(fencePages.add(page)){
				names.add(new String[]{"Page "+(i++), StringUtils.upperCaseFirst(s)});
			}
			
		}
		
		String[][] table=new String[names.size()][2];
		for(i=0;i<names.size();i++){
			table[i]=names.get(i);
		}
		fencePages.add(0, new ManualPages.Table(man, "fences_index", table, false));
		
		IManualPage[] array=fencePages.toArray(new IManualPage[fencePages.size()]);
		fencePages.clear();
		
		ManualHelper.addEntry("fences", IPOMod.ID, array);
	}
}
