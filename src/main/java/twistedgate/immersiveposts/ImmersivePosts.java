package twistedgate.immersiveposts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import twistedgate.immersiveposts.client.ClientProxy;
import twistedgate.immersiveposts.common.CommonProxy;

/**
 * @author TwistedGate
 */
@Mod(IPOMod.ID)
public class ImmersivePosts{
	
	public static final ItemGroup creativeTab=new IPOItemGroup();
	public static CommonProxy proxy=DistExecutor.runForDist(()->()->new ClientProxy(), ()->()->new CommonProxy());
	public static Logger log;
	
	public ImmersivePosts(){
		log=LogManager.getLogger(IPOMod.ID);
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::violation);
		
		IPOStuff.populate();
		
		proxy.construct();
	}
	
	public void setup(FMLCommonSetupEvent event){
		proxy.preInit();
		
		proxy.postInit();
	}
	
	public void violation(FMLFingerprintViolationEvent event){
		System.err.println("\r\nTHIS IS NOT AN OFFICIAL BUILD OF "+IPOMod.NAME.toUpperCase()+"! Fingerprints: ["+event.getFingerprints()+"]");
		System.err.println("If you didnt download this mod from the official curse page: Stop Drop and Roll this jar right out of your mods folder!\r\n");
		// Guess thats what this would be used for? lol
	}
	
	
	public static class IPOItemGroup extends ItemGroup{
		private ItemStack iconstack=null;
		private IPOItemGroup(){
			super(IPOMod.ID);
		}
		
		@Override
		public ItemStack createIcon(){
			if(this.iconstack==null)
				this.iconstack=new ItemStack(IPOStuff.post_Base);
			return this.iconstack;
		}
		
		@Override
		public boolean equals(Object obj){
			if(obj==this || obj instanceof IPOItemGroup) return true;
			
			return super.equals(obj);
		}
	}
}
