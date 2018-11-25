package betterwithmods.module.general.moreheads.client;

import betterwithmods.library.utils.DirUtils;
import betterwithmods.module.general.moreheads.common.HeadType;
import betterwithmods.module.general.moreheads.common.TileHead;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class TESRHead extends TileEntitySpecialRenderer<TileHead> {

    @Override
    public void render(TileHead te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        IBlockState state = te.getWorld().getBlockState(te.getPos());
        EnumFacing facing = state.getValue(DirUtils.FACING);
        HeadType type = te.getType();
        if(type == null)
            return;
        Vec3d t = type.getTranslation(facing);
        if(t != null) {
            Vec3d r = t.add(x, y, z);
            RenderHandler.render(type, r.x, r.y, r.z, partialTicks, te.getRotation());
        }
    }


}
