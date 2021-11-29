package com.hezhihu.plugin.dependencies;

import java.io.File;

public class DepConfig {
    public File dependencies;

    public File getDependenciesXml() {
        return dependencies;
    }

    public void setDependenciesXml(File dependencies) {
        this.dependencies = dependencies;
    }
}
