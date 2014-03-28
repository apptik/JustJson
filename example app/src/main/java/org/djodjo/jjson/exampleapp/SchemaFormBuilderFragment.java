package org.djodjo.jjson.exampleapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.djodjo.jjson.atools.LayoutBuilder;
import org.djodjo.json.JsonElement;
import org.djodjo.json.JsonException;
import org.djodjo.json.schema.SchemaV4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Scanner;

public class SchemaFormBuilderFragment extends BlankFragment {

    TextView txtInput;

    public SchemaFormBuilderFragment() {
        layout = R.layout.fragment_formbuilder;
    }

    @Override
    public void execGo() {
        final SchemaV4 schema = new SchemaV4();
        try {
            schema.wrap(JsonElement.readFrom(txtInput.getText().toString()));
        } catch (JsonException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                LayoutBuilder<SchemaV4> lb  = new LayoutBuilder<SchemaV4>(schema, getFragmentManager());
                lb.build(R.id.form_container);
            }
        }).start();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        v.findViewById(R.id.txt_input);
        InputStream is = null;
        try {
            is = getActivity().getAssets().open("schema1.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            StringWriter sw = new StringWriter();
            txtInput = (TextView)v.findViewById(R.id.txt_input);
            txtInput.setText(new Scanner(is, "UTF-8").useDelimiter("\\A").next());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }
}
