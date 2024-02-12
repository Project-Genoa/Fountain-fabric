package micheal65536.fountain.fabric.core.piston.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import micheal65536.fountain.fabric.core.piston.BedrockCompatiblePistonBlockEntity;

// provide update packet for PistonBlockEntity
// also include piston base position and pushed block state ID in packet
@Mixin(PistonBlockEntity.class)
public class PistonBlockEntityMixin implements BedrockCompatiblePistonBlockEntity
{
	private BlockPos basePos = null;

	public Packet<ClientPlayPacketListener> toUpdatePacket()
	{
		return BlockEntityUpdateS2CPacket.create((PistonBlockEntity) (Object) this);
	}

	@Override
	public void setBasePos(BlockPos basePos)
	{
		this.basePos = basePos;
	}

	@Inject(method = "readNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At(value = "TAIL"))
	private void readNbt(NbtCompound nbtCompound, CallbackInfo callbackInfo)
	{
		if (nbtCompound.contains("basePos"))
		{
			NbtCompound basePosNbtCompound = nbtCompound.getCompound("basePos");
			this.basePos = new BlockPos(basePosNbtCompound.getInt("x"), basePosNbtCompound.getInt("y"), basePosNbtCompound.getInt("z"));
		}
		else
		{
			this.basePos = null;
		}
	}

	@Inject(method = "writeNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At(value = "TAIL"))
	private void writeNbt(NbtCompound nbtCompound, CallbackInfo callbackInfo)
	{
		if (this.basePos != null)
		{
			NbtCompound basePosNbtCompound = new NbtCompound();
			basePosNbtCompound.putInt("x", this.basePos.getX());
			basePosNbtCompound.putInt("y", this.basePos.getY());
			basePosNbtCompound.putInt("z", this.basePos.getZ());
			nbtCompound.put("basePos", basePosNbtCompound);
		}

		nbtCompound.putInt("blockStateId", Block.getRawIdFromState(((PistonBlockEntityAccessor) this).getPushedBlock()));
	}

	@Mixin(PistonBlockEntity.class)
	public interface PistonBlockEntityAccessor
	{
		@Accessor("pushedBlock")
		BlockState getPushedBlock();
	}
}