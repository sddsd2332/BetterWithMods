package betterwithmods.util;


import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionLib {

    public static final String[] ITEMTOOL_EFFECTIVE_BLOCKS = new String[]{"effectiveBlocks", "field_150914_c"};

    public static final String[] DEFAULT_RESOURCE_PACKS = new String[]{"field_110449_ao", "defaultResourcePacks"};

    public static final String[] IMC_MESSAGE_VALUE = new String[]{"value"};

    public static final String[] MAP_GEN_VILLAGE_SIZE = new String[]{"size", "field_75054_f"};

    public static final String[] ENTITY_VILLAGER_ISWILLINGTOMATE = new String[]{"isWillingToMate", "field_175565_bs"};

    public static final String[] INVENTORY_CRAFTING_EVENTHANDLER = new String[]{"eventHandler", "field_70465_c"};
    public static final String[] CONTAINER_WORKBENCH_PLAYER = new String[]{"player", "field_192390_i"};
    public static final String[] CONTAINER_PLAYER_PLAYER = new String[]{"player", "field_82862_h"};

    public static final String[] ENTITY_PIG_TEMPTATIONITEM = new String[]{"TEMPTATION_ITEMS", "field_184764_bw"};

    public static final String[] TOOLMATERIAL_MAXUSES = new String[]{"field_78002_g", "maxUses"};
    public static final String[] TOOLMATERIAL_EFFICIENCY = new String[]{"field_78010_h", "efficiency"};
    public static final String[] TOOLMATERIAL_ENCHANTABILITIY = new String[]{"field_78008_j", "enchantability"};

    public static final String[] ITEMTOOL_TOOLMATERIAL = new String[]{"field_77862_b", "toolMaterial"};
    public static final String[] ITEMTOOL_EFFICIENCY = new String[]{"field_77864_a", "efficiency"};
    public static final String[] ITEMTOOL_TOOLCLASS = new String[]{"toolClass"};
    public static final String[] ITEMHOE_TOOLMATERIAL = new String[]{"field_77843_a", "toolMaterial"};
    public static final String[] ITEMSWORD_TOOLMATERIAL = new String[]{"field_150933_b", "material"};

    public static final String[] POTIONHELPER_TYPE_CONVERSIONS = new String[]{"field_185213_a", "POTION_TYPE_CONVERSIONS"};
    public static final String[] POTIONHELPER_ITEM_CONVERSIONS = new String[]{"field_185214_b", "POTION_ITEM_CONVERSIONS"};

    public static final String[] ENCHANTMENT_APPLICIBLE_EQUIPMENT_TYPES = new String[]{"field_185263_a", "applicableEquipmentTypes"};

    public static final Pair<String, String> SILK_TOUCH_DROP = Pair.of("getSilkTouchDrop", "func_180643_i");

    public static final String[] ZOMBIE_BABY_SPEED_BOOST = new String[]{"BABY_SPEED_BOOST", "field_110188_br"};

    public static final String[] ENTITY_JUMP_MOTION = new String[]{"getJumpUpwardsMotion", "func_175134_bD"};

    public static final String[] MINECART_DISPENSER_BEHAVIOR = new String[]{"MINECART_DISPENSER_BEHAVIOR", "field_96602_b"};

    public static final String[] ENUMDYEECOLOR_COLORVALUE = new String[]{"field_193351_w", "colorValue"};

    public static final String[] MIXPREDICATE_INPUT = new String[]{"field_185198_a", "input"};

    public static final String[] MIXPREDICATE_OUTPUT = new String[]{"field_185200_c", "output"};

    public static final String[] MIXPREDICATE_REAGENT = new String[]{"reagent", "field_185199_b"};

    public static Class<Object> CLAZZ_MIXPREDICATE;

    static {
        try {
            CLAZZ_MIXPREDICATE = (Class<Object>) Class.forName("net.minecraft.potion.PotionHelper$MixPredicate");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void reflectSubalias(Block block, String... fields) {

        Field field = ReflectionHelper.findField(Blocks.class, fields);
        field.setAccessible(true);

        Field modifiersField = null;
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if(modifiersField != null) {
            modifiersField.setAccessible(true);
            try {
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        try {
            field.set(null, block);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
