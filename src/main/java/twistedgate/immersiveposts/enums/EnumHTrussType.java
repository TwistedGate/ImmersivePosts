package twistedgate.immersiveposts.enums;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

/**
 * Horizontal Truss
 * 
 * @author TwistedGate
 */
public enum EnumHTrussType implements IStringSerializable{
	SINGLE,
	MULTI_A,
	MULTI_B,
	MULTI_C,
	MULTI_D_EVEN,
	MULTI_D_ODD
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
