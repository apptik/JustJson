package org.djodjo.widget;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectSpinner extends Spinner implements
        OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> items;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;
    private ListAdapter listAdapter;

    public MultiSelectSpinner(Context context) {
        super(context);
    }

    public MultiSelectSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSelectSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (isChecked) {
            selected[which] = true;
        }
        else {
            selected[which] = false;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuffer spinnerBuffer = new StringBuffer();
        boolean someUnselected = false;
        for (int i = 0; i < items.size(); i++) {
            if (selected[i] == true) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
            } else {
                someUnselected = true;
            }
        }
        String spinnerText;
        if (someUnselected) {
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        } else {
            spinnerText = defaultText;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[] { spinnerText });
        setAdapter(adapter);
        if(listener!=null) {
            listener.onItemsSelected(selected);
        }
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setOnCancelListener(this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );
         if(listAdapter!=null) {
            builder.setAdapter(this.listAdapter, null);
            AlertDialog dialog = builder.create();
             //dialog.getListView().setAdapter(listAdapter);
            dialog.getListView().setItemsCanFocus(false);
            dialog.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            dialog.show();
             for(int i=0;i<listAdapter.getCount();i++) {
                 dialog.getListView().setItemChecked(i,selected[i]);
             }
             dialog.getListView().setOnItemClickListener(new OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     selected[position] = !selected[position];
                 }
             });
            return true;
        } else if(items!=null) {
             builder.setMultiChoiceItems(items.toArray(new CharSequence[items.size()]), selected, this).show();
             return true;

         }
        return false;
    }


    public void setItems(List<String> items, String allText,
                         MultiSpinnerListener listener) {
        this.items = items;
        this.defaultText = allText;
        this.listener = listener;

        // all selected by default
        selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++)
            selected[i] = true;

        // all text on the spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { allText });
        setAdapter(spinnerAdapter);
    }

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }


    public void setListAdapter(ListAdapter listAdapter, String allText, MultiSpinnerListener listener) {
        this.listener = listener;
        this.defaultText = allText;
        this.listAdapter = listAdapter;
        this.items = new ArrayList<String>();
        selected = new boolean[listAdapter.getCount()];
        for(int i=0;i<listAdapter.getCount();i++) {
            items.add(String.valueOf(listAdapter.getItem(i)));
            selected[i] = true;

        }

        // all text on the spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { allText });
        setAdapter(spinnerAdapter);
    }
}
