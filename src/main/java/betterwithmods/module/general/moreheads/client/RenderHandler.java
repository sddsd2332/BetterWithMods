package betterwithmods.module.general.moreheads.client;

import betterwithmods.module.general.moreheads.client.heads.*;
import betterwithmods.module.general.moreheads.client.model.ModelHead;
import betterwithmods.module.general.moreheads.client.model.ModelPigHead;
import betterwithmods.module.general.moreheads.common.HeadType;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import java.util.EnumMap;

public class RenderHandler {

    private static EnumMap<HeadType, IRenderHead<?>> HEAD_RENDERS = Maps.newEnumMap(HeadType.class);

    static {
        HEAD_RENDERS.put(HeadType.COW, new CowHead());
        HEAD_RENDERS.put(HeadType.MUSHROOMCOW, new CowHead(new ResourceLocation("minecraft:textures/entity/cow/mooshroom.png")));
        HEAD_RENDERS.put(HeadType.CHICKEN, new ChickenHead());
        HEAD_RENDERS.put(HeadType.BLAZE, new BasicHead(new ResourceLocation("minecraft:textures/entity/blaze.png"), 64, 32));
        HEAD_RENDERS.put(HeadType.ENDERMAN, new EndermanHead());
        HEAD_RENDERS.put(HeadType.PIG, new BasicHead(new ModelPigHead(), new ResourceLocation("minecraft:textures/entity/pig/pig.png")));
        HEAD_RENDERS.put(HeadType.PIGMAN, new BasicHead(new ModelHead(0, 0, 64, 64), new ResourceLocation("minecraft:textures/entity/zombie_pigman.png")));
        HEAD_RENDERS.put(HeadType.SLIME, new SlimeHead());
        HEAD_RENDERS.put(HeadType.SHEEP, new SheepHead());
        HEAD_RENDERS.put(HeadType.VILLAGER, new VillagerHead());
        HEAD_RENDERS.put(HeadType.WITCH, new VillagerHead(new ResourceLocation("minecraft:textures/entity/witch.png"), 64,128));

    }

    public static IRenderHead<?> getRender(HeadType type) {
        return HEAD_RENDERS.get(type);
    }

    public static void render(HeadType type) {
        Vec3d t = type.getItemTranslation();
        if(t != null) {
            render(type, t.x, t.y, t.z, 0, -180f);
        }
    }

    public static void render(HeadType type, double x, double y, double z, float partialTicks, float rotation) {
        IRenderHead<?> renderer = getRender(type);
        if (renderer != null) {
            renderer.render(x, y, z, partialTicks, rotation);
        }
    }

}
