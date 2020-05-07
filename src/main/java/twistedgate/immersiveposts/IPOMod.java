package twistedgate.immersiveposts;

/**
 * Class holding informations about Immersive Posts
 * @author TwistedGate
 */
public class IPOMod{
	public static final String ID			= "immersiveposts";
	public static final String NAME			= "Immersive Posts";
	public static final String VERSION		= "0.2.1";
	public static final String DEPENDS		= "required-after:forge@[14.23.5.2820,);required-after:immersiveengineering@[0.12,)";
	public static final String PROXY_CLIENT	= "twistedgate.immersiveposts.client.ClientProxy";
	public static final String PROXY_SERVER	= "twistedgate.immersiveposts.common.CommonProxy";
	public static final String CERT_PRINT	= "0ba8738eadcf158e7fe1452255a73a022fb15feb";
	public static final String UPDATE_URL	= "https://raw.githubusercontent.com/TwistedGate/ImmersivePosts/master/change.json";
}
