package twistedgate.immersiveposts.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumPostType implements IStringSerializable{
	POST("post"),
	POST_TOP("post_top"),
	ARM("arm")
	;
	
	final String name;
	private EnumPostType(String name){
		this.name=name;
	}
	
	@Override
	public String getName(){
		return this.name;
	}
}
