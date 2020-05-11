package quarris.incstu.client.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import quarris.incstu.container.ResearchTableContainer;
import quarris.qlib.api.client.screen.TileContainerScreen;

@OnlyIn(Dist.CLIENT)
public class ResearchTableScreen extends TileContainerScreen<ResearchTableContainer> {

    public ResearchTableScreen(ResearchTableContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 300;
        this.ySize = 200;
    }
}




















