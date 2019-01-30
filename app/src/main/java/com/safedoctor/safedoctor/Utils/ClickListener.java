package com.safedoctor.safedoctor.Utils;

import android.view.View;

/**
 * Created by Kwabena Berko on 6/19/2017.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
