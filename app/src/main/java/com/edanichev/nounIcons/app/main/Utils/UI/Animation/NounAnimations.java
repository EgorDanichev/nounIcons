package com.edanichev.nounIcons.app.main.Utils.UI.Animation;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class NounAnimations {

    private final static float FULLY_VISIBLE = 1.0f;
    private final static float FULLY_INVISIBLE = 0.0f;

    public final static int SHORT_FADE = 500;
    public final static int LONG_FADE = 1000;

    public static Animation getBecomeInvisibleAnimation() {
        Animation animation = new AlphaAnimation(FULLY_VISIBLE, FULLY_INVISIBLE);
        animation.setDuration(500);
        animation.setFillAfter(true);
        return animation;
    }

    public static Animation getBecomeVisibleAnimation(int duration) {
        Animation animation = new AlphaAnimation(FULLY_INVISIBLE, FULLY_VISIBLE);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        return animation;
    }

    public static Animation getIconBindAnimation() {
        Animation animation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        return animation;
    }

    public static Animation getFavoriteButtonAnimation() {
        Animation animation = new ScaleAnimation(1.5f, 1.0f, 1.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        return animation;
    }

}
