package at.donmatzos.prismaticrealms.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import at.donmatzos.prismaticrealms.armor.ArmorSetEffects;

/** Lets armor set bonuses (Cruel Sun, Storm Fang) change the damage of a player's melee hit; Fabric has no event for that. */
@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageMixin {
	@ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true)
	private float prismaticrealms$setBonusDamage(float amount, ServerLevel level, DamageSource source) {
		return ArmorSetEffects.modifyAttackDamage((LivingEntity) (Object) this, source, amount);
	}
}
