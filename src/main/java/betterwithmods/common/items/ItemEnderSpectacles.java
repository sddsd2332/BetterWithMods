package betterwithmods.common.items;

import betterwithmods.common.BWMRegistry;
import betterwithmods.lib.ModLib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemEnderSpectacles extends ItemArmor {

    public ItemEnderSpectacles() {
        super(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD);

    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (!player.isPotionActive(BWMRegistry.POTION_SLOWFALL))
            player.addPotionEffect(new PotionEffect(BWMRegistry.POTION_TRUESIGHT, 0));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return ModLib.MODID + ":textures/models/armor/ender_spectacles.png";
    }
}
