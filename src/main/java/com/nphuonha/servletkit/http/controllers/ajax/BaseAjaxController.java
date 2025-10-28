package com.nphuonha.servletkit.http.controllers.ajax;

import com.nphuonha.servletkit.core.utils.PropertiesUtils;
import com.nphuonha.servletkit.http.controllers.base.BaseController;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseAjaxController extends BaseController {

    private Map<String, String> getAjaxHeaders() {
        String allowOrigin = PropertiesUtils.getProperty("cors.allowOrigin");
        String allowMethods = PropertiesUtils.getProperty("cors.allowMethods");
        String allowHeaders = PropertiesUtils.getProperty("cors.allowHeaders");
        String allowCredentials = PropertiesUtils.getProperty("cors.allowCredentials");

        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", allowOrigin);
        headers.put("Access-Control-Allow-Methods", allowMethods);
        headers.put("Access-Control-Allow-Headers", allowHeaders);
        headers.put("Access-Control-Allow-Credentials", allowCredentials);

        return headers;
    }

    private void send(AjaxResponse ajaxResponse, HttpServletResponse response, Map<String, String> headers) {
        // Set CORS headers
        if (headers == null) {
            headers = getAjaxHeaders();
        }
        for (Map.Entry<String, String> header : headers.entrySet()) {
            response.setHeader(header.getKey(), header.getValue());
        }
        // Send the Ajax response
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(ajaxResponse.getCode());
        try {
            response.getWriter().write(ajaxResponse.serialize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a success response to the client
     * @param message the message to be sent to the client
     * @param code the code to be sent to the client
     * @param data the data to be sent to the client
     * @param extras (Optional) the extras to be sent to the client. E.g., { "total": 100, "page": 1, "limit": 10 }
     * @param response the response to be sent to the client 
     */
    public void success(String message, Integer code, Object data, Map<String, Object> extras, HttpServletResponse response) {
        if (code == null) {
            code = 200;
        }
        AjaxResponse respAjaxObject = new AjaxResponse().setSuccess(true).setMessage(message).setCode(code).setData(data).setExtras(extras);
        send(respAjaxObject, response, getAjaxHeaders());
    }


    /**
     * Send an error response to the client
     * @param message the message to be sent to the client
     * @param code the code to be sent to the client
     * @param data the data to be sent to the client
     * @param extras (Optional) the extras to be sent to the client. E.g., { "total": 100, "page": 1, "limit": 10 }
     * @param response the response to be sent to the client 
     */
    public void error(String message, Integer code, Object data, Map<String, Object> extras, HttpServletResponse response) {
        // Set the code to 500 if not provided
        if (code == null) {
            code = 500;
        }
        AjaxResponse respAjaxObject = new AjaxResponse().setSuccess(false).setMessage(message).setCode(code).setData(data).setExtras(extras);
        send(respAjaxObject, response, getAjaxHeaders());
    }
}
