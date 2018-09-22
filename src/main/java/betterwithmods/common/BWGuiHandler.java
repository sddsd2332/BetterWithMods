package betterwithmods.common;

import betterwithmods.client.gui.GuiBlockDispenser;
import betterwithmods.client.gui.GuiInfernalEnchanter;
import betterwithmods.client.gui.GuiPulley;
import betterwithmods.client.gui.GuiSteelAnvil;
import betterwithmods.client.gui.bulk.GuiCauldron;
import betterwithmods.client.gui.bulk.GuiCrucible;
import betterwithmods.client.gui.bulk.GuiFilteredHopper;
import betterwithmods.client.gui.bulk.GuiMill;
import betterwithmods.common.container.anvil.ContainerSteelAnvil;
import betterwithmods.common.container.bulk.ContainerCookingPot;
import betterwithmods.common.container.bulk.ContainerFilteredHopper;
import betterwithmods.common.container.bulk.ContainerMill;
import betterwithmods.common.container.other.ContainerBlockDispenser;
import betterwithmods.common.container.other.ContainerInfernalEnchanter;
import betterwithmods.common.container.other.ContainerPulley;
import betterwithmods.common.tile.*;
import betterwithmods.manual.client.gui.GuiManual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class BWGuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        switch (Gui.VALUES[ID]) {
            case TILE:
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof TilePulley)
                    return new ContainerPulley(player, (TilePulley) tile);
                if (tile instanceof TileBlockDispenser)
                    return new ContainerBlockDispenser(player, (TileBlockDispenser) tile);
                if (tile instanceof TileCrucible)
                    return new ContainerCookingPot(player, (TileCrucible) tile);
                if (tile instanceof TileCauldron)
                    return new ContainerCookingPot(player, (TileCauldron) tile);
                if (tile instanceof TileMill)
                    return new ContainerMill(player, (TileMill) tile);
                if (tile instanceof TileFilteredHopper)
                    return new ContainerFilteredHopper(player, (TileFilteredHopper) tile);
                if (tile instanceof TileSteelAnvil)
                    return new ContainerSteelAnvil(player.inventory, (TileSteelAnvil) tile);
                if (tile instanceof TileInfernalEnchanter)
                    return new ContainerInfernalEnchanter(player, (TileInfernalEnchanter) tile);
                return null;
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        switch (Gui.VALUES[ID]) {
            case TILE:
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof TilePulley)
                    return new GuiPulley(player, (TilePulley) tile);
                if (tile instanceof TileBlockDispenser)
                    return new GuiBlockDispenser(player, (TileBlockDispenser) tile);
                if (tile instanceof TileCrucible)
                    return new GuiCrucible(player, (TileCrucible) tile);
                if (tile instanceof TileCauldron)
                    return new GuiCauldron(player, (TileCauldron) tile);
                if (tile instanceof TileMill)
                    return new GuiMill(player, (TileMill) tile);
                if (tile instanceof TileFilteredHopper)
                    return new GuiFilteredHopper(player, (TileFilteredHopper) tile);
                if (tile instanceof TileSteelAnvil)
                    return new GuiSteelAnvil((TileSteelAnvil) tile, new ContainerSteelAnvil(player.inventory, (TileSteelAnvil) tile));
                if (tile instanceof TileInfernalEnchanter)
                    return new GuiInfernalEnchanter(player, (TileInfernalEnchanter) tile);
                return null;
            case MANUAL:
                return new GuiManual();
            default:
                return null;
        }
    }

    public enum Gui {
        TILE,
        MANUAL;

        public static final Gui[] VALUES = values();
    }

}
