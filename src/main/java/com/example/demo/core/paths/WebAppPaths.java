package com.example.demo.paths;

/**
 * Utility class to get the paths of the web application
 * This class is used to get the paths of the web application
 * and return the path by the given file name
 */
public class WebAppPaths {
    // Resource Paths
    private static final String STATIC_PATH = "/static/";
    public static final String CSS_PATH = STATIC_PATH + "css/";
    public static final String JS_PATH = STATIC_PATH + "js/";
    public static final String IMAGES_PATH = STATIC_PATH + "images/";

    // View Paths
    public static final String VIEWS_PATH = "/views/";
    public static final String LAYOUTS_PATH = VIEWS_PATH + "layouts/";
    public static final String PAGES_PATH = VIEWS_PATH + "pages/";


    /**
     * Get full CSS path for a given file name. E.g., "style.css" equals to "/static/css/style.css"
     * @param fileName the CSS file name
     * @return the full path to the CSS file
     */
    public static String getCssPath(String fileName) {
        return CSS_PATH + fileName;
    }

    /**
     * Get full JS path for a given file name. E.g., "app.js" equals to "/static/js/app.js"
     * @param fileName the JS file name
     * @return the full path to the JS file
     */
    public static String getJsPath(String fileName) {
        return JS_PATH + fileName;
    }

    /**
     * Get full Image path for a given file name. E.g., "logo.png" equals to "/static/images/logo.png"
     * @param fileName the Image file name
     * @return the full path to the Image file
     */
    public static String getImagePath(String fileName) {
        return IMAGES_PATH + fileName;
    }

    /**
     * Get full Page view path for a given page name. E.g., "home/index.jsp" equals to "/views/pages/home/index.jsp"
     * @param pageName the page name
     * @return the full path to the page view
     */
    public static String getPageViewsPath(String pageName) {
        return PAGES_PATH + pageName + ".jsp";
    }

    /**
     * Get full Layout view path for a given layout name. E.g., "main.jsp" equals to "/views/layouts/main.jsp"
     * @param layoutName the layout name
     * @return the full path to the layout view
     */
    public static String getLayoutsPath(String layoutName) {
        return LAYOUTS_PATH + layoutName + ".jsp";
    }

}
