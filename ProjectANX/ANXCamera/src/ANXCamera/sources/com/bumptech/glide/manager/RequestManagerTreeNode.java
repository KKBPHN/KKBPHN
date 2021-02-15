package com.bumptech.glide.manager;

import androidx.annotation.NonNull;
import java.util.Set;

public interface RequestManagerTreeNode {
    @NonNull
    Set getDescendants();
}
