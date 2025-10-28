package com.nphuonha.servletkit.http.controllers.web.home;

import com.nphuonha.servletkit.http.pages.Page;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "homeController", urlPatterns = {"/home", "/"})
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Page page = new Page().setTitle("Home Page").setPageName("home/index").setLayout("app");
        page.render(req, resp);
    }
}
