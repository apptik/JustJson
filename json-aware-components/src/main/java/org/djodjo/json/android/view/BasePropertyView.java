package org.djodjo.json.android.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.djodjo.json.android.R;
import org.djodjo.json.schema.Schema;

import java.util.HashMap;

public abstract class BasePropertyView extends LinearLayout{


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

    public <T extends BasePropertyView> T setSchema(Schema schema) {
        return (T)this;
    }

    private void init() {
        layoutId = 0;
        title = "Ttile";
    }

    public <T extends BasePropertyView> T prepare() {
        if (layoutId == 0) layoutId = getLayoutId();
        inflate(getContext(), layoutId, this);
        txtTitle =  (TextView)findViewById(R.id.txtPropTitle);
        txtTitle.setText(title);
        return (T)this;
    }

    protected abstract int getLayoutId();
}
