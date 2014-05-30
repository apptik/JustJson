package org.djodjo.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class FilterableListView extends LinearLayout {

    private ListView listView;
    private EditText searchBox;


    public FilterableListView(Context context) {
        this(context, null);
    }

    public FilterableListView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.FilterableListViewStyle);
    }

    public FilterableListView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    public FilterableListView(Context context, AttributeSet attrs, int defStyle, int styleRes) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiSlider, defStyle, styleRes);

        String titleText = a.getString(R.styleable.FilterableListView_hint);

        a.recycle();


        searchBox = (EditText)findViewById(R.id.searchBox);
        listView = (ListView)findViewById(R.id.list);

    }
}
