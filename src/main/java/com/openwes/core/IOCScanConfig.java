package com.openwes.core;

import java.util.List;

/**
 *
 * @author xuanloc0511@gmail.com
 */
class IOCScanConfig {

    private List<String> packages;
    private List<String> includes;
    private List<String> excludes;

    public List<String> getPackages() {
        return packages;
    }

    public IOCScanConfig setPackages(List<String> packages) {
        this.packages = packages;
        return this;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public IOCScanConfig setIncludes(List<String> includes) {
        this.includes = includes;
        return this;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public IOCScanConfig setExcludes(List<String> excludes) {
        this.excludes = excludes;
        return this;
    }

}
