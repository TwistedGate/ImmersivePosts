package twistedgate.immersiveposts;

/**
 * Class holding informations about Immersive Posts
 * @author TwistedGate
 */
public class ModInfo{
	public static final String ID			= "immersiveposts";
	public static final String NAME			= "Immersive Posts";
	public static final String DEPENDING	= "required-after:forge@[14.23.5.2768,];required-after:immersiveengineering@[0.12,]";
	public static final String PROXY_CLIENT	= "twistedgate.immersiveposts.client.ClientProxy";
	public static final String PROXY_SERVER	= "twistedgate.immersiveposts.common.CommonProxy";
	public static final String CERT_PRINT	= "0ba25d8c0ec23537afc6db926e1ea764335a33b1";
	public static final String UPDATE_URL	= "https://raw.githubusercontent.com/TwistedGate/ImmersivePosts/master/change.json";
}
