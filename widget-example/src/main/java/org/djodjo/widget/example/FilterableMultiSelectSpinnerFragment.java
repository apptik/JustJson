package org.djodjo.widget.example;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.djodjo.widget.FilterableMultiSelectSpinner;

import java.util.ArrayList;


public class FilterableMultiSelectSpinnerFragment extends Fragment {


    public FilterableMultiSelectSpinnerFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_filterable_multi_select_spinner, container, false);

        FilterableMultiSelectSpinner multiSelectSpinner1 =  (FilterableMultiSelectSpinner)v.findViewById(R.id.multiSelectSpinner1);
        FilterableMultiSelectSpinner multiSelectSpinner2 =  (FilterableMultiSelectSpinner)v.findViewById(R.id.multiSelectSpinner2);
        ArrayList<String> items =  new ArrayList<String>();

        for(int i=1;i<13;i++)
        items.add("Item "+i);

        multiSelectSpinner1.setItems(items,"all simple items", "please select", new FilterableMultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {

            }
        }).setMaxSelectedItems(3);

        multiSelectSpinner2.setListAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_multiple_choice,items),"all adapter items","please select",new FilterableMultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {

            }
        }).setSelectAll(true).setMinSelectedItems(3);
        return v;
    }


}
