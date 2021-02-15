package com.bumptech.glide.request.transition;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.transition.Transition.ViewAdapter;

public abstract class BitmapContainerTransitionFactory implements TransitionFactory {
    private final TransitionFactory realFactory;

    final class BitmapGlideAnimation implements Transition {
        private final Transition transition;

        BitmapGlideAnimation(Transition transition2) {
            this.transition = transition2;
        }

        public boolean transition(Object obj, ViewAdapter viewAdapter) {
            return this.transition.transition(new BitmapDrawable(viewAdapter.getView().getResources(), BitmapContainerTransitionFactory.this.getBitmap(obj)), viewAdapter);
        }
    }

    public BitmapContainerTransitionFactory(TransitionFactory transitionFactory) {
        this.realFactory = transitionFactory;
    }

    public Transition build(DataSource dataSource, boolean z) {
        return new BitmapGlideAnimation(this.realFactory.build(dataSource, z));
    }

    public abstract Bitmap getBitmap(Object obj);
}
