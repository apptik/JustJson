package org.djodjo.jjson.atools.ui.fragment;


import android.os.Bundle;

import org.djodjo.jjson.atools.R;
import org.djodjo.jjson.atools.FragmentBuilder;

import java.util.ArrayList;


public class EnumFragment extends BasePropertyFragment {

    public final static int LAYOUT_ENUM_RADIO = R.layout.fragment_enum_radio;
    public final static int LAYOUT_ENUM_SPINNER = R.layout.fragment_enum_spinner;

    public static final String ARG_OPTIONS = "options";

    private ArrayList<String> options;

    public EnumFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        switch (displayType) {
            case FragmentBuilder.DISPLAY_TYPE_SPINNER: return LAYOUT_ENUM_SPINNER;
            case FragmentBuilder.DISPLAY_TYPE_RADIO: return LAYOUT_ENUM_RADIO;
        }
        return LAYOUT_ENUM_RADIO;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            options = getArguments().getStringArrayList(ARG_OPTIONS);
        }
    }



}
