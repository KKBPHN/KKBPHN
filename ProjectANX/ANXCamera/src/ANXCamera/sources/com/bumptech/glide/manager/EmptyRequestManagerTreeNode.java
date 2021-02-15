package com.bumptech.glide.manager;

import androidx.annotation.NonNull;
import java.util.Collections;
import java.util.Set;

final class EmptyRequestManagerTreeNode implements RequestManagerTreeNode {
    EmptyRequestManagerTreeNode() {
    }

    @NonNull
    public Set getDescendants() {
        return Collections.emptySet();
    }
}
