package twistedgate.immersiveposts.enums;

import java.util.Locale;

import net.minecraft.util.StringRepresentable;

/**
 * @author TwistedGate
 */
public enum EnumFlipState implements StringRepresentable{
	UP,
	DOWN,
	BOTH;
	
	public int id(){
		return ordinal();
	}
	
	@Override
	public String getSerializedName(){
		return toString();
	}
	
	@Override
	public String toString(){
		return name().toLowerCase(Locale.ENGLISH);
	}
}
