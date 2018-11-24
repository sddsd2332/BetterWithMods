package betterwithmods.module.general.moreheads.client;

import betterwithmods.module.general.moreheads.client.heads.BasicHead;
import betterwithmods.module.general.moreheads.client.model.ModelHead;
import betterwithmods.module.general.moreheads.common.HeadType;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;

import java.util.EnumMap;

public class RenderHandler {

    private static EnumMap<HeadType, IRenderHead<?>> HEAD_RENDERS = Maps.newEnumMap(HeadType.class);

    static {
        HEAD_RENDERS.put(HeadType.COW, new BasicHead(new ResourceLocation("minecraft:textures/entity/cow/cow.png"), 64, 32));
        HEAD_RENDERS.put(HeadType.BLAZE, new BasicHead(new ResourceLocation("minecraft:textures/entity/blaze.png"), 64, 32));
        HEAD_RENDERS.put(HeadType.PIGMAN, new BasicHead(new ModelHead(0, 0, 64, 64), new ResourceLocation("minecraft:textures/entity/zombie_pigman.png")));

    }

    public static IRenderHead<?> getRender(HeadType type) {
        return HEAD_RENDERS.get(type);
    }

    public static void render(HeadType type) {
        render(type, 0.5, 0, 0.5, 0, 180.0F);
    }

    public static void render(HeadType type, double x, double y, double z, float partialTicks, float rotation) {
        IRenderHead<?> renderer = getRender(type);
        if (renderer != null) {
            renderer.render(x, y, z, partialTicks, rotation);
        }
    }

}
