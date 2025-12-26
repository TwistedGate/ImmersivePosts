package twistedgate.immersiveposts.common.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.Objects;

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
		return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
	}
	
	@Override
	public void handleUpdateTag(@Nonnull CompoundTag tag, @Nonnull HolderLookup.Provider provider){
		loadAdditional(tag, provider);
	}
	
	@Nonnull
	@Override
	public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider provider){
		CompoundTag nbt = new CompoundTag();
		saveAdditional(nbt, provider);
		return nbt;
	}
	
	@Override
	public void onDataPacket(@Nonnull Connection net, ClientboundBlockEntityDataPacket pkt, @Nonnull HolderLookup.Provider provider){
		loadAdditional(pkt.getTag(), provider);
	}
	
	@Override
	public void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider){
		super.saveAdditional(compound, provider);
		writeCustom(compound, provider);
	}
	
	@Override
	public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider){
		super.loadAdditional(compound, provider);
		readCustom(compound, provider);
	}
	
	protected abstract CompoundTag writeCustom(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider);
	
	protected abstract void readCustom(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider);
}
