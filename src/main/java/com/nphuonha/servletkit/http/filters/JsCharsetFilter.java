package com.nphuonha.servletkit.http.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebFilter(urlPatterns = "*.js")
public class JsCharsetFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        if (response instanceof HttpServletResponse resp) {
            resp.setHeader("Content-Type", "application/javascript; charset=UTF-8");
        }
        chain.doFilter(request, response);
    }
}
