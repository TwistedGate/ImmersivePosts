package twistedgate.immersiveposts.enums;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

/**
 * @author TwistedGate
 */
public enum EnumPostType implements IStringSerializable{
	POST,
	POST_TOP,
	ARM, // Includes the two-way arm
	ARM_DOUBLE,
	EMPTY
	;
	
	public int id(){
		return ordinal();
	}
	
	@Override
	public String getString(){
		return toString();
	}
	
	@Override
	public String toString(){
		return name().toLowerCase(Locale.ENGLISH);
	}
}
