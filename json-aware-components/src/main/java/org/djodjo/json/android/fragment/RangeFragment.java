/*
 * Copyright (C) 2014 Kalin Maldzhanski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.djodjo.json.android.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.djodjo.json.android.R;
import org.djodjo.widget.MultiSlider;


/**
 * this fragment renders range objects with properties of the form:
 "properties" : {
     "min": {
         "type" : "integer",
         "minimum" : 0,
         "maximum" : 10000
     },
     "max": {
         "type" : "integer",
         "minimum" : 0,
         "maximum" : 10000
     }
 }

 where min and max are not required.
 the slider has extra positions at the end and beginning and when selected no min and max values are returned, as assuming infinite/undefined range
 *
 */

public class RangeFragment extends BasePropertyFragment {

    public final static int LAYOUT_RANGE_SLIDER = R.layout.fragment_range_slider;
    public static final String ARG_MIN_BUNDLE = "min";
    public static final String ARG_MAX_BUNDLE = "max";

    protected int minVal1;
    protected int maxVal1;

    protected int minVal2;
    protected int maxVal2;

    public RangeFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        switch (displayType) {
            case DisplayType.DISPLAY_TYPE_SLIDER: return LAYOUT_RANGE_SLIDER;
        }
        return LAYOUT_RANGE_SLIDER;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            minVal1 = getArguments().getBundle(ARG_MIN_BUNDLE).getInt(NumberFragment.ARG_MINIMUM);
            maxVal1 = getArguments().getBundle(ARG_MIN_BUNDLE).getInt(NumberFragment.ARG_MAXIMUM);
            minVal2 = getArguments().getBundle(ARG_MAX_BUNDLE).getInt(NumberFragment.ARG_MINIMUM);
            maxVal2 = getArguments().getBundle(ARG_MAX_BUNDLE).getInt(NumberFragment.ARG_MAXIMUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        final TextView min = (TextView) v.findViewById(R.id.minValue);
        final TextView max = (TextView) v.findViewById(R.id.maxValue);

        if(min!=null) {
            min.setTextAppearance(getActivity(), styleValue);
        }
        if(max!=null) {
            max.setTextAppearance(getActivity(), styleValue);
        }



        MultiSlider seekBar = (MultiSlider)v.findViewById(R.id.range_slider);

        if(seekBar!=null) {
            seekBar.setMax(maxVal2, true, true);
            seekBar.setMin(minVal1, true, true);

            min.setText(String.valueOf(seekBar.getThumb(0).getValue()));
            max.setText(String.valueOf(seekBar.getThumb(1).getValue()));

            seekBar.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
                @Override
                public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                    if (thumbIndex == 0) {
                        if(min!=null) {
                            min.setText(String.valueOf(value));
                        }
                    } else {
                        if(max!=null) {
                            max.setText(String.valueOf(value));
                        }
                    }
                }
            });
        }
        return v;
    }
}
