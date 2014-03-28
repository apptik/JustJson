package org.djodjo.jjson.atools.ui.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.djodjo.jjson.atools.FragmentBuilder;
import org.djodjo.jjson.atools.R;
import org.djodjo.jjson.atools.ui.widget.RangeSlider;


public class RangeFragment<T extends Number> extends BasePropertyFragment {

    public final static int LAYOUT_RANGE_SLIDER = R.layout.fragment_range_slider;
    public static final String ARG_MINVAL = "min_val";
    public static final String ARG_MAXVAL = "max_val";

    private T minVal;
    private T maxVal;

    public RangeFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        switch (displayType) {
            case FragmentBuilder.DISPLAY_TYPE_SLIDER: return LAYOUT_RANGE_SLIDER;
        }
        return LAYOUT_RANGE_SLIDER;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            minVal = (T) getArguments().get(ARG_MINVAL);
            maxVal = (T) getArguments().get(ARG_MAXVAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        final TextView min = (TextView) v.findViewById(R.id.minValue);
        final TextView max = (TextView) v.findViewById(R.id.maxValue);


        RangeSlider<T> seekBar = new RangeSlider<T>(minVal, maxVal, getActivity());

        seekBar.setOnRangeSeekBarChangeListener(new RangeSlider.OnRangeSeekBarChangeListener<T>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSlider<?> bar, T minValue, T maxValue) {
                //TODO logging
                Log.i("", "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);

                min.setText(minValue.toString());
                max.setText(maxValue.toString());

            }
        });

        // add RangeSeekBar to pre-defined layout
        ViewGroup layout = (ViewGroup) v.findViewById(R.id.range_slider);
        layout.addView(seekBar);

        return v;
    }
}
