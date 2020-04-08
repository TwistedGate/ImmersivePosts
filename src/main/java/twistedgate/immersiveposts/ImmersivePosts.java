package twistedgate.immersiveposts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import twistedgate.immersiveposts.client.ClientProxy;
import twistedgate.immersiveposts.common.CommonProxy;

/**
 * @author TwistedGate
 */
@Mod(IPOMod.ID)
public class ImmersivePosts{
	
	public static final ItemGroup creativeTab=new ItemGroup(IPOMod.ID){
		@Override
		public ItemStack createIcon(){
			return new ItemStack(IPOStuff.post_Base==null?Items.BARRIER:IPOStuff.post_Base);
		}
	};
	
	public static CommonProxy proxy=DistExecutor.runForDist(()->ClientProxy::new, ()->CommonProxy::new);
	public static Logger log;
	
	public ImmersivePosts(){
		log=LogManager.getLogger(IPOMod.ID);
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::violation);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
		
		IPOStuff.populate();
		
		proxy.construct();
	}
	
	public void setup(FMLCommonSetupEvent event){
		proxy.setup();
	}
	
	public void loadComplete(FMLLoadCompleteEvent event){
		proxy.completed();
	}
	
	public void violation(FMLFingerprintViolationEvent event){
		System.err.println("\r\nTHIS IS NOT AN OFFICIAL BUILD OF "+IPOMod.NAME.toUpperCase()+"! Fingerprints: ["+event.getFingerprints()+"]");
		System.err.println("If you didnt download this mod from the official curse page: Stop Drop and Roll this jar right out of your mods folder!\r\n");
		// Guess thats what this would be used for? lol
	}
}
