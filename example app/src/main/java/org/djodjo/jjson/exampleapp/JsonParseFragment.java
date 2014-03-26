package org.djodjo.jjson.exampleapp;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class JsonParseFragment extends Fragment {




    public static JsonParseFragment newInstance(String param1, String param2) {
        JsonParseFragment fragment = new JsonParseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public JsonParseFragment() {
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
