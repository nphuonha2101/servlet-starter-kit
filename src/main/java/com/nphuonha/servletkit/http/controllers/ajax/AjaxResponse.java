package com.nphuonha.servletkit.http.controllers.ajax;


import com.nphuonha.servletkit.core.utils.gson.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

@Data
@Accessors(chain=true)
public class AjaxResponse implements Serializable {
    @Expose
    private int code;
    @Expose
    private String message;
    @Expose
    private boolean success;
    @Expose
    private Object data;
    @Expose
    private Map<String, Object> extras;

    public String serialize() {
        Gson gson = GsonUtils.getGson();
        return gson.toJson(this);
    }
}
