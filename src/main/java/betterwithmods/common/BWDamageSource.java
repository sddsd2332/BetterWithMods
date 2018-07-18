package betterwithmods.common;

import betterwithmods.event.FakePlayerHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BWDamageSource extends DamageSource {
    public static final BWDamageSource gloom = new BWDamageSource("gloom", true);
    public static final BWDamageSource growth = new BWDamageSource("growth", false);
    public static final BWDamageSource squid = new BWDamageSource("squid", false);
    private static FakeDamageSource saw = null;
    private static FakeDamageSource choppingBlock = null;

    protected BWDamageSource(String name, boolean ignoreArmor) {
        super(name);
        if (ignoreArmor)
            setDamageBypassesArmor();
    }

    public static FakeDamageSource getSawDamage() {
        if (saw != null)
            return saw;
        if (FakePlayerHandler.getSword() != null)
            return saw = new FakeDamageSource("saw", FakePlayerHandler.getSword());
        return null;
    }

    public static FakeDamageSource getChoppingBlockDamage() {
        if (choppingBlock != null)
            return choppingBlock;
        if (FakePlayerHandler.getSword() != null)
            return choppingBlock = new FakeDamageSource("chopping_block", FakePlayerHandler.getSword());
        return null;
    }

    public static class FakeDamageSource extends EntityDamageSource {
        public final String message;

        public FakeDamageSource(String message, EntityPlayer player) {
            super("player", player);
            this.message = message;
        }

        @Override
        public boolean isDifficultyScaled() {
            return false;
        }

        @Override
        public boolean isUnblockable() {
            return false;
        }

        @Override
        public boolean getIsThornsDamage() {
            return false;
        }

        @Nonnull
        @Override
        public ITextComponent getDeathMessage(EntityLivingBase killed) {
            return new TextComponentTranslation("death.attack." + message, killed.getDisplayName());
        }

        @Nullable
        @Override
        public Entity getTrueSource() {
            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof FakeDamageSource)
                return ((FakeDamageSource) o).message.equals(this.message);
            return false;
        }
    }
}
