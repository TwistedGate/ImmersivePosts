package twistedgate.immersiveposts;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import twistedgate.immersiveposts.common.CommonProxy;

@Mod(
	modid=ModInfo.ID,
	name=ModInfo.NAME,
	dependencies=ModInfo.DEPENDING,
	certificateFingerprint=ModInfo.CERT_PRINT,
	updateJSON=ModInfo.UPDATE_URL
)
public class ImmersivePosts{
	@Mod.Instance(ModInfo.ID)
	public static ImmersivePosts instance;
	
	@SidedProxy(modId=ModInfo.ID, serverSide=ModInfo.PROXY_SERVER, clientSide=ModInfo.PROXY_CLIENT)
	public static CommonProxy proxy;
	
	public static final CreativeTabs ipCreativeTab=new CreativeTabs(ModInfo.ID){
		ItemStack iconstack=null;
		@Override
		public ItemStack createIcon(){
			if(this.iconstack==null)
				iconstack=new ItemStack(IPStuff.postBase);
			return this.iconstack;
		}
	};
	
	public static Logger log;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		log=event.getModLog();
		
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		proxy.postInit(event);
	}
}
