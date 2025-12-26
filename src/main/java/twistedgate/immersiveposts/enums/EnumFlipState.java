package twistedgate.immersiveposts.enums;

import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;
import java.util.Locale;

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
