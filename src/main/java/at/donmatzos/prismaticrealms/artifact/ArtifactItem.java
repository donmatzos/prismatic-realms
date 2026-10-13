package at.donmatzos.prismaticrealms.artifact;

import java.util.List;
import java.util.function.BiConsumer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import at.donmatzos.prismaticrealms.PrismaticRealms;

/**
 * Base class for artifacts: items worn in the three artifact slots for passive bonuses. Override only what an artifact
 * needs. Every artifact has the enchantment glint, to set it apart from plain crystals. All hooks run on the server, only while the artifact is equipped. The bonus description (lang key
 * `<item key>.bonus`) is shown in the artifact screen and the tooltip.
 */
public abstract class ArtifactItem extends Item {
	protected ArtifactItem(Item.Properties properties) {
		super(properties.stacksTo(1).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
	}

	/** Attribute modifiers while equipped. Use modifierId(...) for their ids so they never collide. */
	public void addModifiers(BiConsumer<Holder<Attribute>, AttributeModifier> out) {
	}

	/** Called once a second while equipped. */
	public void tick(ServerPlayer wearer) {
	}

	/** Called every server tick while equipped, for bonuses that follow the wearer's movement. Keep it cheap. */
	public void tickMovement(ServerPlayer wearer) {
	}

	/** True cancels this damage before it's dealt (Fabric ALLOW_DAMAGE). */
	public boolean preventsDamage(ServerPlayer wearer, DamageSource source) {
		return false;
	}

	/**
	 * True when this artifact saves the wearer from lethal damage. It then breaks (leaves its slot, its modifiers go) and the
	 * wearer is healed to full. Not asked for damage that bypasses invulnerability (void, /kill), like the Totem of Undying.
	 */
	public boolean savesFromDeath(ServerPlayer wearer, DamageSource source) {
		return false;
	}

	/** Changes the drops of a block the wearer broke, while the loot is generated (before anything lands). */
	public void modifyBlockDrops(ServerPlayer wearer, LootContext context, List<ItemStack> drops) {
	}

	/** True makes the wearer immune to powder-snow freezing. Checked on both sides (the slots are synced to their owner). */
	public boolean preventsFreezing() {
		return false;
	}

	public void onHurt(ServerPlayer wearer, DamageSource source, float damage) {
	}

	public void onAttack(ServerPlayer wearer, LivingEntity target, DamageSource source, float damage) {
	}

	/** The wearer's direct melee hit on `target` was fully charged (attack charge > 0.9); sweep targets don't count. */
	public void onChargedHit(ServerPlayer wearer, LivingEntity target) {
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
