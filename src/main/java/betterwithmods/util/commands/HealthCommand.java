package betterwithmods.util.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

public class HealthCommand extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "health";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/health <health points>";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 4;
    }
    
    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        if (sender instanceof EntityPlayer) {
            if (args.length > 0) {
                float health = Float.parseFloat(args[0]);
                ((EntityPlayer) sender).setHealth(health);
            }
        }
    }
}
