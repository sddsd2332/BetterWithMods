package betterwithmods.common.items;

import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.entity.EntityDynamite;
import betterwithmods.util.InvUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


import javax.annotation.Nullable;
import java.util.List;

public class ItemDynamite extends Item {

    private final static Ingredient FLINT_AND_STEEL = Ingredient.fromStacks(new ItemStack(Items.FLINT_AND_STEEL, 1, OreDictionary.WILDCARD_VALUE));

    public ItemDynamite() {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);

        if (!world.isRemote) {

            ItemStack activator = InvUtils.findItemInInventory(FLINT_AND_STEEL, InvUtils.getPlayerInventory(player, null));
            if (!activator.isEmpty()) {
                activator.damageItem(1, player);
            }
            boolean lit = !activator.isEmpty();


            held.shrink(1);
            EntityDynamite dynamite = new EntityDynamite(world, player, lit);
            world.spawnEntity(dynamite);

            if (lit)
                world.playSound(null, new BlockPos(dynamite.posX, dynamite.posY, dynamite.posZ), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.AMBIENT, 1.0F, 1.0F);
            else
                world.playSound(null, new BlockPos(dynamite.posX, dynamite.posY, dynamite.posZ), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.AMBIENT, 0.5F, 0.4F / (Item.itemRand.nextFloat() * 0.4F + 0.8F));
            return new ActionResult<>(EnumActionResult.SUCCESS, held);
        }
        return new ActionResult<>(EnumActionResult.PASS, held);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("lore.bwm:dynamite"));
    }

}
