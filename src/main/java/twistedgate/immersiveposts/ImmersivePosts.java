package twistedgate.immersiveposts;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import twistedgate.immersiveposts.common.CommonProxy;

/**
 * @author TwistedGate
 */
@Mod(
	modid=IPOMod.ID,
	name=IPOMod.NAME,
	version=IPOMod.VERSION,
	dependencies=IPOMod.DEPENDS,
	certificateFingerprint=IPOMod.CERT_PRINT,
	updateJSON=IPOMod.UPDATE_URL
)
public class ImmersivePosts{
	@Mod.Instance(IPOMod.ID)
	public static ImmersivePosts instance;
	
	@SidedProxy(modId=IPOMod.ID, serverSide=IPOMod.PROXY_SERVER, clientSide=IPOMod.PROXY_CLIENT)
	public static CommonProxy proxy;
	
	public static final CreativeTabs creativeTab=new CreativeTabs(IPOMod.ID){
		@Override
		public ItemStack createIcon(){
			return new ItemStack(IPOStuff.postBase==null?Blocks.BARRIER:IPOStuff.postBase);
		}
	};
	
	public static Logger log;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		log=event.getModLog();
		
		proxy.preInit(event);
		
		IPOStuff.initBlocks();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		proxy.postInit(event);
	}
	
	@EventHandler
	public void violation(FMLFingerprintViolationEvent event){
		System.err.println("THIS IS NOT AN OFFICIAL BUILD OF "+IPOMod.NAME.toUpperCase()+"! Fingerprints: ["+event.getFingerprints()+"]");
		// Guess thats what this would be used for? lol
	}
}
