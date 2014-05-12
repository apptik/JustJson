package org.djodjo.widget.example;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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

        MultiSelectSpinner multiSelectSpinner1 =  (MultiSelectSpinner)v.findViewById(R.id.multiSelectSpinner1);
        MultiSelectSpinner multiSelectSpinner2 =  (MultiSelectSpinner)v.findViewById(R.id.multiSelectSpinner2);
        ArrayList<String> items =  new ArrayList<String>();

        for(int i=1;i<13;i++)
        items.add("Item "+i);

        multiSelectSpinner1.setItems(items,"all simple items", "please select", new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {

            }
        }).setMaxSelectedItems(3);

        multiSelectSpinner2.setListAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_multiple_choice,items),"all adapter items","please select",new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {

            }
        }).setSelectAll(true).setMinSelectedItems(3);
        return v;
    }


}
