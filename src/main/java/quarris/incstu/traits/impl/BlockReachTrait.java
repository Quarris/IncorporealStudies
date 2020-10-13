package quarris.incstu.traits.impl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import quarris.incstu.traits.AbstractTrait;
import quarris.incstu.traits.ITrait;

public class BlockReachTrait extends AbstractTrait<PlayerEntity> {

    private static final AttributeModifier INCREASED_REACH = new AttributeModifier("increased_reach", 2, AttributeModifier.Operation.ADDITION);

    public BlockReachTrait(PlayerEntity holder) {
        super(holder);
    }

    @Override
    public void onApplied(PlayerEntity entity) {
        entity.getAttribute(PlayerEntity.REACH_DISTANCE).applyModifier(INCREASED_REACH);
    }

    @Override
    public void onRemoved(PlayerEntity entity) {
        entity.getAttribute(PlayerEntity.REACH_DISTANCE).removeModifier(INCREASED_REACH);
    }

    @Override
    public boolean canApplyTo(Entity entity) {
        return entity instanceof PlayerEntity;
    }
}
