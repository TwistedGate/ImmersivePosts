package twistedgate.immersiveposts.enums;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

/**
 * @author TwistedGate
 */
public enum EnumFlipState implements IStringSerializable{
	UP,
	DOWN,
	BOTH;
	
	public int id(){
		return ordinal();
	}
	
	@Override
	public String getName(){
		return toString();
	}
	
	@Override
	public String toString(){
		return name().toLowerCase(Locale.ENGLISH);
	}
}
