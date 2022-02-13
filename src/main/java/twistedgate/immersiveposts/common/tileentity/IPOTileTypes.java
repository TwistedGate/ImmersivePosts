package twistedgate.immersiveposts.common.tileentity;

import java.util.function.Supplier;

import com.google.common.collect.ImmutableSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.IPOContent;

public class IPOTileTypes{
	public static DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, IPOMod.ID);
	
	public static final RegistryObject<BlockEntityType<PostBaseTileEntity>> POST_BASE = register("postbase", PostBaseTileEntity::new, IPOContent.Blocks.POST_BASE::get);
	
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntitySupplier<T> factory, Supplier<Block> valid){
		return REGISTER.register(name, () -> new BlockEntityType<T>(factory, ImmutableSet.of(valid.get()), null));
	}
}
