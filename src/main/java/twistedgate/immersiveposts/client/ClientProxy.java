package twistedgate.immersiveposts.client;

import blusunrize.immersiveengineering.client.models.obj.IEOBJLoader;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.common.CommonProxy;
import twistedgate.immersiveposts.common.blocks.BlockPost;
import twistedgate.immersiveposts.common.items.MultiMetaItem;

/**
 * @author TwistedGate
 */
@Mod.EventBusSubscriber(value=Dist.CLIENT, modid=IPOMod.ID, bus=Bus.MOD)
public class ClientProxy extends CommonProxy{
	
	@Override
	public void construct(){
	}
	
	@Override
	public void preInit(){
		IEOBJLoader.instance.addDomain(IPOMod.ID);
		//OBJLoader.INSTANCE.addDomain(IPOMod.ID);
	}
	
	@Override
	public void postInit(){
		// TODO Remake manual entries.
		//setupManualPage();
	}
	
	@SubscribeEvent
	public static void regModels(ModelRegistryEvent event){
		for(Block block:IPOStuff.BLOCKS){
			if(!(block instanceof BlockPost)){ // Prevent posts from getting an item
				Item item=block.asItem();
				ModelResourceLocation loc=new ModelResourceLocation(block.getRegistryName(), "inventory");
				//ModelLoader.setCustomModelResourceLocation(item, 0, loc);
			}
		}
		
		for(Item item:IPOStuff.ITEMS){
			if(item instanceof BlockItem) continue;
			
			if(item instanceof MultiMetaItem){
				MultiMetaItem mItem=(MultiMetaItem)item;
				for(int i=0;i<mItem.getSubItemCount();i++){
					ResourceLocation loc=new ResourceLocation(IPOMod.ID, mItem.regName+"/"+mItem.getName(i));
					//ModelBakery.registerItemVariants(mItem, loc);
					//ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(loc, "inventory"));
				}
				
			}else{
				ModelResourceLocation loc=new ModelResourceLocation(item.getRegistryName(), "inventory");
				
				//for(int i=0;i<15;i++)
					//ModelLoader.setCustomModelResourceLocation(item, i, loc);
			}
		}
	}
	
	/*
	public void setupManualPage(){
		ManualInstance man=ManualHelper.getManual();
		
		man.addEntry(IPOMod.ID+".postbase", IPOMod.ID, new ManualPages.Crafting(man, IPOMod.ID+".postbase0", new ItemStack(IPOStuff.postBase)));
		
		setupUsagePage(man);
		setupAcceptedBlocksCategory(man);
	}
	
	private void setupUsagePage(ManualInstance man){
		IManualPage[] pages=new IManualPage[]{
				new ManualPages.ItemDisplay(man, IPOMod.ID+".usage.p0", new ItemStack(IEContent.itemTool, 1, 0)),
				new ManualPages.Image(man, IPOMod.ID+".usage.p1", "immersiveposts:textures/manual/usage/what.png;0;0;64;64"),
				new ManualPages.Image(man, IPOMod.ID+".usage.p2", "immersiveposts:textures/manual/usage/what.png;0;64;64;75"),
				new ManualPages.Image(man, IPOMod.ID+".usage.p3", "immersiveposts:textures/manual/usage/what.png;64;0;64;64"),
				new ManualPages.Image(man, IPOMod.ID+".usage.p4", "immersiveposts:textures/manual/usage/what.png;128;0;81;64"),
				new ManualPages.Image(man, IPOMod.ID+".usage.p5", "immersiveposts:textures/manual/usage/what.png;128;64;112;64"),
		};
		
		man.addEntry(IPOMod.ID+".usage", IPOMod.ID, pages);
	}
	
	private void setupAcceptedBlocksCategory(ManualInstance man){
		// every 13 entries a new index page
		ArrayList<IManualPage> fencePages=new ArrayList<>();
		
		{
			String[][] index0=new String[13][2];
			String[][] index1=new String[4][2];
			
			int page=1;
			for(int i=0;i<index0.length;i++){
				index0[i][0]=Integer.toString(page++);
				index0[i][1]="index.page_0_entry."+Integer.toString(i+1);
			}
			for(int i=0;i<index1.length;i++){
				index1[i][0]=Integer.toString(page++);
				index1[i][1]="index.page_1_entry."+Integer.toString(i+1);
			}
			
			fencePages.add(new ManualPages.Table(man, IPOMod.ID+".posts_index0", index0, true));
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
	*/
}
