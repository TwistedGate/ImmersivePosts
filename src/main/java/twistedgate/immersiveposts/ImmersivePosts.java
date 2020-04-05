package twistedgate.immersiveposts;

import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
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

	//
	// Item group / creative tab
	//
	public static final ItemGroup ITEMGROUP = (new ItemGroup("tab" + IPOMod.ID) {
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(IPOStuff.postBase == null ? Items.STONE_BRICKS : IPOStuff.postBase);
		}
		   // --> Item group is a very small class, can also be inlined as above, we only need to change one method.
		  	//		public static class IPOItemGroup extends ItemGroup{
				//			private ItemStack iconstack=null;
				//			private IPOItemGroup(){
				//				super(IPOMod.ID);
				//			}
				//
				//			@Override
				//			public ItemStack createIcon(){
				//				if(this.iconstack==null)
				//					this.iconstack=new ItemStack(IPOStuff.postBase);
				//				return this.iconstack;
				//			}
				//
				//			@Override
				//			public boolean equals(Object obj){
				//				if(obj==this || obj instanceof IPOItemGroup) return true;
				//
				//				return super.equals(obj);
				//			}
				//		}
				//public static final ItemGroup creativeTab = new IPOItemGroup();
	});

	public static CommonProxy proxy = DistExecutor.runForDist(()->ClientProxy::new, ()->CommonProxy::new);
	public static Logger log;
	
	public ImmersivePosts(){
		log=LogManager.getLogger(IPOMod.ID);
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::violation);
		
		ModLoadingContext.get().registerConfig(Type.COMMON, IPOConfig.VALUES);

		proxy.construct();
	}
	
	public void setup(FMLCommonSetupEvent event){

		// @todo: --> preInit and postInit are not needed anymore in 1.14.
		// proxy is now just client and server, mods may have common proxy from 1.12 porting yet, but is't now all
		// done with dedicated Forge events.
		proxy.preInit();
		proxy.postInit();
	}
	
	public void violation(FMLFingerprintViolationEvent event){
		System.err.println("THIS IS NOT AN OFFICIAL BUILD OF "+IPOMod.NAME.toUpperCase()+"! Fingerprints: ["+event.getFingerprints()+"]");
		// Guess thats what this would be used for? lol
	}
	
	

}
