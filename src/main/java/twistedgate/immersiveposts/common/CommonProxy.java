package twistedgate.immersiveposts.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author TwistedGate
 */
public class CommonProxy{
	public void preInitStart(FMLPreInitializationEvent event){}
	public void preInitEnd(FMLPreInitializationEvent event){}
	
	public void initStart(FMLInitializationEvent event){}
	public void initEnd(FMLInitializationEvent event){}
	
	public void postInitStart(FMLPostInitializationEvent event){}
	public void postInitEnd(FMLPostInitializationEvent event){}
}
