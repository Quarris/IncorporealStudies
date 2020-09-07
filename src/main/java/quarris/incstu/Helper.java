package quarris.incstu;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class Helper {

    private static final Capability.IStorage<?> EMPTY = new Capability.IStorage<Object>() {
        @Override
        public INBT writeNBT(Capability<Object> capability, Object instance, Direction side) {
            return null;
        }

        @Override
        public void readNBT(Capability<Object> capability, Object instance, Direction side, INBT nbt) {

        }
    };

    public static void registerCapability(Class clazz) {
        CapabilityManager.INSTANCE.register(clazz, EMPTY, () -> null);
    }

}
