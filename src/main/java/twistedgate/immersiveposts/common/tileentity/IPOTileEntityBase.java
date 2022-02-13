package twistedgate.immersiveposts.common.tileentity;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class IPOTileEntityBase extends BlockEntity{
	public IPOTileEntityBase(BlockEntityType<? extends BlockEntity> pType, BlockPos pWorldPosition, BlockState pBlockState){
		super(pType, pWorldPosition, pBlockState);
	}

	@Nonnull
	public Level getWorldNonnull(){
		return Objects.requireNonNull(super.getLevel());
	}
	
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket(){
		return ClientboundBlockEntityDataPacket.create(this, (be) -> be.getUpdateTag());
	}
	
	@Override
	public void handleUpdateTag(CompoundTag tag){
		load(tag);
	}
	
	@Override
	public CompoundTag getUpdateTag(){
		CompoundTag nbt = new CompoundTag();
		save(nbt);
		return nbt;
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt){
		load(pkt.getTag());
	}
	
	@Override
	public CompoundTag save(CompoundTag compound){
		super.save(compound);
		writeCustom(compound);
		return compound;
	}
	
	@Override
	public void load(CompoundTag compound){
		super.load(compound);
		readCustom(compound);
	}
	
	protected abstract CompoundTag writeCustom(CompoundTag compound);
	
	protected abstract void readCustom(CompoundTag compound);
}
