package twistedgate.immersiveposts.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;

/**
 * @author TwistedGate
 */
public class IPOBlockBase extends Block{
	public IPOBlockBase(Properties properties){
		super(properties);
	}
	
	@Override
	public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction){
		return state;
	}
	
	@Nonnull
	@Override
	protected InteractionResult useWithoutItem(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult hit){
		return use(state, level, pos, player, player.getUsedItemHand(), hit);
	}
	
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult hit){
		return InteractionResult.PASS;
	}
	
	/** convenience method */
	public static Block getBlockFrom(BlockGetter world, BlockPos pos){
		if(world == null || pos == null)
			return null;
		
		return world.getBlockState(pos).getBlock();
	}
}
