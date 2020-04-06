package twistedgate.immersiveposts.client;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualElementTable;
import blusunrize.lib.manual.ManualEntry;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.Tree.InnerNode;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.CommonProxy;

/**
 * @author TwistedGate
 */
@Mod.EventBusSubscriber(value=Dist.CLIENT, modid=IPOMod.ID, bus=Bus.MOD)
public class ClientProxy extends CommonProxy{
	@Override
	public void completed(){
		// TODO Remake manual entries.
		setupManualPage();
	}
	
	
	public void setupManualPage(){
		ManualInstance man=ManualHelper.getManual();
		
		InnerNode<ResourceLocation, ManualEntry> cat=man.contentTree.getRoot().getOrCreateSubnode(modLoc("main"));
		
		man.addEntry(cat, modLoc("postbase"));
		man.addEntry(cat, modLoc("usage"));
		
		{ // TODO Temporary
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
			
			ManualEntry.ManualEntryBuilder builder=new ManualEntry.ManualEntryBuilder(man);
			builder.addSpecialElement("index0", 0, new ManualElementTable(man, index0, false));
			builder.addSpecialElement("index1", 1, new ManualElementTable(man, index1, false));
			builder.readFromFile(modLoc("posts"));
			man.addEntry(cat, builder.create());
		}
	}
	
	private ResourceLocation modLoc(String str){
		return new ResourceLocation(IPOMod.ID, str);
	}
	
	/*
	private void setupUsagePage(ManualInstance man){
		IManualPage[] pages=new IManualPage[]{
				new ManualPages.ItemDisplay(man, IPOMod.ID+".usage.p0", new ItemStack(IEItems.Tools.hammer)),
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
