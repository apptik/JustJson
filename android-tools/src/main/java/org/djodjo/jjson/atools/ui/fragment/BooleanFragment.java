package org.djodjo.jjson.atools.ui.fragment;


import android.os.Bundle;

import org.djodjo.jjson.atools.FragmentBuilder;
import org.djodjo.jjson.atools.R;


public class BooleanFragment extends BasePropertyFragment {

    public final static int LAYOUT_BOOL_CHECKBOX = R.layout.fragment_bool_check;
    public final static int LAYOUT_BOOL_SWITCH = R.layout.fragment_bool_switch;
    public final static int LAYOUT_BOOL_TOGGLE = R.layout.fragment_bool_toggle;


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

        }
    }

}
