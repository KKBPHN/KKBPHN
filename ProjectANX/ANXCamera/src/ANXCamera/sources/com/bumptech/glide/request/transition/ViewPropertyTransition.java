package com.bumptech.glide.request.transition;

import android.view.View;
import com.bumptech.glide.request.transition.Transition.ViewAdapter;

public class ViewPropertyTransition implements Transition {
    private final Animator animator;

    public interface Animator {
        void animate(View view);
    }

    public ViewPropertyTransition(Animator animator2) {
        this.animator = animator2;
    }

    public boolean transition(Object obj, ViewAdapter viewAdapter) {
        if (viewAdapter.getView() != null) {
            this.animator.animate(viewAdapter.getView());
        }
        return false;
    }
}
