package net.boster.particles.main.particle;

import lombok.Getter;
import net.boster.particles.main.utils.Version;
import org.jetbrains.annotations.NotNull;

public enum EnumBosterParticle {

    EXPLOSION_NORMAL(Version.v1_8_R1),
    EXPLOSION_LARGE(Version.v1_8_R1),
    EXPLOSION_HUGE(Version.v1_8_R1),
    FIREWORKS_SPARK(Version.v1_8_R1),
    WATER_BUBBLE(Version.v1_8_R1),
    WATER_SPLASH(Version.v1_8_R1),
    WATER_WAKE(Version.v1_8_R1),
    SUSPENDED(Version.v1_8_R1),
    SUSPENDED_DEPTH(Version.v1_8_R1),
    CRIT(Version.v1_8_R1),
    CRIT_MAGIC(Version.v1_8_R1),
    SMOKE_NORMAL(Version.v1_8_R1),
    SMOKE_LARGE(Version.v1_8_R1),
    SPELL(Version.v1_8_R1),
    SPELL_INSTANT(Version.v1_8_R1),
    SPELL_MOB(Version.v1_8_R1),
    SPELL_MOB_AMBIENT(Version.v1_8_R1),
    SPELL_WITCH(Version.v1_8_R1),
    DRIP_WATER(Version.v1_8_R1),
    DRIP_LAVA(Version.v1_8_R1),
    VILLAGER_ANGRY(Version.v1_8_R1),
    VILLAGER_HAPPY(Version.v1_8_R1),
    TOWN_AURA(Version.v1_8_R1),
    NOTE(Version.v1_8_R1),
    PORTAL(Version.v1_8_R1),
    ENCHANTMENT_TABLE(Version.v1_8_R1),
    FLAME(Version.v1_8_R1),
    LAVA(Version.v1_8_R1),
    CLOUD(Version.v1_8_R1),
    REDSTONE(Version.v1_8_R1),
    SNOWBALL(Version.v1_8_R1),
    SNOW_SHOVEL(Version.v1_8_R1),
    SLIME(Version.v1_8_R1),
    HEART(Version.v1_8_R1),
    BARRIER(Version.v1_8_R1),
    ITEM_CRACK(Version.v1_8_R1),
    BLOCK_CRACK(Version.v1_8_R1),
    BLOCK_DUST(Version.v1_8_R1),
    WATER_DROP(Version.v1_8_R1),
    MOB_APPEARANCE(Version.v1_8_R1),
    DRAGON_BREATH(Version.v1_9_R1),
    END_ROD(Version.v1_9_R1),
    DAMAGE_INDICATOR(Version.v1_9_R1),
    SWEEP_ATTACK(Version.v1_9_R1),
    FALLING_DUST(Version.v1_10_R1),
    TOTEM(Version.v1_11_R1),
    SPIT(Version.v1_11_R1),
    SQUID_INK(Version.v1_13_R2),
    BUBBLE_POP(Version.v1_13_R2),
    CURRENT_DOWN(Version.v1_13_R2),
    BUBBLE_COLUMN_UP(Version.v1_13_R2),
    NAUTILUS(Version.v1_13_R2),
    DOLPHIN(Version.v1_13_R2),
    SNEEZE(Version.v1_14_R1),
    CAMPFIRE_COSY_SMOKE(Version.v1_14_R1),
    CAMPFIRE_SIGNAL_SMOKE(Version.v1_14_R1),
    COMPOSTER(Version.v1_14_R1),
    FLASH(Version.v1_14_R1),
    FALLING_LAVA(Version.v1_14_R1),
    LANDING_LAVA(Version.v1_14_R1),
    FALLING_WATER(Version.v1_14_R1),
    DRIPPING_HONEY(Version.v1_15_R1),
    FALLING_HONEY(Version.v1_15_R1),
    LANDING_HONEY(Version.v1_15_R1),
    FALLING_NECTAR(Version.v1_15_R1),
    SOUL_FIRE_FLAME(Version.v1_16_R1),
    ASH(Version.v1_16_R1),
    CRIMSON_SPORE(Version.v1_16_R1),
    WARPED_SPORE(Version.v1_16_R1),
    SOUL(Version.v1_16_R1),
    DRIPPING_OBSIDIAN_TEAR(Version.v1_16_R1),
    FALLING_OBSIDIAN_TEAR(Version.v1_16_R1),
    LANDING_OBSIDIAN_TEAR(Version.v1_16_R1),
    REVERSE_PORTAL(Version.v1_16_R1),
    WHITE_ASH(Version.v1_16_R1),
    LIGHT(Version.v1_17_R1),
    DUST_COLOR_TRANSITION(Version.v1_17_R1),
    VIBRATION(Version.v1_17_R1),
    FALLING_SPORE_BLOSSOM(Version.v1_17_R1),
    SPORE_BLOSSOM_AIR(Version.v1_17_R1),
    SMALL_FLAME(Version.v1_17_R1),
    SNOWFLAKE(Version.v1_17_R1),
    DRIPPING_DRIPSTONE_LAVA(Version.v1_17_R1),
    FALLING_DRIPSTONE_LAVA(Version.v1_17_R1),
    DRIPPING_DRIPSTONE_WATER(Version.v1_17_R1),
    FALLING_DRIPSTONE_WATER(Version.v1_17_R1),
    GLOW_SQUID_INK(Version.v1_17_R1),
    GLOW(Version.v1_17_R1),
    WAX_ON(Version.v1_17_R1),
    WAX_OFF(Version.v1_17_R1),
    ELECTRIC_SPARK(Version.v1_17_R1),
    SCRAPE(Version.v1_17_R1),
    BLOCK_MARKER(Version.v1_18_R1);

    @Getter @NotNull private final Version version;

    EnumBosterParticle(@NotNull Version version) {
        this.version = version;
    }
}
