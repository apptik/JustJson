package org.djodjo.jjson.atools;


import android.os.Bundle;

import java.util.ArrayList;


public class NumberFragment extends BasePropertyFragment {

    public final static int LAYOUT_NUMBER_TEXT = R.layout.fragment_number_text;
    public final static int LAYOUT_NUMBER_PICKER = R.layout.fragment_number_picker;
    public final static int LAYOUT_NUMBER_SLIDER = R.layout.fragment_number_slider;

    private ArrayList<String> options;

    public NumberFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        switch (displayType) {
            case FragmentBuilder.DISPLAY_TYPE_TEXT: return LAYOUT_NUMBER_TEXT;
            case FragmentBuilder.DISPLAY_TYPE_NUMBER_PICKER: return LAYOUT_NUMBER_PICKER;
            case FragmentBuilder.DISPLAY_TYPE_SLIDER: return LAYOUT_NUMBER_SLIDER;
        }
        return LAYOUT_NUMBER_SLIDER;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            options = getArguments().getStringArrayList(FragmentBuilder.ARG_OPTIONS);
        }
    }

}
