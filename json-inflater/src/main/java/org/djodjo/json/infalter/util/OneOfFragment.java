package org.djodjo.json.infalter.util;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import org.djodjo.json.JsonObject;
import org.djodjo.json.android.fragment.BasePropertyFragment;
import org.djodjo.json.android.fragment.ControllerCallback;
import org.djodjo.json.android.fragment.EnumFragment;
import org.djodjo.json.exception.JsonException;
import org.djodjo.json.infalter.LayoutBuilder;
import org.djodjo.json.infalter.R;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaV4;
import org.djodjo.json.util.LinkedTreeMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;


public class OneOfFragment extends Fragment implements ControllerCallback {

    private static final String ARG_SCHEMAS = "schemas";
    private static final String ARG_CONTROLLERS = "controllers";


    private static final String ARG_SETTING_BUNDLE = "settingsBundle";

    int maxRadioItems = 3;
    RadioGroup oneOfRadioGroup;
    Spinner oneOfSpinner;

    private ArrayList<String> controllers = null;
    private ArrayList<Schema> schemas =  new ArrayList<Schema>();

    /**
     * Bundle used only for property settings.
     * these are normally only passed to the other Layout builders
     */
    Bundle settingsArgs = null;

    private HashMap<ArrayList<Integer>,LayoutBuilder<Schema>> layoutBuilders =  new HashMap<ArrayList<Integer>, LayoutBuilder<Schema>>();

    //this is the current selection object used a key to find the right LayoutBuilder
    //its size should equal to controllers.size
    private LinkedTreeMap<String,Integer> currSelection = new LinkedTreeMap<String, Integer>();

    private boolean isRadioDisplay() {
        return schemas.size() <= maxRadioItems;
    }

    public static OneOfFragment newInstance(ArrayList<String> schemas, ArrayList<String> controllers, Bundle settingsBundle) {
        OneOfFragment oneOfFragment = new OneOfFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_SCHEMAS, schemas);
        args.putStringArrayList(ARG_CONTROLLERS, controllers);
        args.putBundle(ARG_SETTING_BUNDLE, settingsBundle);
        oneOfFragment.setArguments(args);
        return oneOfFragment;
    }
    public OneOfFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            settingsArgs = getArguments().getBundle(ARG_SETTING_BUNDLE);

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
        for (String controller : controllers) {
            currSelection.put(controller,0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = isRadioDisplay() ?
                inflater.inflate(R.layout.fragment_one_of_radio, container, false):
                inflater.inflate(R.layout.fragment_one_of_spinner, container, false);

        View footer =  v.findViewById(R.id.oneOfFooter);
        if(footer!=null)
            footer.setVisibility(View.GONE);

        if(isRadioDisplay()) {
            oneOfRadioGroup = (RadioGroup) v.findViewById(R.id.oneOfRadioGroup);
            oneOfRadioGroup.removeAllViews();
            oneOfSpinner = null;
        } else {
            oneOfSpinner = (Spinner) v.findViewById(R.id.oneofSpinner);
            oneOfRadioGroup = null;
        }

        if(controllers!=null && controllers.size()>0) {
            if(oneOfRadioGroup!= null)
                oneOfRadioGroup.setVisibility(View.GONE);
            if(oneOfSpinner!=null)
                oneOfSpinner.setVisibility(View.GONE);
        } else {
            // --> Prepare default controller
            if(oneOfRadioGroup!=null) {
                oneOfRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, final int checkedId) {

                        if (checkedId != -1)
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<Integer> hs = new ArrayList<Integer>();
                                    hs.add(checkedId);
                                    layoutBuilders.get(hs)
                                            .build(R.id.oneOfContainer);
                                }
                            }).start();
                    }
                });
            } else if(oneOfSpinner!=null) {
                oneOfSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                        if (position != -1)
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<Integer> hs = new ArrayList<Integer>();
                                    hs.add(position);
                                    layoutBuilders.get(hs)
                                            .build(R.id.oneOfContainer);
                                }
                            }).start();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        }
        performLayout();
        return v;
    }





    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onValueChanged(final String name, final int position) {
        if(position>=0)
            currSelection.put(name,position);

        new Thread(new Runnable() {
            @Override
            public void run() {
                LayoutBuilder<Schema> lb = layoutBuilders.get(new ArrayList<Integer>(currSelection.values()));
                //note that if controllers not chosen wisely there could be a combination where there are no layout matching

                if(lb!=null)
                    lb.build(R.id.oneOfContainer);
            }
        }).start();
    }


    private void performLayout() {
        //used to populate values for controllers
        LinkedTreeMap<String, LinkedHashSet<String>> controllerOptions =  new LinkedTreeMap<String, LinkedHashSet<String>>();

        //used to populate values for the default spinner controller
        ArrayList<String> spinnerOptions =  new ArrayList<String>();

        //the set of controller keys we will match the generated layout builder with
        //many hashsets can match one layout builder. Tha is because we can have > 1 controller value for a distinct anyOf schema
        ArrayList<ArrayList<Integer>> customCtrlKeys;

        //init controllers if any are set
        if(controllers!=null && controllers.size()>0) {
            for (String controller : controllers) {
                controllerOptions.put(controller, new LinkedHashSet<String>());
            }
        }

        // --> buildup options and create layout builders
        for(Schema schema:schemas) {
            customCtrlKeys =  null;
            int selectionId = 0;
            //build default controller
            if(oneOfRadioGroup!= null && oneOfRadioGroup.getVisibility() == View.VISIBLE) {
                RadioButton button = new RadioButton(getActivity());
                button.setText(schema.getTitle());
                oneOfRadioGroup.addView(button);
                selectionId = button.getId();
            } else if(oneOfSpinner != null && oneOfSpinner.getVisibility() == View.VISIBLE) {
                spinnerOptions.add(schema.getTitle());
                selectionId = spinnerOptions.size() - 1;
            } else if(controllers!=null) {


                customCtrlKeys =  new ArrayList<ArrayList<Integer>>();
                //add initial set, we normally use one set only if there is one item per controller
                customCtrlKeys.add(new ArrayList<Integer>());

                //gather all possible controller indexes
                for(int i =0; i< controllers.size(); i++) {
                    //current values to be added
                    ArrayList<String> newVals;
                    //current indexes of those values  which we check against in the callback
                    ArrayList<Integer> newIndexes = new ArrayList<Integer>();

                    try {
                        //the new values we need to find the keys for and add.
                        newVals = FragmentTools.genEnumStringList(schema.getProperties().getValue(controllers.get(i)));

                        //addall properties , note that not all may be new ones
                        controllerOptions.get(controllers.get(i)).addAll(newVals);

                        //then fetch indexes fo those
                        //TODO implement index aware set
                        ArrayList<String> tmpConvList = new ArrayList<String>(controllerOptions.get(controllers.get(i)));
                        for(String newVal:newVals) {
                            newIndexes.add(tmpConvList.indexOf(newVal));
                        }
                        //now fit in the sets
                        if(newIndexes.size()>1) {

                            //copy current sets n-1 times
                            int currCtrlKeysSize = customCtrlKeys.size();
                            for(int j=1; j<newIndexes.size(); j++) {
                                for(int n=0;n<currCtrlKeysSize;n++) {
                                    customCtrlKeys.add(new ArrayList<Integer>(customCtrlKeys.get(n)));
                                }
                            }

                            //then fit in the new controller index in each set
                            for(int j=0; j<newIndexes.size(); j++) {
                                for(int n=0;n<currCtrlKeysSize;n++) {
                                    customCtrlKeys.get(currCtrlKeysSize*j+n).add(newIndexes.get(j));
                                }
                            }

                        } else {
                            for(ArrayList<Integer> s:customCtrlKeys) {
                                s.add(newIndexes.get(0));
                            }
                        }

                    } catch (JsonException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(customCtrlKeys==null) {
                customCtrlKeys = new ArrayList<ArrayList<Integer>>();
                ArrayList<Integer> hs = new ArrayList<Integer>();
                hs.add(selectionId);
                customCtrlKeys.add(hs);
            }

            LayoutBuilder<Schema> lb = new LayoutBuilder<Schema>(schema, getFragmentManager())
                    //ignore properties that are controllers as they are handled directly from here
                    .setSettingsBundle(settingsArgs)
                    .ignoreProperties(controllers);

            for(ArrayList<Integer> customCtrlKey:customCtrlKeys) {
                layoutBuilders.put(customCtrlKey, lb);
            }

        }

        // --> build up data for spinner if we use spinner
        if(oneOfSpinner != null && oneOfSpinner.getVisibility() == View.VISIBLE) {
            SpinnerAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerOptions);
            oneOfSpinner.setAdapter(adapter);
        }


        //--> setup custom controllers
        if(controllers != null && controllers.size()>0) {

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            for (String controller : controllers) {
                android.app.Fragment frag = new EnumFragment();
                Bundle bundle = new Bundle();
                ArrayList<String> opts = new ArrayList<String>();
                opts.addAll(controllerOptions.get(controller));
                bundle.putStringArrayList(EnumFragment.ARG_OPTIONS, opts);
                bundle.putBoolean(EnumFragment.ARG_IS_CONTROLLER, true);
                bundle.putString(BasePropertyFragment.ARG_LABEL, controller);

               bundle.putInt(BasePropertyFragment.ARG_BUTTON_SELECTOR,
                       ((HashMap<String, Integer>) settingsArgs.getSerializable(LayoutBuilder.ARG_GLOBAL_BOTTON_SELECTORS)).get(BasePropertyFragment.ARG_GLOBAL_RADIOBUTTON_SELECTOR));
                //TODO doesnt work like this for fragment builder
                //bundle.putBundle(ARG_SETTING_BUNDLE, settingsArgs);
                frag.setArguments(bundle);
                transaction.add(R.id.oneOfControllers, frag, controller);
            }

            transaction.commit();
        }
    }

}
