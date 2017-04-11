package com.masha.testapplication.ModelClasses;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoLinkResponse {

    @SerializedName("errors")
    @Expose
    private List<Object> errors = null;
    @SerializedName("data")
    @Expose
    private LinkData data;

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }

    public LinkData getData() {
        return data;
    }

    public void setData(LinkData data) {
        this.data = data;
    }


}


