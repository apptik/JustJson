package org.djodjo.jjson.atools.util;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.djodjo.jjson.atools.EnumControllerCallback;
import org.djodjo.jjson.atools.LayoutBuilder;
import org.djodjo.jjson.atools.R;
import org.djodjo.jjson.atools.ui.fragment.EnumFragment;
import org.djodjo.json.JsonException;
import org.djodjo.json.JsonObject;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaV4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class OneOfFragment extends Fragment implements EnumControllerCallback {

    private static final String ARG_SCHEMAS = "schemas";
    private static final String ARG_CONTROLLERS = "controllers";


    RadioGroup oneOfRadioGroup;

    private ArrayList<String> controllers = null;
    private ArrayList<Schema> schemas =  new ArrayList<Schema>();
    private HashMap<Integer,LayoutBuilder<Schema>> layoutBuilders = new HashMap<Integer,LayoutBuilder<Schema>>();

    public static OneOfFragment newInstance(ArrayList<String> schemas, ArrayList<String> controllers) {
        OneOfFragment fragment = new OneOfFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_SCHEMAS, schemas);
        args.putStringArrayList(ARG_CONTROLLERS, controllers);
        fragment.setArguments(args);
        return fragment;
    }
    public OneOfFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            controllers = getArguments().getStringArrayList(ARG_CONTROLLERS);
            ArrayList<String> stringSchemas = getArguments().getStringArrayList(ARG_SCHEMAS);
            for(String schema:stringSchemas) {
                try {
                    schemas.add((Schema)new SchemaV4().wrap(JsonObject.readFrom(schema)));
                } catch (JsonException e) {e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        if(oneOfRadioGroup!=null && oneOfRadioGroup.getVisibility() == View.VISIBLE) {
            oneOfRadioGroup.setOnCheckedChangeListener(null);
        }
        super.onDestroyView();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_one_of_radio, container, false);


        oneOfRadioGroup = (RadioGroup) v.findViewById(R.id.oneOfRadioGroup);
        oneOfRadioGroup.removeAllViews();
        if(controllers!=null && controllers.size()>0) {
            oneOfRadioGroup.setVisibility(View.GONE);
        } else {
            // --> Prepare default controller
            oneOfRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, final int checkedId) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            layoutBuilders.get(checkedId)
                                    .build(R.id.oneOfContainer);
                        }
                    }).start();
                }
            });
        }



        HashMap<String, ArrayList<String>> controllerOptions =  new HashMap<String, ArrayList<String>>();
        //init controllers
        if(controllers!=null && controllers.size()>0) {
            for (String controller : controllers) {
                controllerOptions.put(controller, new ArrayList<String>());
            }
        }

        // --> buildup options and create layout builders
        for(Schema schema:schemas) {
            int selectionId = 0;
            //build default controller
            if(oneOfRadioGroup.getVisibility() == View.VISIBLE) {
                RadioButton button = new RadioButton(getActivity());
                button.setText(schema.getTitle());
                oneOfRadioGroup.addView(button);
                selectionId = button.getId();
            } else {
                //build custom controller
                for(String controller:controllers) {
                    try {
                        controllerOptions.get(controller).addAll(FragmentTools.genEnumStringList(schema.getProperties().getValue(controller)));
                    } catch (JsonException e) {
                        e.printStackTrace();
                    }
                }
            }

            layoutBuilders.put(selectionId, new LayoutBuilder<Schema>(schema, getFragmentManager())
                            //ignore properties that are controllers as they are handled directly from here
                            .ignoreProperties(controllers)
            );
        }
        if(controllers != null && controllers.size()>0) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            for (String controller : controllers) {
                Fragment frag = new EnumFragment();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(EnumFragment.ARG_OPTIONS, controllerOptions.get(controller));
                frag.setArguments(bundle);
                transaction.add(container.getId(), frag, controller);
            }

            transaction.commit();
        }
        return v;
    }


    @Override
    public void onValueChanged(final int position, String value) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                layoutBuilders.get(position)
                        .build(R.id.oneOfContainer);
            }
        }).start();
    }
}
