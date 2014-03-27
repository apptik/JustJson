package org.djodjo.jjson.atools.util;


import android.app.FragmentManager;

public class FragmentMaster {

    private static FragmentMaster instance = null;
    private FragmentManager fragmentManager;

    private FragmentMaster(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;

    }

    public static FragmentMaster get(FragmentManager fragmentManager) {
        instance = new FragmentMaster(fragmentManager);
        return instance;
    }

    public static FragmentMaster get() {
        return instance;
    }


}
