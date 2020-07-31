package com.onuroapplications.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.HashMap;

/**
 * This singleton class provides methods for animations. Animations can be stored and operations
 * can be executed on. A class using this manager can also start an animation.
 * All animations in the project are only loaded once and are stored in this class.
 */
public class AnimationManager {
    HashMap<String, Animation> animations = new HashMap<>();

    private static final AnimationManager manager = new AnimationManager();

    //only instance of AnimationManager
    private AnimationManager() {
    }

    public static AnimationManager getInstance() {
        return manager;
    }

    public void addAnimation(int id, String name, Context context){
        if(!animations.containsKey(name)) {
            Animation animation = AnimationUtils.loadAnimation(context, id);
            animations.put(name, animation);
        }
    }

    //TODO add exception
    public void executeStoredAnimation(String name, View view) {
        Animation anim = animations.get(name);
        view.startAnimation(anim);
    }

}
