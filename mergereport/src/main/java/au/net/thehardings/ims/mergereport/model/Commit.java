package au.net.thehardings.ims.mergereport.model;

import java.util.List;

/**
 * The class <code>Commit</code>
 */
public class Commit {
    private Repository repository;
    private String branchName;
    private String commitId;
    private String username;
    private String date;
    private List<String> comment;

    private Commit(Repository repository, String branchName, String commitId, String username, String date, List<String> comment) {
        this.repository = repository;
        this.branchName = branchName;
        this.commitId = commitId;
        this.username = username;
        this.date = date;
        this.comment = comment;
    }

    public Repository getRepository() {
        return repository;
    }

    public String getRepositoryName() {
        return repository.getName();
    }

    public String getBranchName() {
        return branchName;
    }

    public String getCommitId() {
        return commitId;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public List<String> getComment() {
        return comment;
    }

    public static class Builder {
        private Repository repository;
        private String branchName;
        private String commitId;
        private String username;
        private String date;
        private List<String> comment;

        public Builder(Repository repository, String branchName, int commitId) {
            this.repository = repository;
            this.branchName = branchName;
            this.commitId = Integer.toString(commitId);
        }

        public Builder username(String value) {
            username = value;
            return this;
        }

        public Builder date(String value) {
            date = value;
            return this;
        }

        public Builder comment(List<String> value) {
            comment = value;
            return this;
        }

        public Commit build() {
            return new Commit(repository, branchName, commitId, username, date, comment);
        }
    }
}
