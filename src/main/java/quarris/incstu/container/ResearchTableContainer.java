package quarris.incstu.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import quarris.incstu.ModContent;
import quarris.qlib.api.container.TileContainer;

import javax.annotation.Nonnull;

public class ResearchTableContainer extends TileContainer {

    @OnlyIn(Dist.CLIENT)
    public ResearchTableContainer(int id, PlayerInventory playerInv, BlockPos tilePos) {
        this(id, playerInv, tilePos, new ItemStackHandler(3) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot) {
                    case 0: return stack.getItem() == Items.INK_SAC;
                    case 1: return stack.getItem() == Items.PAPER;
                    case 2: return stack.getItem() == Items.FEATHER;
                    default: return false;
                }
            }
        });
    }

    public ResearchTableContainer(int id, PlayerInventory playerInv, BlockPos tilePos, ItemStackHandler inventory) {
        super(ModContent.RESEARCH_TABLE_CONTAINER_TYPE, id, playerInv, tilePos, inventory);
        this.addSlot(new SlotItemHandler(inventory, 0, 40, 20));
        this.addSlot(new SlotItemHandler(inventory, 1, 141, 20));
        this.addSlot(new SlotItemHandler(inventory, 2, 260, 20));

        this.addPlayerSlots((300 - playerSizeX) / 2, 200 - playerSizeY - 8);
    }
}
