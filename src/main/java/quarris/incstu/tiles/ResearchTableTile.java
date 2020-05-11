package quarris.incstu.tiles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import quarris.incstu.ModContent;
import quarris.incstu.container.ResearchTableContainer;
import quarris.qlib.api.block.tile.BasicTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ResearchTableTile extends BasicTileEntity implements INamedContainerProvider {

    public ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            switch (slot) {
                case 0: return stack.getItem() == Items.INK_SAC;
                case 1: return stack.getItem() == Items.PAPER;
                case 2: return stack.getItem() == Items.FEATHER;
                default: return false;
            }
        }
    };

    public ResearchTableTile() {
        super(ModContent.RESEARCH_TABLE_TILE_TYPE);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.research_table");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
        return new ResearchTableContainer(id, playerInv, this.pos, this.inventory);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) inventory);
        }
        return LazyOptional.empty();
    }
}
