package at.donmatzos.prismaticrealms.artifact;

import java.util.function.BiConsumer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;

import at.donmatzos.prismaticrealms.PrismaticRealms;

/**
 * Base class for artifacts: items worn in the three artifact slots for passive bonuses. Override only what an artifact
 * needs. All hooks run on the server, only while the artifact is equipped. The bonus description (lang key
 * `<item key>.bonus`) is shown in the artifact screen and the tooltip.
 */
public abstract class ArtifactItem extends Item {
	protected ArtifactItem(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	/** Attribute modifiers while equipped. Use modifierId(...) for their ids so they never collide. */
	public void addModifiers(BiConsumer<Holder<Attribute>, AttributeModifier> out) {
	}

	/** Called once a second while equipped. */
	public void tick(ServerPlayer wearer) {
	}

	public void onHurt(ServerPlayer wearer, DamageSource source, float damage) {
	}

	public void onAttack(ServerPlayer wearer, LivingEntity target, DamageSource source, float damage) {
	}

	public void onBlockBreak(ServerPlayer wearer, BlockPos pos, BlockState state) {
	}

	public void onKill(ServerPlayer wearer, LivingEntity killed) {
	}

	public String bonusKey() {
		return this.getDescriptionId() + ".bonus";
	}

	/** A modifier id unique to this artifact: prismaticrealms:artifact/<item path>/<name>. */
	public Identifier modifierId(String name) {
		return PrismaticRealms.id("artifact/" + net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(this).getPath() + "/" + name);
	}
}
