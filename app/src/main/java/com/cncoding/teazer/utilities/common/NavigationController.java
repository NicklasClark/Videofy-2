package com.cncoding.teazer.utilities.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.view.View;

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static android.R.anim.fade_in;
import static android.R.anim.fade_out;

/**
 * The class is used to manage navigation through multiple stacks of fragments, as well as coordinate
 * fragments that may appear on screen
 * <p>
 * https://github.com/ncapdevi/FragNav
 * Nic Capdevila
 * Nic.Capdevila@gmail.com
 * <p>
 * Originally Created March 2016
 */
@SuppressWarnings({"RestrictedApi", "SameParameterValue", "WeakerAccess"})
public class NavigationController {
    //Declare the constants  There is a maximum of 5 tabs, this is per Material Design's Bottom Navigation's design spec.
    static final int NO_TAB = -1;
    public static final int TAB1 = 0;
    public static final int TAB2 = 1;
    public static final int TAB3 = 2;
    public static final int TAB4 = 3;
    public static final int TAB5 = 4;

    private static final int MAX_NUM_TABS = 5;

    // Extras used to store savedInstanceState
    private static final String EXTRA_TAG_COUNT = NavigationController.class.getName() + ":EXTRA_TAG_COUNT";
    private static final String EXTRA_SELECTED_TAB_INDEX = NavigationController.class.getName() + ":EXTRA_SELECTED_TAB_INDEX";
    private static final String EXTRA_CURRENT_FRAGMENT = NavigationController.class.getName() + ":EXTRA_CURRENT_FRAGMENT";
    private static final String EXTRA_FRAGMENT_STACK = NavigationController.class.getName() + ":EXTRA_FRAGMENT_STACK";

    @IdRes private final int containerId;
    @NonNull private final List<Stack<Fragment>> fragmentStacks;
    @NonNull private final FragmentManager fragmentManager;
    private final NavigationTransactionOptions defaultTransactionOptions;
    private final Activity activity;
    @TabIndex private int selectedTabIndex;
    private int tagCount;
    @Nullable private Fragment currentFragment;
//    @Nullable private DialogFragment currentDialogFragment;
    @Nullable private RootFragmentListener rootFragmentListener;
    @Nullable private TransactionListener transactionListener;
    private boolean executingTransaction;

    //region Construction and setup

    private NavigationController(final Builder builder, @Nullable final Bundle savedInstanceState) {
        fragmentManager = builder.fragmentManager;
        containerId = builder.containerId;
        fragmentStacks = new ArrayList<>(builder.numberOfTabs);
        rootFragmentListener = builder.rootFragmentListener;
        transactionListener = builder.transactionListener;
        activity = builder.activity;
        defaultTransactionOptions = builder.defaultTransactionOptions;
        selectedTabIndex = builder.mSelectedTabIndex;

        new Thread(new Runnable() {
            public void run() {
                //Attempt to restore from bundle, if not, initialize
                if (!restoreFromBundle(savedInstanceState, builder.rootFragments)) {

                    for (int i = 0; i < builder.numberOfTabs; i++) {
                        Stack<Fragment> stack = new Stack<>();
                        if (builder.rootFragments != null) {
                            stack.add(builder.rootFragments.get(i));
                        }
                        fragmentStacks.add(stack);
                    }

                    initialize(builder.mSelectedTabIndex);
                }
            }
        }).start();
    }

    @NonNull
    public static Builder newBuilder(@Nullable Bundle savedInstanceState, FragmentManager fragmentManager, int containerId) {
        return new Builder(savedInstanceState, fragmentManager, containerId);
    }

    //endregion

    //region Transactions

    /**
     * Function used to switch to the specified fragment stack
     *
     * @param index              The given index to switch to
     * @param transactionOptions Transaction options to be displayed
     * @throws IndexOutOfBoundsException Thrown if trying to switch to an index outside given range
     */
    public void switchTab(@TabIndex int index, @Nullable NavigationTransactionOptions transactionOptions) {
        try {
            //Check to make sure the tab is within range
            if (index >= fragmentStacks.size()) {
                throw new IndexOutOfBoundsException("Can't switch to a tab that hasn't been initialized, " +
                        "Index : " + index + ", current stack size : " + fragmentStacks.size() +
                        ". Make sure to create all of the tabs you need in the Constructor or provide a way for them to be created via RootFragmentListener.");
            }
            if (selectedTabIndex != index) {
                selectedTabIndex = index;

                FragmentTransaction ft = createTransactionWithOptions(transactionOptions);

                detachCurrentFragment(ft);

                Fragment fragment = null;
                if (index == NO_TAB) {
                    ft.commit();
                } else {
                    //Attempt to reattach previous fragment
                    fragment = reattachPreviousFragment(ft);
                    if (fragment != null) {
                        ft.commit();
                    } else {
                        fragment = getRootFragment(selectedTabIndex);
                        ft.setCustomAnimations(fade_in, fade_out, fade_in, fade_out);
                        ft.add(containerId, fragment, generateTag(fragment));
                        ft.commit();
                    }
                }
                executePendingTransactions();
                currentFragment = fragment;
                if (transactionListener != null) {
                    transactionListener.onTabTransaction(currentFragment, selectedTabIndex);
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function used to switch to the specified fragment stack
     *
     * @param index The given index to switch to
     * @throws IndexOutOfBoundsException Thrown if trying to switch to an index outside given range
     */
    public void switchTab(@TabIndex int index) throws IndexOutOfBoundsException {
        switchTab(index, null);
    }

    /**
     * Push a fragment onto the current stack
     *
     * @param fragment           The fragment that is to be pushed
     * @param transactionOptions Transaction options to be displayed
     */
    public void pushFragment(@Nullable final Fragment fragment, @Nullable NavigationTransactionOptions transactionOptions) {
        if (fragment != null && selectedTabIndex != NO_TAB) {
            try {
                FragmentTransaction ft = createTransactionWithOptions(transactionOptions);

                detachCurrentFragment(ft);
//            ft.setCustomAnimations(float_up, sink_down, float_up, sink_down);
                ft.add(containerId, fragment, generateTag(fragment));
                ft.commitAllowingStateLoss();

                executePendingTransactions();

                fragmentStacks.get(selectedTabIndex).push(fragment);

                currentFragment = fragment;
                if (transactionListener != null) {
                    transactionListener.onFragmentTransaction(currentFragment, TransactionType.PUSH);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Push a fragment onto the current stack without detaching the previous one
     *
     * @param fragment The fragment that is to be pushed
     */
    public void pushFragmentOnto(@Nullable final Fragment fragment) {
        if (fragment != null && selectedTabIndex != NO_TAB) {
            FragmentTransaction ft = createTransactionWithOptions(null);

//            detachCurrentFragment(ft);

            // ft.setCustomAnimations(float_up, sink_down, float_up, sink_down);
            ft.add(containerId, fragment, generateTag(fragment));
            ft.commit();

            executePendingTransactions();

            fragmentStacks.get(selectedTabIndex).push(fragment);

            currentFragment = fragment;

            if (transactionListener != null) {
                transactionListener.onFragmentTransaction(currentFragment, TransactionType.PUSH);
            }

        }
    }

    /**
     * Push a fragment onto the current stack
     *
     * @param fragment The fragment that is to be pushed
     */
    public void pushFragment(@Nullable Fragment fragment) {
        pushFragment(fragment, null);
    }

    /**
     * Pop the current fragment from the current tab
     *
     * @param transactionOptions Transaction options to be displayed
     */
    public void popFragment(@Nullable NavigationTransactionOptions transactionOptions) throws UnsupportedOperationException {
        popFragments(1, transactionOptions);
    }

    /**
     * Pop the current fragment from the current tab
     */
    public void popFragment() throws UnsupportedOperationException {
        popFragment(null);
    }

    /**
     * Pop the current stack until a given tag is found. If the tag is not found, the stack will popFragment until it is at
     * the root fragment
     *
     * @param transactionOptions Transaction options to be displayed
     */
    public void popFragments(int popDepth, @Nullable NavigationTransactionOptions transactionOptions) throws UnsupportedOperationException {
        if (isRootFragment()) {
            throw new UnsupportedOperationException(
                    "You can not popFragment the rootFragment. If you need to change this fragment, use replaceFragment(fragment)");
        } else if (popDepth < 1) {
            throw new UnsupportedOperationException("popFragments parameter needs to be greater than 0");
        } else if (selectedTabIndex == NO_TAB) {
            throw new UnsupportedOperationException("You can not pop fragments when no tab is selected");
        }

        //If our popDepth is big enough that it would just clear the stack, then call that.
        if (popDepth >= fragmentStacks.get(selectedTabIndex).size() - 1) {
            clearStack(transactionOptions);
            return;
        }

        Fragment fragment;
        FragmentTransaction ft = createTransactionWithOptions(transactionOptions);

        //Pop the number of the fragments on the stack and remove them from the FragmentManager
        for (int i = 0; i < popDepth; i++) {
            fragment = fragmentManager.findFragmentByTag(fragmentStacks.get(selectedTabIndex).pop().getTag());
            if (fragment != null) {
                ft.remove(fragment);
            }
        }

        //Attempt to reattach previous fragment
        fragment = reattachPreviousFragment(ft);

        boolean bShouldPush = false;
        //If we can't reattach, either pull from the stack, or create a new root fragment
        if (fragment != null) {
            ft.commit();
        } else {
            if (!fragmentStacks.get(selectedTabIndex).isEmpty()) {
                fragment = fragmentStacks.get(selectedTabIndex).peek();
                ft.add(containerId, fragment, fragment.getTag());
                ft.commit();
            } else {
                fragment = getRootFragment(selectedTabIndex);
                ft.add(containerId, fragment, generateTag(fragment));
                ft.commit();

                bShouldPush = true;
            }
        }

        executePendingTransactions();

        //Need to have this down here so that that tag has been
        // committed to the fragment before we add to the stack
        if (bShouldPush) {
            fragmentStacks.get(selectedTabIndex).push(fragment);
        }

        currentFragment = fragment;
        if (transactionListener != null) {
            transactionListener.onFragmentTransaction(currentFragment, TransactionType.POP);
        }
    }

    /**
     * Pop the current fragment from the current tab
     */
    public void popFragments(int popDepth) throws UnsupportedOperationException {
        popFragments(popDepth, null);
    }

    /**
     * Clears the current tab's stack to get to just the bottom Fragment. This will reveal the root fragment
     *
     * @param transactionOptions Transaction options to be displayed
     */
    public void clearStack(@Nullable NavigationTransactionOptions transactionOptions) {
        try {
            if (selectedTabIndex == NO_TAB) {
                return;
            }

            //Grab Current stack
            Stack<Fragment> fragmentStack = fragmentStacks.get(selectedTabIndex);

            // Only need to start popping and reattach if the stack is greater than 1
            if (fragmentStack.size() > 1) {
                Fragment fragment;
                FragmentTransaction ft = createTransactionWithOptions(transactionOptions);

                //Pop all of the fragments on the stack and remove them from the FragmentManager
                while (fragmentStack.size() > 1) {
                    fragment = fragmentManager.findFragmentByTag(fragmentStack.pop().getTag());
                    if (fragment != null) {
                        ft.remove(fragment);
                    }
                }

                //Attempt to reattach previous fragment
                fragment = reattachPreviousFragment(ft);

                boolean bShouldPush = false;
                //If we can't reattach, either pull from the stack, or create a new root fragment
                if (fragment != null) {
                    ft.commit();
                } else {
                    if (!fragmentStack.isEmpty()) {
                        fragment = fragmentStack.peek();
                        ft.add(containerId, fragment, fragment.getTag());
                        ft.commit();
                    } else {
                        fragment = getRootFragment(selectedTabIndex);
                        ft.add(containerId, fragment, generateTag(fragment));
                        ft.commit();

                        bShouldPush = true;
                    }
                }

                executePendingTransactions();

                if (bShouldPush) {
                    fragmentStacks.get(selectedTabIndex).push(fragment);
                }

                //Update the stored version we have in the list
                fragmentStacks.set(selectedTabIndex, fragmentStack);

                currentFragment = fragment;
                if (transactionListener != null) {
                    transactionListener.onFragmentTransaction(currentFragment, TransactionType.POP);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the current tab's stack to get to just the bottom Fragment. This will reveal the root fragment.
     */
    public void clearStack() {
        clearStack(null);
    }

    /**
     * Replace the current fragment
     *
     * @param fragment           the fragment to be shown instead
     * @param transactionOptions Transaction options to be displayed
     */
    public void replaceFragment(@NonNull Fragment fragment, @Nullable NavigationTransactionOptions transactionOptions) {
        Fragment poppingFrag = getCurrentFragment();

        if (poppingFrag != null) {
            FragmentTransaction ft = createTransactionWithOptions(transactionOptions);

            //overly cautious fragment popFragment
            Stack<Fragment> fragmentStack = fragmentStacks.get(selectedTabIndex);
            if (!fragmentStack.isEmpty()) {
                fragmentStack.pop();
            }

            String tag = generateTag(fragment);
            ft.setCustomAnimations(fade_in, fade_out, fade_in, fade_out);
            ft.replace(containerId, fragment, tag);

            //Commit our transactions
            ft.commit();

            executePendingTransactions();

            fragmentStack.push(fragment);
            currentFragment = fragment;

            if (transactionListener != null) {
                transactionListener.onFragmentTransaction(currentFragment, TransactionType.REPLACE);

            }
        }
    }

    /**
     * Replace the current fragment
     *
     * @param fragment the fragment to be shown instead
     */
    public void replaceFragment(@NonNull Fragment fragment) {
        replaceFragment(fragment, null);
    }
    //endregion

    //region Private helper functions
    /**
     * Helper function to make sure that we are starting with a clean slate and to perform our first fragment interaction.
     *
     * @param index the tab index to initialize to
     */
    private void initialize(@TabIndex final int index) {
        selectedTabIndex = index;
        if (selectedTabIndex > fragmentStacks.size()) {
            throw new IndexOutOfBoundsException("Starting index cannot be larger than the number of stacks");
        }

        selectedTabIndex = index;
        clearFragmentManager();

        if (index == NO_TAB) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction ft = createTransactionWithOptions(null);
                Fragment fragment = getRootFragment(index);
                ft.add(containerId, fragment, generateTag(fragment));
                ft.commit();
                executePendingTransactions();

                currentFragment = fragment;
                if (transactionListener != null) {
                    transactionListener.onTabTransaction(currentFragment, selectedTabIndex);
                }
            }
        });
    }

    /**
     * Helper function to get the root fragment for a given index. This is done by either passing them in the constructor, or dynamically via NavListener.
     *
     * @param index The tab index to get this fragment from
     * @return The root fragment at this index
     * @throws IllegalStateException This will be thrown if we can't find a rootFragment for this index. Either because you didn't provide it in the
     *                               constructor, or because your RootFragmentListener.getRootFragment(index) isn't returning a fragment for this index.
     */
    @NonNull
    @CheckResult
    private Fragment getRootFragment(int index) throws IllegalStateException {
        Fragment fragment = null;
        if (!fragmentStacks.get(index).isEmpty()) {
            fragment = fragmentStacks.get(index).peek();
        } else if (rootFragmentListener != null) {
            fragment = rootFragmentListener.getRootFragment(index);

            if (selectedTabIndex != NO_TAB) {
                fragmentStacks.get(selectedTabIndex).push(fragment);
            }

        }
        if (fragment == null) {
            throw new IllegalStateException("Either you haven't past in a fragment at this index in your constructor, or you haven't " +
                    "provided a way to create it while via your RootFragmentListener.getRootFragment(index)");
        }

        return fragment;
    }

    /**
     * Will attempt to reattach a previous fragment in the FragmentManager, or return null if not able to.
     *
     * @param ft current fragment transaction
     * @return Fragment if we were able to find and reattach it
     */
    @Nullable
    private Fragment reattachPreviousFragment(@NonNull FragmentTransaction ft) {
        Stack<Fragment> fragmentStack = fragmentStacks.get(selectedTabIndex);
        Fragment fragment = null;
        if (!fragmentStack.isEmpty()) {
            fragment = fragmentManager.findFragmentByTag(fragmentStack.peek().getTag());
            if (fragment != null) {
                ft.attach(fragment);
            }
        }
        return fragment;
    }

    /**
     * Attempts to detach any current fragment if it exists, and if none is found, returns.
     *
     * @param ft the current transaction being performed
     */
    private void detachCurrentFragment(@NonNull FragmentTransaction ft) {
        Fragment oldFrag = getCurrentFragment();
        if (oldFrag != null) {
            ft.detach(oldFrag);
        }
    }

    /**
     * Helper function to attempt to get current fragment
     *
     * @return Fragment the current frag to be returned
     */
    @Nullable
    @CheckResult
    public Fragment getCurrentFragment() {
        //Attempt to used stored current fragment
        if (currentFragment != null) {
            return currentFragment;
        } else if (selectedTabIndex == NO_TAB) {
            return null;
        }
        //if not, try to pull it from the stack
        else {
            Stack<Fragment> fragmentStack = fragmentStacks.get(selectedTabIndex);
            if (!fragmentStack.isEmpty()) {
                currentFragment = fragmentManager.findFragmentByTag(fragmentStacks.get(selectedTabIndex).peek().getTag());
            }
        }
        return currentFragment;
    }

    /**
     * Create a unique fragment tag so that we can grab the fragment later from the FragmentManger
     *
     * @param fragment The fragment that we're creating a unique tag for
     * @return a unique tag using the fragment's class name
     */
    @NonNull
    @CheckResult
    private String generateTag(@NonNull Fragment fragment) {
        return fragment.getClass().getName() + ++tagCount;
    }

    /**
     * This check is here to prevent recursive entries into executePendingTransactions
     */
    private void executePendingTransactions() {
        if (!executingTransaction) {
            executingTransaction = true;
            try {
                fragmentManager.executePendingTransactions();
                executingTransaction = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Private helper function to clear out the fragment manager on initialization. All fragment management should be done via FragNav.
     */
    private void clearFragmentManager() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (fragmentManager.getFragments() != null) {
                        FragmentTransaction ft = createTransactionWithOptions(null);
                        for (Fragment fragment : fragmentManager.getFragments()) {
                            if (fragment != null) {
                                ft.remove(fragment);
                            }
                        }
                        ft.commit();
                        executePendingTransactions();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Setup a fragment transaction with the given option
     *
     * @param transactionOptions The options that will be set for this transaction
     */
    @CheckResult
    private FragmentTransaction createTransactionWithOptions(@Nullable NavigationTransactionOptions transactionOptions) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (transactionOptions == null) {
            transactionOptions = defaultTransactionOptions;
        }
        if (transactionOptions != null) {

            ft.setCustomAnimations(transactionOptions.enterAnimation, transactionOptions.exitAnimation,
                    transactionOptions.popEnterAnimation, transactionOptions.popExitAnimation);
            ft.setTransitionStyle(transactionOptions.transitionStyle);

            ft.setTransition(transactionOptions.transition);

            if (transactionOptions.sharedElements != null) {
                for (Pair<View, String> sharedElement : transactionOptions.sharedElements) {
                    ft.addSharedElement(sharedElement.first, sharedElement.second);
                }
            }

            if (transactionOptions.breadCrumbTitle != null) {
                ft.setBreadCrumbTitle(transactionOptions.breadCrumbTitle);
            }

            if (transactionOptions.breadCrumbShortTitle != null) {
                ft.setBreadCrumbShortTitle(transactionOptions.breadCrumbShortTitle);

            }
        }
        return ft;
    }

    //endregion

    //region Public helper functions

    /**
     * Get the number of fragment stacks
     *
     * @return the number of fragment stacks
     */
    @CheckResult
    public int getSize() {
        return fragmentStacks.size();
    }

    /**
     * Get a copy of the stack at a given index
     *
     * @return requested stack
     */
    @SuppressWarnings("unchecked")
    @CheckResult
    @Nullable
    public Stack<Fragment> getStack(@TabIndex int index) {
        if (index == NO_TAB) return null;
        if (index >= fragmentStacks.size()) {
            throw new IndexOutOfBoundsException("Can't get an index that's larger than we've setup");
        }
        return (Stack<Fragment>) fragmentStacks.get(index).clone();
    }

    /**
     * Get a copy of the current stack that is being displayed
     *
     * @return Current stack
     */
    @SuppressWarnings("unchecked")
    @CheckResult
    @Nullable
    public Stack<Fragment> getCurrentStack() {
        return getStack(selectedTabIndex);
    }

    /**
     * Get the index of the current stack that is being displayed
     *
     * @return Current stack index
     */
    @CheckResult
    @TabIndex
    public int getCurrentStackIndex() {
        return selectedTabIndex;
    }

    /**
     * @return If true, you are at the bottom of the stack
     * (Consider using replaceFragment if you need to change the root fragment for some reason)
     * else you can popFragment as needed as your are not at the root
     */
    @CheckResult
    public boolean isRootFragment() {
        Stack<Fragment> stack = getCurrentStack();

        return stack == null || stack.size() == 1;
    }

    //endregion

    //region SavedInstanceState

    /**
     * Call this in your Activity's onSaveInstanceState(Bundle outState) method to save the instance's state.
     *
     * @param outState The Bundle to save state information to
     */
    public void onSaveInstanceState(@NonNull Bundle outState) {

        // Write tag count
        outState.putInt(EXTRA_TAG_COUNT, tagCount);

        // Write select tab
        outState.putInt(EXTRA_SELECTED_TAB_INDEX, selectedTabIndex);

        // Write current fragment
        if (currentFragment != null) {
            outState.putString(EXTRA_CURRENT_FRAGMENT, currentFragment.getTag());
        }

        // Write stacks
        try {
            final JSONArray stackArrays = new JSONArray();

            for (Stack<Fragment> stack : fragmentStacks) {
                final JSONArray stackArray = new JSONArray();

                for (Fragment fragment : stack) {
                    stackArray.put(fragment.getTag());
                }

                stackArrays.put(stackArray);
            }

            outState.putString(EXTRA_FRAGMENT_STACK, stackArrays.toString());
        } catch (Throwable t) {
            // Nothing we can do
        }
    }

    /**
     * Restores this instance to the state specified by the contents of savedInstanceState
     *
     * @param savedInstanceState The bundle to restore from
     * @param rootFragments      List of root fragments from which to initialize empty stacks. If null, pull fragments from RootFragmentListener.
     * @return true if successful, false if not
     */
    private boolean restoreFromBundle(@Nullable Bundle savedInstanceState, @Nullable List<Fragment> rootFragments) {
        if (savedInstanceState == null) {
            return false;
        }

        // Restore tag count
        tagCount = savedInstanceState.getInt(EXTRA_TAG_COUNT, 0);

        // Restore current fragment
        currentFragment = fragmentManager.findFragmentByTag(savedInstanceState.getString(EXTRA_CURRENT_FRAGMENT));

        // Restore fragment stacks
        try {
            final JSONArray stackArrays = new JSONArray(savedInstanceState.getString(EXTRA_FRAGMENT_STACK));

            for (int x = 0; x < stackArrays.length(); x++) {
                final JSONArray stackArray = stackArrays.getJSONArray(x);
                final Stack<Fragment> stack = new Stack<>();

                if (stackArray.length() == 1) {
                    final String tag = stackArray.getString(0);
                    final Fragment fragment;

                    if (tag == null || "null".equalsIgnoreCase(tag)) {
                        if (rootFragments != null) {
                            fragment = rootFragments.get(x);
                        } else {
                            fragment = getRootFragment(x);
                        }

                    } else {
                        fragment = fragmentManager.findFragmentByTag(tag);
                    }

                    if (fragment != null) {
                        stack.add(fragment);
                    }
                } else {
                    for (int y = 0; y < stackArray.length(); y++) {
                        final String tag = stackArray.getString(y);

                        if (tag != null && !"null".equalsIgnoreCase(tag)) {
                            final Fragment fragment = fragmentManager.findFragmentByTag(tag);

                            if (fragment != null) {
                                stack.add(fragment);
                            }
                        }
                    }
                }

                fragmentStacks.add(stack);
            }
            // Restore selected tab if we have one
            switch (savedInstanceState.getInt(EXTRA_SELECTED_TAB_INDEX)) {
                case TAB1:
                    switchTab(TAB1);
                    break;
                case TAB2:
                    switchTab(TAB2);
                    break;
//                case TAB3:
//                    switchTab(TAB3);
//                    break;
                case TAB4:
                    switchTab(TAB4);
                    break;
                case TAB5:
                    switchTab(TAB5);
                    break;
            }

            //Successfully restored state
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
    //endregion

    public enum TransactionType {
        PUSH,
        POP,
        REPLACE
    }

    //Declare the TabIndex annotation
    @IntDef({NO_TAB, TAB1, TAB2, TAB4, TAB5})// TAB3,
    @Retention(RetentionPolicy.SOURCE)
    public @interface TabIndex {}

    // Declare Transit Styles
    @IntDef({FragmentTransaction.TRANSIT_NONE, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, FragmentTransaction.TRANSIT_FRAGMENT_CLOSE, FragmentTransaction.TRANSIT_FRAGMENT_FADE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Transit {}

    public interface RootFragmentListener {
        /**
         * Dynamically create the Fragment that will go on the bottom of the stack
         *
         * @param index the index that the root of the stack Fragment needs to go
         * @return the new Fragment
         */
        Fragment getRootFragment(int index);
    }

    public interface TransactionListener {
        void onTabTransaction(Fragment fragment, int index);
        void onFragmentTransaction(Fragment fragment, TransactionType transactionType);
    }

    public static final class Builder {
        private final int containerId;
        private FragmentManager fragmentManager;
        private RootFragmentListener rootFragmentListener;
        @TabIndex private int mSelectedTabIndex = TAB1;
        private TransactionListener transactionListener;
        private Activity activity;
        private NavigationTransactionOptions defaultTransactionOptions;
        private int numberOfTabs = 0;
        private List<Fragment> rootFragments;
        private Bundle savedInstanceState;

        public Builder(@Nullable Bundle savedInstanceState, FragmentManager fragmentManager, int containerId) {
            this.savedInstanceState = savedInstanceState;
            this.fragmentManager = fragmentManager;
            this.containerId = containerId;
        }

        /**
         * @param selectedTabIndex The initial tab index to be used must be in range of rootFragments size
         */
        public Builder selectedTabIndex(@TabIndex int selectedTabIndex) {
            mSelectedTabIndex = selectedTabIndex;
            if (rootFragments != null && mSelectedTabIndex > numberOfTabs) {
                throw new IndexOutOfBoundsException("Starting index cannot be larger than the number of stacks");
            }
            return this;
        }

        /**
         * @param rootFragment A single root fragment. This library can still be helpful when managing a single stack of fragments
         */
        public Builder rootFragment(Fragment rootFragment) {
            rootFragments = new ArrayList<>(1);
            rootFragments.add(rootFragment);
            numberOfTabs = 1;
            return rootFragments(rootFragments);
        }

        /**
         * @param rootFragments a list of root fragments. root Fragments are the root fragments that exist on any tab structure. If only one fragment is sent in, fragnav will still manage
         *                      transactions
         */
        public Builder rootFragments(@NonNull List<Fragment> rootFragments) {
            this.rootFragments = rootFragments;
            numberOfTabs = rootFragments.size();
            if (numberOfTabs > MAX_NUM_TABS) {
                throw new IllegalArgumentException("Number of root fragments cannot be greater than " + MAX_NUM_TABS);
            }
            return this;
        }

        /**
         * @param transactionOptions The default transaction options to be used unless otherwise defined.
         */
        public Builder defaultTransactionOptions(@NonNull NavigationTransactionOptions transactionOptions) {
            defaultTransactionOptions = transactionOptions;
            return this;
        }

        /**
         * @param rootFragmentListener a listener that allows for dynamically creating root fragments
         * @param numberOfTabs         the number of tabs that will be switched between
         */
        public Builder rootFragmentListener(RootFragmentListener rootFragmentListener, int numberOfTabs) {
            this.rootFragmentListener = rootFragmentListener;
            this.numberOfTabs = numberOfTabs;
            if (this.numberOfTabs > MAX_NUM_TABS) {
                throw new IllegalArgumentException("Number of tabs cannot be greater than " + MAX_NUM_TABS);
            }
            return this;
        }

        /**
         * @param val A listener to be implemented (typically within the main activity) to fragment transactions (including tab switches)
         */
        public Builder transactionListener(TransactionListener val) {
            transactionListener = val;
            return this;
        }

        @NonNull
        public NavigationController build() {
            if (rootFragmentListener == null && rootFragments == null) {
                throw new IndexOutOfBoundsException("Either a root fragment(s) needs to be set, or a fragment listener");
            }
            return new NavigationController(this, savedInstanceState);
        }

        @Contract(pure = true)
        public Builder activity(Activity activity) {
            this.activity = activity;
            return this;
        }
    }
}
