package nl.bitbrains.nebu.common.topology;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.bitbrains.nebu.common.util.ErrorChecker;

/**
 * Builder class for the {@link PhysicalDataCenter}.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class PhysicalDataCenterBuilder extends PhysicalResourceBuilder<PhysicalDataCenter> {
    private PhysicalRoot parent;
    private Map<String, PhysicalRack> racks;

    /**
     * Simple constructor.
     */
    public PhysicalDataCenterBuilder() {
        this.reset();
    }

    /**
     * Resets all.
     */
    @Override
    public final void reset() {
        super.reset();
        this.parent = null;
        this.racks = new HashMap<String, PhysicalRack>();
    }

    /**
     * @param parent
     *            to build with
     * @return this for fluency.
     */
    public PhysicalDataCenterBuilder withParent(final PhysicalRoot parent) {
        this.parent = parent;
        return this;
    }

    /**
     * @param rack
     *            to include.
     * @return this for fluency.
     */
    public PhysicalDataCenterBuilder withRack(final PhysicalRack rack) {
        ErrorChecker.throwIfNullArgument(rack, "rack");
        this.racks.put(rack.getUniqueIdentifier(), rack);
        return this;
    }

    /**
     * @param racks
     *            to include.
     * @return this for fluency.
     */
    public PhysicalDataCenterBuilder withRacks(final List<PhysicalRack> racks) {
        ErrorChecker.throwIfNullArgument(racks, "racks");
        for (final PhysicalRack rack : racks) {
            this.withRack(rack);
        }
        return this;
    }

    /**
     * @return the build {@link PhysicalRack} object.
     */
    public PhysicalDataCenter build() {
        ErrorChecker.throwIfNotSet(this.getUUID(), PhysicalResource.UUID_NAME);
        final PhysicalDataCenter res = new PhysicalDataCenter(this.getUUID(), this.parent,
                this.racks);
        this.reset();
        return res;
    }
}
