package com.example.demo.http.pages;

import com.example.demo.core.paths.WebAppPaths;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class representing a web page with common properties and methods for rendering.
 * This class uses Lombok annotations to generate boilerplate code such as getters, setters,
 * and builder-style accessors.
 */
@Data
@Accessors(chain = true)
public class Page {
    protected String title; // The title of the web page. E.g., "Home", "About Us".
    protected String pageName; // The name of the page, used to locate the view file. E.g., "home/index", "about".
    protected String layout; // The layout template to be used for rendering the page. E.g., "app", "blank".
    protected Map<String, Object> data = new HashMap<>(); // A map to hold dynamic data to be passed to the view.
    protected String[] styleSheets = new String[]{}; // An array of stylesheet file names to be included in the page. E.g., "main.css", "theme.css".
    protected String[] scripts = new String[]{}; // An array of script file names to be included in the page. E.g., "app.js", "utils.js".

    public Page setScripts(String[] scripts) {
        String[] scriptPaths = Arrays.stream(scripts)
                .map(String::trim)
                .map(WebAppPaths::getJsPath)
                .toArray(String[]::new);
        this.data.put("scripts", scriptPaths);
        return this;
    }

    public Page setStyleSheets(String[] styleSheets) {
        String[] styleSheetPaths = Arrays.stream(styleSheets)
                .map(String::trim)
                .map(WebAppPaths::getCssPath)
                .toArray(String[]::new);
        this.data.put("styleSheets", styleSheetPaths);
        return this;
    }

    public void render(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("title", this.title);
        this.data.forEach(request::setAttribute);
        request.setAttribute("contents", WebAppPaths.getPageViewsPath(this.pageName));

        try {
            RequestDispatcher dispatcher = request.getRequestDispatcher(WebAppPaths.getLayoutsPath(this.layout));
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
