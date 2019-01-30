package com.safedoctor.safedoctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kwabena on 8/17/17.
 */

public class DataModel<T> {
    @SerializedName("content")
    @Expose
    private T content = null;

    public T getContent() {
        return content;
    }
}
