package twistedgate.immersiveposts.common;

import java.util.function.Supplier;

import com.google.common.collect.ImmutableSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraftforge.registries.RegistryObject;
import twistedgate.immersiveposts.common.tileentity.PostBaseTileEntity;

public class IPOTileTypes{
	public static final RegistryObject<BlockEntityType<PostBaseTileEntity>> POST_BASE;
	
	static{
		POST_BASE = register("postbase", PostBaseTileEntity::new, IPOContent.Blocks.POST_BASE::get);
	}

	@SuppressWarnings("all")
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntitySupplier<T> factory, Supplier<Block> valid){
		return IPORegistries.TILE_REGISTER.register(name, () -> new BlockEntityType<>(factory, ImmutableSet.of(valid.get()), null));
	}
	
	protected static void forceClassLoad(){
	}
}
