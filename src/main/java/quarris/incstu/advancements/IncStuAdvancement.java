package quarris.incstu.advancements;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.KilledTrigger;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import quarris.qlib.api.data.advancement.DisplayInfoBuilder;
import quarris.qlib.api.util.Utils;

import java.util.function.Consumer;

public class IncStuAdvancement implements Consumer<Consumer<Advancement>> {

    @Override
    public void accept(Consumer<Advancement> consumer) {
        ItemStack ghastHead = Utils.createPlayerHead("MHF_Ghast");
        Advancement root = Advancement.Builder.builder()
                .withDisplay(new DisplayInfoBuilder()
                        .icon(ghastHead)
                        .title("advancements.incstu.root.title")
                        .description("advancements.incstu.root.description")
                        .background(new ResourceLocation("textures/gui/advancements/backgrounds/end.png"))
                        .frame(FrameType.TASK).build())
                .withCriterion("killed_ghast", KilledTrigger.Instance.playerKilledEntity(EntityPredicate.Builder.create().type(EntityType.GHAST)))
                .register(consumer, "incstu/root");
    }
}
