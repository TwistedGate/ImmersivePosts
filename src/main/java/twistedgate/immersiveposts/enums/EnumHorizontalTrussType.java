package twistedgate.immersiveposts.enums;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

public enum EnumHorizontalTrussType implements IStringSerializable{
	HORIZONTAL_A,
	HORIZONTAL_B,
	HORIZONTAL_C,
	HORIZONTAL_D
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
