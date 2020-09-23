package twistedgate.immersiveposts.common.tileentity;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;

public abstract class IPOTileEntityBase extends TileEntity{
	public IPOTileEntityBase(TileEntityType<?> tileEntityTypeIn){
		super(tileEntityTypeIn);
	}
	
	@Nonnull
	public World getWorldNonnull(){
		return Objects.requireNonNull(super.getWorld());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket(){
		return new SUpdateTileEntityPacket(this.pos, 3, getUpdateTag());
	}
	
	@Override
	public void handleUpdateTag(CompoundNBT tag){
		read(tag);
	}
	
	@Override
	public CompoundNBT getUpdateTag(){
		return write(new CompoundNBT());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
		read(pkt.getNbtCompound());
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound){
		super.write(compound);
		writeCustom(compound);
		return compound;
	}
	
	@Override
	public void read(CompoundNBT compound){
		super.read(compound);
		readCustom(compound);
	}
	
	protected abstract CompoundNBT writeCustom(CompoundNBT compound);
	
	protected abstract void readCustom(CompoundNBT compound);
}
