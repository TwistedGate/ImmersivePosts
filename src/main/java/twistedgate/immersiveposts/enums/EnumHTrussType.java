package twistedgate.immersiveposts.enums;

import java.util.Locale;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

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
	
	@Override
	public @NotNull String getSerializedName(){
		return toString();
	}
	
	@Override
	public String toString(){
		return name().toLowerCase(Locale.ENGLISH);
	}
}
