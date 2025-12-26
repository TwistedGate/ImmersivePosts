package twistedgate.immersiveposts.enums;

import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * Horizontal Truss
 * 
 * @author TwistedGate
 */
public enum EnumHTrussType implements StringRepresentable{
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
	
	@Nonnull
	@Override
	public String getSerializedName(){
		return toString();
	}
	
	@Override
	public String toString(){
		return name().toLowerCase(Locale.ENGLISH);
	}
}
