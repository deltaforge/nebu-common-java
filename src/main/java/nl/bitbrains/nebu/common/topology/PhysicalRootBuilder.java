package nl.bitbrains.nebu.common.topology;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.bitbrains.nebu.common.util.ErrorChecker;

/**
 * Builder class for the {@link PhysicalRoot}.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class PhysicalRootBuilder extends PhysicalResourceBuilder<PhysicalRoot> {
    private Map<String, PhysicalDataCenter> dcs;

    /**
     * Simple constructor.
     */
    public PhysicalRootBuilder() {
        this.reset();
    }

    /**
     * Resets all.
     */
    @Override
    public final void reset() {
        super.reset();
        this.dcs = new HashMap<String, PhysicalDataCenter>();
    }

    /**
     * @param dc
     *            to include.
     * @return this for fluency.
     */
    public PhysicalRootBuilder withDataCenter(final PhysicalDataCenter dc) {
        ErrorChecker.throwIfNullArgument(dc, "dc");
        this.dcs.put(dc.getUniqueIdentifier(), dc);
        return this;
    }

    /**
     * @param dcs
     *            to include.
     * @return this for fluency.
     */
    public PhysicalRootBuilder withDataCenters(final List<PhysicalDataCenter> dcs) {
        ErrorChecker.throwIfNullArgument(dcs, "dcs");
        for (final PhysicalDataCenter dc : dcs) {
            this.withDataCenter(dc);
        }
        return this;
    }

    /**
     * @return the build {@link PhysicalRack} object.
     */
    public PhysicalRoot build() {
        ErrorChecker.throwIfNotSet(this.getUUID(), PhysicalResource.UUID_NAME);
        final PhysicalRoot res = new PhysicalRoot(this.getUUID(), this.dcs);
        this.reset();
        return res;
    }

}
