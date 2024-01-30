package micheal65536.fountain.fabric.core.itembehavior.mixins;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BarrierBlock;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.PowderSnowBucketItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// dear Mojang, you already have PlayerAbilities.creativeMode to use for block breaking/drops/take item, please make sure to actually use it for all block breaking/drops/take item
// this fixes your badly-written blocks and items so that they use the player abilities like all the rest of your blocks and items do, rather than the PlayerEntity.isCreative() function
// PlayerEntity.isCreative() is supposed to be used for mob targeting and similar, not building/breaking
@Mixin({
		AbstractSignBlock.class,
		BarrierBlock.class,
		BeehiveBlock.class,
		CakeBlock.class,
		//ChiseledBookshelfBlock.class,
		DecoratedPotBlock.class,
		ShulkerBoxBlock.class,
		TntBlock.class,
		PowderSnowBucketItem.class,
		SwordItem.class,
		TridentItem.class
})
public class UsePlayerAbilitiesMixin
{
	@Redirect(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isCreative()Z"))
	private boolean isCreative(PlayerEntity playerEntity)
	{
		return playerEntity.getAbilities().creativeMode;
	}
}