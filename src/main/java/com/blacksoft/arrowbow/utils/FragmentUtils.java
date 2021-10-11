package com.blacksoft.arrowbow.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * The goal of this class is to hold utils that help you to manage Fragments like adding
 * them to the screen replacing them, animating them ext..
 */
public class FragmentUtils {

    /**
     * private constructor to prevent users from creating an instance from this class
     */
    private FragmentUtils() {
    }

    /**
     * puts the fragment inside the specified layout
     *
     * @param fragment:        your fragment
     * @param fragmentManager: fragment manager instance you get from your activity
     *                         or your fragment
     * @param parentLayoutId:  layout you want to put your fragment inside
     * @param addToBackStack:  if this fragment should be added to back stack or not
     */
    public static final void putFragmentInParent(@NonNull Fragment fragment,
                                                 @NonNull FragmentManager fragmentManager,
                                                 int parentLayoutId,
                                                 boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(parentLayoutId, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        if (addToBackStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    /**
     * puts the fragment inside the specified layout
     *
     * @param fragment:           your fragment
     * @param fragmentManager:    fragment manager instance you get from your activity
     *                            or your fragment
     * @param parentLayoutId:     layout you want to put your fragment inside
     * @param fragmentTransition: the animation that should be applied when the fragment
     *                            is added to the screen
     * @param addToBackStack:     if this fragment should be added to back stack or not
     */
    public static final void putFragmentInParent(@NonNull Fragment fragment,
                                                 @NonNull FragmentManager fragmentManager,
                                                 int parentLayoutId,
                                                 int fragmentTransition,
                                                 boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(parentLayoutId, fragment);
        fragmentTransaction.setTransition(fragmentTransition);
        if (addToBackStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    /**
     * Removes the fragment
     *
     * @param fragment:        your fragment
     * @param fragmentManager: fragment manager instance you get from your activity
     *                         or your fragment
     */
    public static final void removeFragment(@NonNull Fragment fragment,
                                            @NonNull FragmentManager fragmentManager) {
        fragmentManager
                .beginTransaction()
                .remove(fragment)
                .commit();
    }

    /**
     * Returns to the previous fragment if found in the back stack.
     *
     * @param fragmentManager: fragment manager instance you get from your activity
     *                         or your fragment
     */
    public static final void backNavigation(@NonNull FragmentManager fragmentManager) {
        fragmentManager.popBackStack();
    }

}