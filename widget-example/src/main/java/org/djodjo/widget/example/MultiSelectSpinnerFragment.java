package org.djodjo.widget.example;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.djodjo.widget.MultiSelectSpinner;

import java.util.ArrayList;


public class MultiSelectSpinnerFragment extends Fragment {


    public MultiSelectSpinnerFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_multi_select_spinner, container, false);

        MultiSelectSpinner multiSelectSpinner =  (MultiSelectSpinner)v.findViewById(R.id.multiSelectSpinner);
        ArrayList<String> items =  new ArrayList<String>();

        for(int i=1;i<33;i++)
        items.add("Item "+i);

        multiSelectSpinner.setItems(items,"all", new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {

            }
        });
        return v;
    }


}
