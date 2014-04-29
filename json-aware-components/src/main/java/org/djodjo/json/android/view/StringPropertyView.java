package org.djodjo.json.android.view;


import android.content.Context;

import org.djodjo.json.android.R;

public class StringPropertyView extends BasePropertyView{

    public StringPropertyView(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_string;
    }
}
