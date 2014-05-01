package org.djodjo.json.android.view;


import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.djodjo.json.android.R;

import java.util.HashMap;

public abstract class BasePropertyView extends LinearLayout{
    private final String LOG_TAG = "JJSON Inflater:BasePropertyView";


    TextView txtTitle;
    TextView txtDesc;

    protected String label;
    protected String title;
    protected String description;

    protected int layoutId;

    protected int displayType;
    protected HashMap<String,Integer> displayTypes;
    protected int themeColor;
    protected int buttonSelector;
    protected HashMap<String,Integer> customButtonSelectors;
    protected int styleTitle;
    protected int styleDesc;
    protected int styleValue;
    protected boolean noTitle;
    protected boolean noDesc;

    public BasePropertyView(Context context) {
        super(context);
        init();
    }

    public BasePropertyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BasePropertyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public <T extends BasePropertyView> T setTitle(String title) {
        this.title = title;
        return (T)this;
    }

    private void init() {
        layoutId = 0;
        title = "";
    }

    public <T extends BasePropertyView> T prepare() {
        if (layoutId == 0) layoutId = getLayoutId();
        inflate(getContext(), layoutId, this);
        txtTitle =  (TextView)findViewById(R.id.txtPropTitle);
        if(txtTitle!=null) {
            if (noTitle || title==null) {
                txtTitle.setVisibility(View.GONE);
            } else {
                txtTitle.setText(title);
                txtTitle.setTextAppearance(getContext(), styleTitle);
            }
        }
        return (T)this;
    }

    protected abstract int getLayoutId();


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        Log.d(LOG_TAG,"onRestoreInstanceState");
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d(LOG_TAG,"onSaveInstanceState");
        return super.onSaveInstanceState();

    }
}
