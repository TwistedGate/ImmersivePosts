package twistedgate.immersiveposts.client;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualElementTable;
import blusunrize.lib.manual.ManualEntry;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.Tree.InnerNode;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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
		setupManualPage();
	}
	
	public void setupManualPage(){
		ManualInstance man=ManualHelper.getManual();
		
		InnerNode<ResourceLocation, ManualEntry> cat=man.getRoot().getOrCreateSubnode(modLoc("main"), 100);
		
		man.addEntry(cat, modLoc("postbase"), 0);
		man.addEntry(cat, modLoc("usage"), 1);
		
		{
			ITextComponent[][] index0=new ITextComponent[13][2];
			ITextComponent[][] index1=new ITextComponent[4][2];
			
			int page=1;
			for(int i=0;i<index0.length;i++){
				index0[i][0]=new StringTextComponent(Integer.toString(page++));
				index0[i][1]=new StringTextComponent("index.page_0_entry."+Integer.toString(i+1));
			}
			for(int i=0;i<index1.length;i++){
				index1[i][0]=new StringTextComponent(Integer.toString(page++));
				index1[i][1]=new StringTextComponent("index.page_1_entry."+Integer.toString(i+1));
			}
			
			ManualEntry.ManualEntryBuilder builder=new ManualEntry.ManualEntryBuilder(man);
			builder.addSpecialElement("index0", 0, new ManualElementTable(man, index0, false));
			builder.addSpecialElement("index1", 0, new ManualElementTable(man, index1, false));
			builder.readFromFile(modLoc("posts"));
			man.addEntry(cat, builder.create(), 2);
		}
	}
	
	private ResourceLocation modLoc(String str){
		return new ResourceLocation(IPOMod.ID, str);
	}
}
