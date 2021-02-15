package com.miui.internal.component.module;

import java.io.File;
import miui.module.Repository;

public class CombinedRepository extends Repository {
    private Repository[] repositories;

    public CombinedRepository(Repository... repositoryArr) {
        this.repositories = repositoryArr;
    }

    public boolean contains(String str) {
        for (Repository contains : this.repositories) {
            if (contains.contains(str)) {
                return true;
            }
        }
        return false;
    }

    public String fetch(File file, String str) {
        Repository[] repositoryArr;
        for (Repository repository : this.repositories) {
            if (repository.contains(str)) {
                return repository.fetch(file, str);
            }
        }
        return null;
    }
}
