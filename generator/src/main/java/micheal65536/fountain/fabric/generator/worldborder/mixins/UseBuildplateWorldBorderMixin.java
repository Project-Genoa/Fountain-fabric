package micheal65536.fountain.fabric.generator.worldborder.mixins;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import micheal65536.fountain.fabric.generator.worldborder.BuildplateWorldBorder;

import java.util.function.Supplier;

@Mixin(World.class)
public class UseBuildplateWorldBorderMixin
{
	@Shadow
	@Mutable
	private WorldBorder border;

	@Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;border:Lnet/minecraft/world/border/WorldBorder;", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
	private void overrideWorldBorder(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, DynamicRegistryManager dynamicRegistryManager, RegistryEntry<DimensionType> registryEntry, Supplier<Profiler> supplier, boolean b1, boolean b2, long l, int i, CallbackInfo callbackInfo)
	{
		if (!((World) (Object) this).isClient())    // we only override the world border on the server, this allows the functionality to be properly tested independently of Minecraft Earth
		{
			this.border = new BuildplateWorldBorder((World) (Object) this);
		}
	}
}