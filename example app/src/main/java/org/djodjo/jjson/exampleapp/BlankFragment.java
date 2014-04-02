package org.djodjo.jjson.exampleapp;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class BlankFragment extends Fragment {

    protected int layout = 0;

    public static BlankFragment newInstance() {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public BlankFragment() {
        layout = R.layout.fragment_blank;
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
        View v = inflater.inflate(layout, container, false);
       try {
           v.findViewById(R.id.btn_go).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   execGo();
               }
           });
       } catch(Exception ex) {}

        return v;
    }

    public void execGo() {
        //do nothing here
    }


}
