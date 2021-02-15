package com.airbnb.lottie.model.content;

public enum MergePaths$MergePathsMode {
    MERGE,
    ADD,
    SUBTRACT,
    INTERSECT,
    EXCLUDE_INTERSECTIONS;

    public static MergePaths$MergePathsMode O000OOo0(int i) {
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? MERGE : EXCLUDE_INTERSECTIONS : INTERSECT : SUBTRACT : ADD : MERGE;
    }
}
