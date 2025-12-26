package twistedgate.immersiveposts.enums;

import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * @author TwistedGate
 */
public enum EnumPostType implements StringRepresentable{
	POST,
	POST_TOP,
	ARM, // Includes the two-way arm
	ARM_DOUBLE,
	EMPTY
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
