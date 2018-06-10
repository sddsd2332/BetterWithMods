package betterwithmods.manual.client.manual.provider;

import betterwithmods.manual.api.prefab.manual.ResourceContentProvider;
import com.google.common.collect.Lists;

import java.io.File;
import java.nio.file.Path;

public class DirectoryDefaultProvider extends ResourceContentProvider {
    public DirectoryDefaultProvider(String resourceDomain, String basePath) {
        super(resourceDomain, basePath);
    }

    @Override
    public Iterable<String> getContent(String path) {
        Iterable<String> content = super.getContent(path);
        if (content == null) {
            if (!path.endsWith(".md") || path.endsWith("/")) {
                content = Lists.newArrayList("#redirect /" + path + "/index.md");
            } else if (path.endsWith("index.md")) {
                Path p = new File(path).toPath();
                String parent = p.getParent().getFileName().toString();
                content = Lists.newArrayList("#redirect /" + path.replace("index", parent));
            }
        }
        return content;
    }
}
