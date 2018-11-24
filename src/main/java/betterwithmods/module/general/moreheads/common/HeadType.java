package betterwithmods.module.general.moreheads.common;

public enum HeadType {
    BLAZE("entity.Blaze.name"),
    PIGMAN(""),
    COW("entity.Cow.name"),
    PIG(""),
    SHEEP(""),
    CHICKEN("");

    public static HeadType[] VALUES = values();

    private String entityUnlocalizedName;

    HeadType(String entityUnlocalizedName) {
        this.entityUnlocalizedName = entityUnlocalizedName;
    }

    public String getEntityUnlocalizedName() {
        return entityUnlocalizedName;
    }
}
