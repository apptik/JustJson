package org.djodjo.jjson.atools;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class EnumFragment extends Fragment {
    public final static int TYPE_RADIO = R.layout.fragment_enum_radio;
    public final static int TYPE_SPINNER = R.layout.fragment_enum_spinner;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "displayType";
    private static final String ARG_PARAM2 = "values";

    // TODO: Rename and change types of parameters
    private int displayType = TYPE_RADIO;
    private String[] values;



    // TODO: Rename and change types and number of parameters
    public static EnumFragment newInstance(int displayType, String[] values) {
        EnumFragment fragment = new EnumFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, displayType);
        args.putStringArray(ARG_PARAM2, values);
        fragment.setArguments(args);
        return fragment;
    }
    public EnumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            displayType = getArguments().getInt(ARG_PARAM1);
            values = getArguments().getStringArray(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(displayType, container, false);
    }


}
