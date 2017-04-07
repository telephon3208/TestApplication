package com.masha.testapplication.ModelClasses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Credentials2 {

    @SerializedName("grant_type")
    @Expose
    private List<Object> grantType = null;
    @SerializedName("username")
    @Expose
    private List<Object> username = null;
    @SerializedName("password")
    @Expose
    private List<Object> password = null;

    public List<Object> getGrantType() {
        return grantType;
    }

    public void setGrantType(List<Object> grantType) {
        this.grantType = grantType;
    }

    public List<Object> getUsername() {
        return username;
    }

    public void setUsername(List<Object> username) {
        this.username = username;
    }

    public List<Object> getPassword() {
        return password;
    }

    public void setPassword(List<Object> password) {
        this.password = password;
    }

}


