package twistedgate.immersiveposts.common.blocks;

import javax.annotation.Nonnull;

import twistedgate.immersiveposts.enums.EnumPostMaterial;

public class BlockGenericPost extends IPOBlockBase{
	protected final EnumPostMaterial postMaterial;
	public BlockGenericPost(EnumPostMaterial postMaterial, @Nonnull String name_post){
		super(postMaterial.getString() + name_post, postMaterial.getProperties());
		this.postMaterial = postMaterial;
	}
	
	public final EnumPostMaterial getPostMaterial(){
		return this.postMaterial;
	}
}
