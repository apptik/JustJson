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


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import org.djodjo.json.android.R;


public class LimitedNumberFragment extends BasePropertyFragment {

    public static final String ARG_MINIMUM = "minimum";
    public static final String ARG_MAXIMUM = "maximum";

    public final static int LAYOUT_NUMBER_TEXT = R.layout.fragment_number_text;
    public final static int LAYOUT_NUMBER_PICKER = R.layout.fragment_number_picker;
    public final static int LAYOUT_NUMBER_SLIDER = R.layout.fragment_number_slider;

    private int minimum;
    private int maximum;

    public LimitedNumberFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        int currLayoutId =  globalLayouts.get(ARG_GLOBAL_LIMITED_NUMBER_LAYOUT);
        if(currLayoutId==0) {
            currLayoutId = LAYOUT_NUMBER_SLIDER;
        }
        return currLayoutId;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            minimum = getArguments().getInt(NumberFragment.ARG_MINIMUM);
            maximum = getArguments().getInt(NumberFragment.ARG_MAXIMUM);
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if(layoutId == LAYOUT_NUMBER_SLIDER) {
            SeekBar seekBar = (SeekBar)v.findViewById(R.id.seekBar);
            if(buttonSelector!=0){
                seekBar.setThumb(getActivity().getResources().getDrawable(buttonSelector));
            } else if (customButtonSelectors!= null && customButtonSelectors.get(ARG_GLOBAL_SLIDER_THUMB_SELECTOR) != 0)
            {
                seekBar.setThumb(getActivity().getResources().getDrawable(customButtonSelectors.get(ARG_GLOBAL_SLIDER_THUMB_SELECTOR)));
            }

            if(customButtonSelectors!= null && customButtonSelectors.get(ARG_GLOBAL_SLIDER_PROGRESS_DRAWABLE) != 0)
            {
                seekBar.setProgressDrawable(getActivity().getResources().getDrawable(customButtonSelectors.get(ARG_GLOBAL_SLIDER_PROGRESS_DRAWABLE)));
            }

            seekBar.setMax(maximum-minimum);

            final TextView propValue = (TextView) v.findViewById(R.id.prop_value);
            propValue.setTextAppearance(getActivity(), styleValue);

            int value = seekBar.getProgress()-minimum;
            propValue.setText(String.valueOf(value));

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int value = progress-minimum;
                    propValue.setText(String.valueOf(value));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

        } else {
            TextView propValue = (TextView) v.findViewById(R.id.prop_value);
            if(propValue!=null) {
                propValue.setTextAppearance(getActivity(), styleValue);
            }
        }

        return v;
    }
}
