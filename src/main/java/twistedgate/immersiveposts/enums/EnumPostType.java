package twistedgate.immersiveposts.enums;

import java.util.Locale;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

/**
 * @author TwistedGate
 */
public enum EnumPostType implements StringRepresentable {
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
	public @NotNull String getSerializedName(){
		return toString();
	}
	
	@Override
	public String toString(){
		return name().toLowerCase(Locale.ENGLISH);
	}
}
