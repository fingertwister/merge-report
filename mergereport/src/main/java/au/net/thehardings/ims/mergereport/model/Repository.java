package au.net.thehardings.ims.mergereport.model;

import java.io.File;

/**
 * The class <code>Repository</code>
 */
public class Repository {
    String branchName;
    File base;
    String name;
    String url;

    public Repository() {
    }

    public Repository(String branchName, String base, String name, String url) {
        setBranchName(branchName);
        setBasePath(base);
        setName(name);
        setUrl(url);
    }

    public String getBranchName() {
        return branchName;
    }

    public String getBranchUrl() {
        //we should support merging from trunk to a branch as well. This code makes some
        //assumptions about the structure of the subversion repository. Fairly safe
        //assumptions, but assumptions none-the-less.
        if ("trunk".equals(branchName)) {
            return url + "/" + branchName;
        } else {
            return url + "/branches/" + branchName;
        }
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public File getBase() {
        return base;
    }

    public void setBasePath(String trunkPath) {
        this.base = new File(trunkPath);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
