package org.djodjo.jjson.atools;


import android.os.Bundle;

import java.util.ArrayList;


public class StringFragment extends BasePropertyFragment {

    private ArrayList<String> options;

    public StringFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {

        return R.layout.fragment_string;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            options = getArguments().getStringArrayList(FragmentBuilder.ARG_OPTIONS);
        }
    }

}
