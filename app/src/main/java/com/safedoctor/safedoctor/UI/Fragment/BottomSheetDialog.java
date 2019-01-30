package com.safedoctor.safedoctor.UI.Fragment;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.safedoctor.safedoctor.R;

/**
 * Created by Stevkkys on 9/18/2017.
 */

public class BottomSheetDialog<T> extends BottomSheetDialogFragment
{

    private BottomSheetBehavior mBehavior;
    private AppBarLayout app_bar_layout;
    private LinearLayout layoutprofile;

    private T obj;
    private int layout;

    public LinearLayout getLayoutprofile()
    {
        return layoutprofile;
    }

    public void setLayoutprofile(LinearLayout layoutprofile)
    {
        this.layoutprofile = layoutprofile;
    }

    public BottomSheetDialog()
    {

    }

    public void  setType(T type)
    {
        this.obj = type;

    }

    public void setLayout(int layout)
    {
        this.layout = layout;
    }

    public int getLayout()
    {
        return this.layout;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final android.support.design.widget.BottomSheetDialog dialog = (android.support.design.widget.BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), layout, null);

        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        app_bar_layout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
       // layoutprofile = (LinearLayout) view.findViewById(R.id.lyt_profile);

        // set data to view
        //Tools.displayImageRound(getActivity(), (ImageView) view.findViewById(R.id.image), people.image);
        //((TextView) view.findViewById(R.id.name)).setText(people.name);
        //((TextView) view.findViewById(R.id.name_toolbar)).setText(people.name);

        hideView(app_bar_layout);

        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    showView(app_bar_layout, getActionBarSize());
                    hideView(layoutprofile);
                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    hideView(app_bar_layout);
                    showView(layoutprofile, getActionBarSize());
                }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        ((ImageButton) view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);
    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

    private int getActionBarSize() {
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) styledAttributes.getDimension(0, 0);
        return size;
    }

}
