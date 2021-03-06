package client;

import net.LongValueHolder;

public enum MapleBuffStat implements LongValueHolder {
    WATK(0x1),
    WDEF(0x2),
    MATK(0x4),
    MDEF(0x8),
    ACC(0x10),
    AVOID(0x20),
    HANDS(0x40),
    SPEED(0x80),
    JUMP(0x100),
    MAGIC_GUARD(0x200),
    DARKSIGHT(0x400), // also used by gm hide
    BOOSTER(0x800),
    POWERGUARD(0x1000),
    HYPERBODYHP(0x2000),
    HYPERBODYMP(0x4000),
    INVINCIBLE(0x8000),
    SOULARROW(0x10000),
    COMBO(0x200000),
    SUMMON(0x200000), //hack buffstat for summons ^.- (does/should not increase damage... hopefully <3)
    WK_CHARGE(0x400000),
    DRAGONBLOOD(0x800000), // another funny buffstat...
    HOLY_SYMBOL(0x1000000),
    MESOUP(0x2000000),
    SHADOWPARTNER(0x4000000),
    PICKPOCKET(0x8000000),
    PUPPET(0x8000000), // HACK - shares buffmask with pickpocket - odin special ^.-
    MESOGUARD(0x10000000),
    RECOVERY(0x400000000l),
    STANCE(0x1000000000l),
    SHARP_EYES(0x2000000000l),
    MANA_REFLECTION(0x4000000000l),
    MAPLE_WARRIOR(0x800000000l),
    SHADOW_CLAW(0x10000000000l),
    INFINITY(0x20000000000l),
    HOLY_SHIELD(0x40000000000l),
    HAMSTRING(0x80000000000l),
    BLIND(0x100000000000l),
    CONCENTRATE(0x200000000000l), // another no op buff
    MONSTER_RIDING(0x400000000000l),
    ECHO_OF_HERO(0x1000000000000l);
    private final long i;

    private MapleBuffStat(long i) {
        this.i = i;
    }

    @Override
    public long getValue() {
        return i;
    }
}
