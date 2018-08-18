package betterwithmods.module.general;

import betterwithmods.module.Feature;
import betterwithmods.module.Module;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public class General extends Module {

    private static boolean debug;

    public static boolean isDebug() {
        return debug;
    }

    @Override
    public void addFeatures() {
        addFeature(new Client());
    }

    @Override
    protected boolean canEnable() {
        return true;
    }

    @Override
    public List<Feature> setup(File file, Logger logger) {
        List<Feature> features = super.setup(file, logger);
        debug = config().load(getName(), "Debug Mode", false).setComment("Log extra debug information, warning can spam logs").get();
        return features;
    }
}
