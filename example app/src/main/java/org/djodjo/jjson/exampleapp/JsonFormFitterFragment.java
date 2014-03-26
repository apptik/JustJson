package org.djodjo.jjson.exampleapp;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class JsonFormFitterFragment extends Fragment {




    public static JsonFormFitterFragment newInstance(String param1, String param2) {
        JsonFormFitterFragment fragment = new JsonFormFitterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public JsonFormFitterFragment() {
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
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }


}
