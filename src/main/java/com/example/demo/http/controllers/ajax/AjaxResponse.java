package com.example.demo.http.controllers.ajax;


import com.example.demo.core.utils.gson.GsonUtils;
import com.google.gson.Gson;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

@Data
@Accessors(chain=true)
public class AjaxResponse implements Serializable {
    private int code;
    private String message;
    private boolean success;
    private Object data;
    private Map<String, Object> extras;

    public String serialize() {
        Gson gson = GsonUtils.getGson();
        return gson.toJson(this);
    }
}
