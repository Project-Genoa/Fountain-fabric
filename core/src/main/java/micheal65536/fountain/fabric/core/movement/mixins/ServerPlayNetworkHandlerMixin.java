package micheal65536.fountain.fabric.core.movement.mixins;

import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import micheal65536.fountain.fabric.core.earthmode.EarthModePlayer;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
	// allows Earth players to move freely (otherwise they will still get rejected for moving too quickly or going inside blocks even though they're allowed to fly)
	@Inject(method = "onPlayerMove(Lnet/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket;)V", at = @At(value = "HEAD"), cancellable = true)
	private void onPlayerMove(PlayerMoveC2SPacket playerMoveC2SPacket, CallbackInfo callbackInfo)
	{
		ServerPlayNetworkHandler serverPlayNetworkHandler = (ServerPlayNetworkHandler) (Object) this;
		NetworkThreadUtils.forceMainThread(playerMoveC2SPacket, serverPlayNetworkHandler, serverPlayNetworkHandler.player.getServerWorld());
		if (((EarthModePlayer) serverPlayNetworkHandler.player).isEarthMode())
		{
			if (!serverPlayNetworkHandler.player.notInAnyWorld)
			{
				if (((ServerPlayNetworkHandlerAccessor) serverPlayNetworkHandler).getRequestedTeleportPos() != null)
				{
					((ServerPlayNetworkHandlerAccessor) serverPlayNetworkHandler).setRequestedTeleportPos(null);
				}

				double x = ServerPlayNetworkHandlerAccessor.clampHorizontal(playerMoveC2SPacket.getX(serverPlayNetworkHandler.player.getX()));
				double y = ServerPlayNetworkHandlerAccessor.clampVertical(playerMoveC2SPacket.getY(serverPlayNetworkHandler.player.getY()));
				double z = ServerPlayNetworkHandlerAccessor.clampHorizontal(playerMoveC2SPacket.getZ(serverPlayNetworkHandler.player.getZ()));
				float yaw = MathHelper.wrapDegrees(playerMoveC2SPacket.getYaw(serverPlayNetworkHandler.player.getYaw()));
				float pitch = MathHelper.wrapDegrees(playerMoveC2SPacket.getPitch(serverPlayNetworkHandler.player.getPitch()));
				boolean onGround = playerMoveC2SPacket.isOnGround();

				serverPlayNetworkHandler.player.updatePositionAndAngles(x, y, z, yaw, pitch);
				serverPlayNetworkHandler.player.setOnGround(onGround);
				serverPlayNetworkHandler.player.getServerWorld().getChunkManager().updatePosition(serverPlayNetworkHandler.player);
				((ServerPlayNetworkHandlerAccessor) serverPlayNetworkHandler).setUpdatedX(x);
				((ServerPlayNetworkHandlerAccessor) serverPlayNetworkHandler).setUpdatedY(y);
				((ServerPlayNetworkHandlerAccessor) serverPlayNetworkHandler).setUpdatedZ(z);
				((ServerPlayNetworkHandlerAccessor) serverPlayNetworkHandler).setFloating(false);

				callbackInfo.cancel();
			}
		}
	}

	@Mixin(ServerPlayNetworkHandler.class)
	public interface ServerPlayNetworkHandlerAccessor
	{
		@Accessor("requestedTeleportPos")
		Vec3d getRequestedTeleportPos();

		@Accessor("requestedTeleportPos")
		void setRequestedTeleportPos(Vec3d requestedTeleportPos);

		@Accessor("updatedX")
		void setUpdatedX(double updatedX);

		@Accessor("updatedY")
		void setUpdatedY(double updatedY);

		@Accessor("updatedZ")
		void setUpdatedZ(double updatedZ);

		@Accessor("floating")
		void setFloating(boolean floating);

		@Invoker("clampHorizontal")
		static double clampHorizontal(double horizontal)
		{
			throw new AssertionError();
		}

		@Invoker("clampVertical")
		static double clampVertical(double vertical)
		{
			throw new AssertionError();
		}
	}
}