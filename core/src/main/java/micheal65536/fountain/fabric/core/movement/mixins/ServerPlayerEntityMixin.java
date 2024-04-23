package micheal65536.fountain.fabric.core.movement.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import micheal65536.fountain.fabric.core.earthmode.EarthModePlayer;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity
{
	// this is the absolute f*cking ugliest cr*p in the entire mixin API and I hate it
	// dear Fabric developers: please just make a f*cking annotation like @Overwrite that allows overriding methods from the parent class while also supporting obfuscation/remapping instead of this completely braindead "extend the super class of the target class and then fudge around with the constructor" cr*p
	private ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile)
	{
		super(world, pos, yaw, gameProfile);
	}

	// prevent Earth players from riding entities
	@Override
	public boolean canStartRiding(Entity entity)
	{
		if (((EarthModePlayer) this).isEarthMode())
		{
			return false;
		}
		else
		{
			return super.canStartRiding(entity);
		}
	}
}