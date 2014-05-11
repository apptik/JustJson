package org.djodjo.json.android.view;


import android.content.Context;

import org.djodjo.json.android.FragmentLayouts;
import org.djodjo.json.android.R;

public class BooleanPropertyView extends BasePropertyView{

    public final static int LAYOUT_BOOL_CHECKED_TEXTVIEW = R.layout.fragment_bool_checkedtextview;
    public final static int LAYOUT_BOOL_CHECKBOX = R.layout.fragment_bool_check;
    public final static int LAYOUT_BOOL_SWITCH = R.layout.fragment_bool_switch;
    public final static int LAYOUT_BOOL_TOGGLE = R.layout.fragment_bool_toggle;

    public BooleanPropertyView(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
//        switch (displayType) {
//            case FragmentLayouts.DISPLAY_TYPE_CHECKBOX: return LAYOUT_BOOL_CHECKBOX;
//            case FragmentLayouts.DISPLAY_TYPE_CHECKED_TEXTVIEW: return LAYOUT_BOOL_CHECKED_TEXTVIEW;
//            case FragmentLayouts.DISPLAY_TYPE_SWITCH: return LAYOUT_BOOL_SWITCH;
//            case FragmentLayouts.DISPLAY_TYPE_TOGGLE: return LAYOUT_BOOL_TOGGLE;
//        }

        return LAYOUT_BOOL_CHECKED_TEXTVIEW;
    }

    @Override
    public <T extends BasePropertyView> T inflate() {
        super.inflate();
//
//        final Checkable checkable = (Checkable) (v != null ? v.findViewById(R.id.prop_value) : null);
//
//        if(checkable instanceof CompoundButton && !(checkable instanceof ToggleButton)) {
//            ((CompoundButton)checkable).setText(title);
//        } else if(checkable instanceof CheckedTextView) {
//            ((CheckedTextView)checkable).setText(title);
//            ((CheckedTextView)checkable).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ((CheckedTextView)checkable).toggle();
//                }
//            });
//        }
//
//        if(buttonSelector!=0 && checkable instanceof CompoundButton) {
//            ((CompoundButton)checkable).setButtonDrawable(buttonSelector);
//        } else if(buttonSelector!=0 && checkable instanceof CheckedTextView) {
//            ((CheckedTextView)checkable).setCheckMarkDrawable(buttonSelector);
//        } else if (checkable instanceof CheckBox && customButtonSelectors!= null && customButtonSelectors.get(ARG_GLOBAL_CHECKBOX_SELECTOR) != 0)
//        {
//            ((CompoundButton)checkable).setButtonDrawable(customButtonSelectors.get(ARG_GLOBAL_CHECKBOX_SELECTOR));
//        } else if (checkable instanceof CheckedTextView && customButtonSelectors!= null && customButtonSelectors.get(ARG_GLOBAL_CHECKBOX_SELECTOR) != 0)
//        {
//            ((CheckedTextView)checkable).setCheckMarkDrawable(customButtonSelectors.get(ARG_GLOBAL_CHECKBOX_SELECTOR));
//        }
//        else if (checkable instanceof ToggleButton && customButtonSelectors!= null && customButtonSelectors.get(ARG_GLOBAL_TOGGLEBUTTON_SELECTOR) != 0)
//        {
//            ((CompoundButton)checkable).setButtonDrawable(customButtonSelectors.get(ARG_GLOBAL_TOGGLEBUTTON_SELECTOR));
//        }
//        else if (checkable instanceof Switch && customButtonSelectors!= null && customButtonSelectors.get(ARG_GLOBAL_SWITCHBUTTON_SELECTOR) != 0)
//        {
//            ((CompoundButton)checkable).setButtonDrawable(customButtonSelectors.get(ARG_GLOBAL_SWITCHBUTTON_SELECTOR));
//        }

        return (T)this;
    }
}
