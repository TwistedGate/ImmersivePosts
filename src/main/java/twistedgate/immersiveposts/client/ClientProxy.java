package twistedgate.immersiveposts.client;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import twistedgate.immersiveposts.IPStuff;
import twistedgate.immersiveposts.ModInfo;
import twistedgate.immersiveposts.common.CommonProxy;

public class ClientProxy extends CommonProxy{
	@Override
	public void preInit(FMLPreInitializationEvent event){
	}
	
	@Override
	public void init(FMLInitializationEvent event){
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event){
		setupManualPage();
	}
	
	public void setupManualPage(){
		ManualHelper.addEntry("postbase", ModInfo.ID, new ManualPages.Crafting(ManualHelper.getManual(), "postbase0", new ItemStack(IPStuff.postBase)),
				new ManualPages.Text(ManualHelper.getManual(), "postbase1"));
	}
}
