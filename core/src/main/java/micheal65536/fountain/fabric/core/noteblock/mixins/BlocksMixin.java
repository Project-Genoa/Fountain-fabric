package micheal65536.fountain.fabric.core.noteblock.mixins;

import net.minecraft.block.Blocks;
import net.minecraft.block.enums.Instrument;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Replaces note block instruments that don't exist in Minecraft Earth with harp instrument
@Mixin(Blocks.class)
public class BlocksMixin
{
	@Redirect(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/block/enums/Instrument;FLUTE:Lnet/minecraft/block/enums/Instrument;"))
	private static Instrument redirectInstrumentFlute()
	{
		return Instrument.HARP;
	}

	@Redirect(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/block/enums/Instrument;BELL:Lnet/minecraft/block/enums/Instrument;"))
	private static Instrument redirectInstrumentBell()
	{
		return Instrument.HARP;
	}

	@Redirect(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/block/enums/Instrument;GUITAR:Lnet/minecraft/block/enums/Instrument;"))
	private static Instrument redirectInstrumentGuitar()
	{
		return Instrument.HARP;
	}

	@Redirect(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/block/enums/Instrument;CHIME:Lnet/minecraft/block/enums/Instrument;"))
	private static Instrument redirectInstrumentChime()
	{
		return Instrument.HARP;
	}

	@Redirect(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/block/enums/Instrument;XYLOPHONE:Lnet/minecraft/block/enums/Instrument;"))
	private static Instrument redirectInstrumentXylophone()
	{
		return Instrument.HARP;
	}

	@Redirect(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/block/enums/Instrument;IRON_XYLOPHONE:Lnet/minecraft/block/enums/Instrument;"))
	private static Instrument redirectInstrumentIronXylophone()
	{
		return Instrument.HARP;
	}

	@Redirect(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/block/enums/Instrument;COW_BELL:Lnet/minecraft/block/enums/Instrument;"))
	private static Instrument redirectInstrumentCowBell()
	{
		return Instrument.HARP;
	}

	@Redirect(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/block/enums/Instrument;DIDGERIDOO:Lnet/minecraft/block/enums/Instrument;"))
	private static Instrument redirectInstrumentDidgeridoo()
	{
		return Instrument.HARP;
	}

	@Redirect(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/block/enums/Instrument;BIT:Lnet/minecraft/block/enums/Instrument;"))
	private static Instrument redirectInstrumentBit()
	{
		return Instrument.HARP;
	}

	@Redirect(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/block/enums/Instrument;BANJO:Lnet/minecraft/block/enums/Instrument;"))
	private static Instrument redirectInstrumentBanjo()
	{
		return Instrument.HARP;
	}
}