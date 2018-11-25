package betterwithmods.module.general.moreheads.common;

import betterwithmods.library.utils.DirUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

public enum HeadType implements IStringSerializable {


    BLAZE("blaze", "entity.Blaze.name", DirUtils.CENTER, DirUtils.CENTER,
            new Vec3d(0.5, 0.25, 0.75),
            new Vec3d(0.5, 0.25, 0.25),
            new Vec3d(0.75, 0.25, 0.5),
            new Vec3d(0.25, 0.25, 0.5)),
    PIGMAN("pigman", "entity.PigZombie.name", DirUtils.CENTER, DirUtils.CENTER,
            new Vec3d(0.5, 0.25, 0.75),
            new Vec3d(0.5, 0.25, 0.25),
            new Vec3d(0.75, 0.25, 0.5),
            new Vec3d(0.25, 0.25, 0.5)),
    COW("cow", "entity.Cow.name", DirUtils.CENTER, DirUtils.CENTER,
            new Vec3d(0.5, 0.25, 0.875),
            new Vec3d(0.5, 0.25, 0.125),
            new Vec3d(0.875, 0.25, 0.5),
            new Vec3d(0.125, 0.25, 0.5)),
    MUSHROOMCOW("mushroomcow", "entity.MushroomCow.name", DirUtils.CENTER, DirUtils.CENTER,
            new Vec3d(0.5, 0.25, 0.875),
            new Vec3d(0.5, 0.25, 0.125),
            new Vec3d(0.875, 0.25, 0.5),
            new Vec3d(0.125, 0.25, 0.5)),
    PIG("pig", "entity.Pig.name", DirUtils.CENTER, DirUtils.CENTER,
            new Vec3d(0.5, 0.25, 0.75),
            new Vec3d(0.5, 0.25, 0.25),
            new Vec3d(0.75, 0.25, 0.5),
            new Vec3d(0.25, 0.25, 0.5)),
    SHEEP("sheep", "entity.Sheep.name", DirUtils.CENTER, DirUtils.CENTER,
            new Vec3d(0.5, 0.25, 0.75),
            new Vec3d(0.5, 0.25, 0.25),
            new Vec3d(0.75, 0.25, 0.5),
            new Vec3d(0.25, 0.25, 0.5)),
    CHICKEN("chicken", "entity.Chicken.name", DirUtils.CENTER, DirUtils.CENTER,
            new Vec3d(0.5, 0.25, 0.9375),
            new Vec3d(0.5, 0.25, 0.0625),
            new Vec3d(0.9375, 0.25, 0.5),
            new Vec3d(0.0625, 0.25, 0.5)),
    ENDERMAN("enderman", "entity.Enderman.name", DirUtils.CENTER, DirUtils.CENTER,
            new Vec3d(0.5, 0.25, 0.75),
            new Vec3d(0.5, 0.25, 0.25),
            new Vec3d(0.75, 0.25, 0.5),
            new Vec3d(0.25, 0.25, 0.5)),
    SLIME("slime", "entity.Slime.name", DirUtils.CENTER, DirUtils.CENTER,
            new Vec3d(0.5, 0.25, 0.75),
            new Vec3d(0.5, 0.25, 0.25),
            new Vec3d(0.75, 0.25, 0.5),
            new Vec3d(0.25, 0.25, 0.5)),
    VILLAGER("villager", "entity.Villager.name", DirUtils.CENTER, DirUtils.CENTER,
            new Vec3d(0.5, 0.25, 0.75),
            new Vec3d(0.5, 0.25, 0.25),
            new Vec3d(0.75, 0.25, 0.5),
            new Vec3d(0.25, 0.25, 0.5)),
    WITCH("witch", "entity.Witch.name", DirUtils.CENTER, DirUtils.CENTER,
            new Vec3d(0.5, 0.25, 0.75),
            new Vec3d(0.5, 0.25, 0.25),
            new Vec3d(0.75, 0.25, 0.5),
            new Vec3d(0.25, 0.25, 0.5));


    public static HeadType[] VALUES = values();

    private Vec3d[] translations = new Vec3d[0];

    HeadType(String name, String entityUnlocalizedName, Vec3d... translations) {
        this.name = name;
        this.translations = translations;
        this.entityUnlocalizedName = entityUnlocalizedName;
    }

    private String name;
    private String entityUnlocalizedName;

    HeadType(String entityUnlocalizedName) {
        this.entityUnlocalizedName = entityUnlocalizedName;
    }

    public String getEntityUnlocalizedName() {
        return entityUnlocalizedName;
    }

    public Vec3d getItemTranslation() {
        return translations[0];
    }

    public Vec3d getTranslation(EnumFacing facing) {
        if (translations.length > facing.getIndex())
            return translations[facing.getIndex()];
        return Vec3d.ZERO;
    }

    public static HeadType getValue(String name) {
        for (HeadType type : VALUES) {
            if (type.getName().equals(name))
                return type;
        }
        return null;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }
}
