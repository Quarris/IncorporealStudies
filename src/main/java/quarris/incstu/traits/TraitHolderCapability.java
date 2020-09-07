package quarris.incstu.traits;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class TraitHolderCapability {

    @CapabilityInject(ITraitHolder.class)
    public static Capability<ITraitHolder> INSTANCE;

}
