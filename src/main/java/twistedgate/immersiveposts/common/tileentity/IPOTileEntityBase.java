package twistedgate.immersiveposts.common.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class IPOTileEntityBase extends BlockEntity{
	public IPOTileEntityBase(BlockEntityType<? extends BlockEntity> pType, BlockPos pWorldPosition, BlockState pBlockState){
		super(pType, pWorldPosition, pBlockState);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket(){
		return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
	}
	
	@Override
	public void handleUpdateTag(CompoundTag tag){
		load(tag);
	}
	
	@Override
	public @NotNull CompoundTag getUpdateTag(){
		CompoundTag nbt = new CompoundTag();
		saveAdditional(nbt);
		return nbt;
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt){
		load(Objects.requireNonNull(pkt.getTag()));
	}
	
	@Override
	public void saveAdditional(@NotNull CompoundTag compound){
		super.saveAdditional(compound);
		writeCustom(compound);
	}
	
	@Override
	public void load(@NotNull CompoundTag compound){
		super.load(compound);
		readCustom(compound);
	}
	
	protected abstract void writeCustom(CompoundTag compound);
	
	protected abstract void readCustom(CompoundTag compound);
}
