package me.libraryaddict.disguise.disguisetypes;

import java.security.InvalidParameterException;
import me.libraryaddict.disguise.disguisetypes.watchers.DroppedItemWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.FallingBlockWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.PaintingWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SplashPotionWatcher;

import org.bukkit.Art;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class MiscDisguise extends TargetedDisguise {
    private int id = -1, data = 0;

    public MiscDisguise(DisguiseType disguiseType) {
        this(disguiseType, -1, -1);
    }

    @Deprecated
    public MiscDisguise(DisguiseType disguiseType, boolean replaceSounds) {
        this(disguiseType, replaceSounds, -1, -1);
    }

    @Deprecated
    public MiscDisguise(DisguiseType disguiseType, boolean replaceSounds, int addictionalData) {
        this(disguiseType, replaceSounds, (disguiseType == DisguiseType.FALLING_BLOCK
                || disguiseType == DisguiseType.DROPPED_ITEM ? addictionalData : -1), (disguiseType == DisguiseType.FALLING_BLOCK
                || disguiseType == DisguiseType.DROPPED_ITEM ? -1 : addictionalData));
    }

    @Deprecated
    public MiscDisguise(DisguiseType disguiseType, boolean replaceSounds, int id, int data) {
        this(disguiseType, id, data);
        this.setReplaceSounds(replaceSounds);
    }

    public MiscDisguise(DisguiseType disguiseType, int id) {
        this(disguiseType, id, -1);
    }

    public MiscDisguise(DisguiseType disguiseType, int firstParam, int secondParam) {
        if (!disguiseType.isMisc()) {
            throw new InvalidParameterException("Expected a non-living DisguiseType while constructing MiscDisguise. Received "
                    + disguiseType + " instead. Please use " + (disguiseType.isPlayer() ? "PlayerDisguise" : "MobDisguise")
                    + " instead");
        }
        createDisguise(disguiseType);
        this.id = getType().getEntityId();
        this.data = getType().getDefaultId();
        switch (disguiseType) {
        // The only disguises which should use a custom data.
        case PAINTING:
            ((PaintingWatcher) getWatcher()).setArt(Art.values()[Math.max(0, firstParam) % Art.values().length]);
            break;
        case FALLING_BLOCK:
            ((FallingBlockWatcher) getWatcher()).setBlock(new ItemStack(Math.max(1, firstParam), 1, (short) Math.max(0,
                    secondParam)));
            break;
        case SPLASH_POTION:
            ((SplashPotionWatcher) getWatcher()).setPotionId(Math.max(0, firstParam));
            break;
        case DROPPED_ITEM:
            if (firstParam > 0) {
                ((DroppedItemWatcher) getWatcher()).setItemStack(new ItemStack(firstParam, Math.max(0, secondParam)));
            }
            break;
        case FISHING_HOOK:
        case ARROW:
        case SMALL_FIREBALL:
        case FIREBALL:
        case WITHER_SKULL:
            this.data = firstParam;
            break;
        default:
            break;
        }
    }

    @Deprecated
    public MiscDisguise(EntityType entityType) {
        this(entityType, -1, -1);
    }

    @Deprecated
    public MiscDisguise(EntityType entityType, boolean replaceSounds) {
        this(entityType, replaceSounds, -1, -1);
    }

    @Deprecated
    public MiscDisguise(EntityType entityType, boolean replaceSounds, int id, int data) {
        this(DisguiseType.getType(entityType), replaceSounds, id, data);
    }

    @Deprecated
    public MiscDisguise(EntityType entityType, int id) {
        this(entityType, id, -1);
    }

    @Deprecated
    public MiscDisguise(EntityType disguiseType, int id, int data) {
        this(DisguiseType.getType(disguiseType), id, data);
    }

    @Override
    public MiscDisguise clone() {
        MiscDisguise disguise = new MiscDisguise(getType(), getData());
        disguise.setReplaceSounds(isSoundsReplaced());
        disguise.setViewSelfDisguise(isSelfDisguiseVisible());
        disguise.setHearSelfDisguise(isSelfDisguiseSoundsReplaced());
        disguise.setHideArmorFromSelf(isHidingArmorFromSelf());
        disguise.setHideHeldItemFromSelf(isHidingHeldItemFromSelf());
        disguise.setVelocitySent(isVelocitySent());
        disguise.setModifyBoundingBox(isModifyBoundingBox());
        disguise.setWatcher(getWatcher().clone(disguise));
        return disguise;
    }

    /**
     * This is the getId of everything but falling block.
     */
    public int getData() {
        switch (getType()) {
        case FALLING_BLOCK:
            return (int) ((FallingBlockWatcher) getWatcher()).getBlock().getDurability();
        case PAINTING:
            return ((PaintingWatcher) getWatcher()).getArt().getId();
        case SPLASH_POTION:
            return ((SplashPotionWatcher) getWatcher()).getPotionId();
        default:
            return data;
        }
    }

    /**
     * Only falling block should use this
     */
    public int getId() {
        if (getType() == DisguiseType.FALLING_BLOCK) {
            return ((FallingBlockWatcher) getWatcher()).getBlock().getTypeId();
        }
        return id;
    }

    public boolean isMiscDisguise() {
        return true;
    }

}