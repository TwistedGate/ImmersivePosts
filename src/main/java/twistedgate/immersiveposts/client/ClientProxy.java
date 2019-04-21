package twistedgate.immersiveposts.client;

import java.util.ArrayList;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.IManualPage;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.common.CommonProxy;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.utils.StringUtils;

public class ClientProxy extends CommonProxy{
	@Override
	public void preInit(FMLPreInitializationEvent event){
		OBJLoader.INSTANCE.addDomain(IPOMod.ID);
	}
	
	@Override
	public void init(FMLInitializationEvent event){
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event){
		setupManualPage();
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
