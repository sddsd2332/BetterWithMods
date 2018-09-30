package betterwithmods.client;
/* Resource Proxy borrowed from Quark*/

import betterwithmods.BetterWithMods;
import betterwithmods.lib.ModLib;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public class ResourceProxy extends AbstractResourcePack {

    private static final String MINECRAFT = "minecraft";
    private static final Set<String> RESOURCE_DOMAINS = ImmutableSet.of(MINECRAFT);

    private static final String BARE_FORMAT = "assets/%s/%s/%s/%s.%s";
    private static final String OVERRIDE_FORMAT = "/assets/%s/%s/%s/overrides/%s.%s";

    private static final Map<String, String> overrides = Maps.newHashMap();

    public ResourceProxy() {
        super(Loader.instance().activeModContainer().getSource());
        overrides.put("pack.mcmeta", "/proxypack.mcmeta");
    }

    public void addResource(String space, String domain, String dir, String file, String ext) {
        String bare = String.format(BARE_FORMAT, domain, space, dir, file, ext);
        String override = String.format(OVERRIDE_FORMAT, domain, space, dir, file, ext);
        overrides.put(bare, override);
        BetterWithMods.logger.info("Override texture: {} to {}", bare, override);
    }

    public void addResource(String space, String dir, String file, String ext) {
        String bare = String.format(BARE_FORMAT, MINECRAFT, space, dir, file, ext);
        String override = String.format(OVERRIDE_FORMAT, ModLib.MODID, space, dir, file, ext);
        overrides.put(bare, override);
        BetterWithMods.logger.info("Override texture: {} to {}", bare, override);
    }

    @Nonnull
    @Override
    protected InputStream getInputStreamByName(@Nonnull String name) {
        return BetterWithMods.class.getResourceAsStream(overrides.get(name));
    }

    @Override
    protected boolean hasResourceName(@Nonnull String name) {
        return overrides.containsKey(name);
    }


    @Override
    public Set<String> getResourceDomains() {
        return RESOURCE_DOMAINS;
    }

    @Nonnull
    @Override
    public String getPackName() {
        return "bwm-texture-proxy";
    }

}