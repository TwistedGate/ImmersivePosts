package twistedgate.immersiveposts.enums;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

/**
 * @author TwistedGate
 */
public enum EnumPostType implements IStringSerializable{
	POST,
	POST_TOP,
	ARM,
	ARM_DOUBLE,
	EMPTY
	;
	
	private EnumPostType(){}
	
	public int id(){
		return ordinal();
	}
	
	@Override
	public String getName(){
		return this.toString().toLowerCase(Locale.ENGLISH);
	}
}
