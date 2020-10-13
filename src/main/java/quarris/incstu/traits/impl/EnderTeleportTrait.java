package quarris.incstu.traits.impl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import quarris.incstu.traits.AbstractTrait;

public class EnderTeleportTrait extends AbstractTrait<PlayerEntity> {

    public EnderTeleportTrait(PlayerEntity holder) {
        super(holder);
    }

    @Override
    public void onApplied(PlayerEntity entity) {
        System.out.println("Added ender tp to " + entity.getDisplayName().getFormattedText());
    }

    @Override
    public void onRemoved(PlayerEntity entity) {
        System.out.println("Removed ender tp from " + entity.getDisplayName().getFormattedText());
    }

    public static void teleport(PlayerEntity entity, EnderTeleportTrait trait, TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && entity.world.getWorldInfo().getGameTime() % 20 == 0) {
            System.out.println("Ticking for " + entity.getDisplayName().getFormattedText());
        }
    }

    @Override
    public boolean canApplyTo(Entity entity) {
        return entity instanceof PlayerEntity;
    }
}
