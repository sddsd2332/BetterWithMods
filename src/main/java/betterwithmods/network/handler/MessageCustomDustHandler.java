package betterwithmods.network.handler;

import betterwithmods.BetterWithMods;
import betterwithmods.client.BWParticleDigging;
import betterwithmods.client.baking.IStateParticleBakedModel;
import betterwithmods.common.blocks.BWMBlock;
import betterwithmods.library.network.MessageHandler;
import betterwithmods.network.messages.MessageCustomDust;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Random;

public class MessageCustomDustHandler extends MessageHandler<MessageCustomDust> {
    @Override
    public void handleMessage(MessageCustomDust message, MessageContext context) {
        World world = getClientWorld();
        spawnBlockDustClient(world, message.pos, world.rand, message.x, message.y, message.z, message.numParticles, message.particleSpeed, EnumFacing.UP);
    }


    private void spawnBlockDustClient(World world, BlockPos pos, Random rand, float posX, float posY, float posZ, int numberOfParticles, float particleSpeed, EnumFacing facing) {
        if (pos == null)
            return;
        TextureAtlasSprite sprite;
        int tintIndex = -1;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BWMBlock) {
            tintIndex = ((BWMBlock) state.getBlock()).getParticleTintIndex();
        }

        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        if (model instanceof IStateParticleBakedModel) {
            state = state.getBlock().getExtendedState(state.getActualState(world, pos), world, pos);
            sprite = ((IStateParticleBakedModel) model).getParticleTexture(state, facing);
        } else {
            sprite = model.getParticleTexture();
        }

        ParticleManager manager = Minecraft.getMinecraft().effectRenderer;

        for (int i = 0; i < numberOfParticles; i++) {
            double xSpeed = rand.nextGaussian() * particleSpeed;
            double ySpeed = rand.nextGaussian() * particleSpeed;
            double zSpeed = rand.nextGaussian() * particleSpeed;

            try {
                Particle particle = new BWParticleDigging(world, posX, posY, posZ, xSpeed, ySpeed, zSpeed, state, pos, sprite, tintIndex);
                manager.addEffect(particle);
            } catch (Throwable var16) {
                BetterWithMods.logger.warn("Could not spawn block particle!");
                return;
            }
        }
    }

}
