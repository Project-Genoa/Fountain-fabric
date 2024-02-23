package micheal65536.fountain.fabric.core.noteblock.mixins;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.enums.Instrument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Replaces note block instruments that don't exist in Minecraft Earth with harp instrument
@Mixin(AbstractBlock.Settings.class)
public class AbstractBlockSettingsMixin
{
	@Inject(method = "instrument(Lnet/minecraft/block/enums/Instrument;)Lnet/minecraft/block/AbstractBlock$Settings;", at = @At(value = "TAIL"), cancellable = false)
	private void instrument(Instrument instrument, CallbackInfoReturnable<AbstractBlock.Settings> callbackInfo)
	{
		switch (instrument)
		{
			case FLUTE:
			case BELL:
			case GUITAR:
			case CHIME:
			case XYLOPHONE:
			case IRON_XYLOPHONE:
			case COW_BELL:
			case DIDGERIDOO:
			case BIT:
			case BANJO:
				((AbstractBlockSettingsAccessor) this).setInstrument(Instrument.HARP);
				break;
		}
	}

	@Mixin(AbstractBlock.Settings.class)
	public interface AbstractBlockSettingsAccessor
	{
		@Accessor("instrument")
		void setInstrument(Instrument instrument);
	}
}