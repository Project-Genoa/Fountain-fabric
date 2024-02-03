package micheal65536.fountain.fabric.generator.terraingen.mixins;

import com.mojang.datafixers.DataFixer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.thread.ThreadExecutor;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.ChunkStatusChangeListener;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.level.storage.LevelStorage;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import micheal65536.fountain.fabric.generator.terraingen.NoiseConfigChunkGenerator;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Mixin(ThreadedAnvilChunkStorage.class)
public class CustomChunkGeneratorNoiseConfigMixin
{
	@Shadow
	@Mutable
	private NoiseConfig noiseConfig;

	@Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;noiseConfig:Lnet/minecraft/world/gen/noise/NoiseConfig;", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
	private void overrideNoiseConfig(ServerWorld serverWorld, LevelStorage.Session session, DataFixer dataFixer, StructureTemplateManager structureTemplateManager, Executor executor, ThreadExecutor<Runnable> threadExecutor, ChunkProvider chunkProvider, ChunkGenerator chunkGenerator, WorldGenerationProgressListener worldGenerationProgressListener, ChunkStatusChangeListener chunkStatusChangeListener, Supplier<PersistentStateManager> supplier, int i, boolean b, CallbackInfo callbackInfo)
	{
		if (chunkGenerator instanceof NoiseConfigChunkGenerator)
		{
			NoiseConfig noiseConfig = ((NoiseConfigChunkGenerator) chunkGenerator).getNoiseConfig(serverWorld.getRegistryManager(), serverWorld.getSeed());
			this.noiseConfig = noiseConfig;
		}
	}
}