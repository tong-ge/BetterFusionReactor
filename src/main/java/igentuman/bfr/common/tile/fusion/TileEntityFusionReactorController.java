package igentuman.bfr.common.tile.fusion;

import mekanism.common.capabilities.Capabilities;
import mekanism.common.integration.energy.EnergyCompatUtils;
import mekanism.common.tile.base.SubstanceType;
import igentuman.bfr.common.content.fusion.FusionReactorMultiblockData;
import igentuman.bfr.common.registries.BfrBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityFusionReactorController extends TileEntityFusionReactorBlock {

    public TileEntityFusionReactorController(BlockPos pos, BlockState state) {
        super(BfrBlocks.FUSION_REACTOR_CONTROLLER, pos, state);
        //Never allow the gas handler, fluid handler, or energy cap to be enabled here even though internally we can handle both of them
        addDisabledCapabilities(Capabilities.GAS_HANDLER_CAPABILITY, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Capabilities.HEAT_HANDLER_CAPABILITY);
        addDisabledCapabilities(EnergyCompatUtils.getEnabledEnergyCapabilities());
        addSemiDisabledCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, () -> !getMultiblock().isFormed());
        delaySupplier = () -> 0;
    }

    @Override
    protected boolean onUpdateServer(FusionReactorMultiblockData multiblock) {
        boolean needsPacket = super.onUpdateServer(multiblock);
        setActive(multiblock.isFormed());
        return needsPacket;
    }

    @Override
    protected boolean canPlaySound() {
        FusionReactorMultiblockData multiblock = getMultiblock();
        return multiblock.isFormed() && multiblock.isBurning();
    }

    @Override
    public boolean canBeMaster() {
        return true;
    }

    @Override
    public boolean handles(SubstanceType type) {
        if (type == SubstanceType.GAS || type == SubstanceType.FLUID || type == SubstanceType.HEAT) {
            return false;
        }
        return super.handles(type);
    }
}