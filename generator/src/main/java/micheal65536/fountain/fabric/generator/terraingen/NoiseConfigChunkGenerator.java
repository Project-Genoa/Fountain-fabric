package micheal65536.fountain.fabric.generator.terraingen;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.jetbrains.annotations.NotNull;

public interface NoiseConfigChunkGenerator
{
	NoiseConfig getNoiseConfig(@NotNull DynamicRegistryManager dynamicRegistryManager, long seed);
}