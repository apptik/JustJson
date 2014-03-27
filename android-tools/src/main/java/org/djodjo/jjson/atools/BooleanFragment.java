package org.djodjo.jjson.atools;


import android.os.Bundle;

import java.util.ArrayList;


public class BooleanFragment extends BasePropertyFragment {

    public final static int LAYOUT_BOOL_CHECKBOX = R.layout.fragment_bool_check;
    public final static int LAYOUT_BOOL_SWITCH = R.layout.fragment_bool_switch;
    public final static int LAYOUT_BOOL_TOGGLE = R.layout.fragment_bool_toggle;

    private ArrayList<String> options;

    public BooleanFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        switch (displayType) {
            case FragmentBuilder.DISPLAY_TYPE_CHECKBOX: return LAYOUT_BOOL_CHECKBOX;
            case FragmentBuilder.DISPLAY_TYPE_SWITCH: return LAYOUT_BOOL_SWITCH;
            case FragmentBuilder.DISPLAY_TYPE_TOGGLE: return LAYOUT_BOOL_TOGGLE;
        }
        return LAYOUT_BOOL_SWITCH;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            options = getArguments().getStringArrayList(FragmentBuilder.ARG_OPTIONS);
        }
    }

}
