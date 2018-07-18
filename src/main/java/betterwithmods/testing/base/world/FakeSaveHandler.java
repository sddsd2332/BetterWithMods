package betterwithmods.testing.base.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

public class FakeSaveHandler implements ISaveHandler {
    @Nullable
    @Override
    public WorldInfo loadWorldInfo() {
        return null;
    }

    @Override
    public void checkSessionLock() {
    }

    @Nonnull
    @Override
    public IChunkLoader getChunkLoader(@Nonnull WorldProvider provider) {
        return null;
    }

    @Override
    public void saveWorldInfoWithPlayer(@Nonnull WorldInfo worldInformation, @Nonnull NBTTagCompound tagCompound) {

    }

    @Override
    public void saveWorldInfo(@Nonnull WorldInfo worldInformation) {

    }

    @Nonnull
    @Override
    public IPlayerFileData getPlayerNBTManager() {
        return null;
    }

    @Override
    public void flush() {

    }

    @Nonnull
    @Override
    public File getWorldDirectory() {
        return null;
    }

    @Nonnull
    @Override
    public File getMapFileFromName(@Nonnull String mapName) {
        return null;
    }

    @Nonnull
    @Override
    public TemplateManager getStructureTemplateManager() {
        return null;
    }
}
