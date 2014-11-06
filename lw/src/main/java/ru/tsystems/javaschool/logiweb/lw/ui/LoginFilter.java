package ru.tsystems.javaschool.logiweb.lw.ui;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class LoginFilter implements Filter {
    private FilterConfig filterConfig;

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (filterConfig == null) {
            return;
        }
        ServletContext ctx = filterConfig.getServletContext();
        String logged = (String) ((HttpServletRequest) servletRequest).getSession().getAttribute("isLogged");
        if (!"true".equals(logged)) {
            RequestDispatcher dispatcher = ctx.getRequestDispatcher("/errorLogin.jsp");
            dispatcher.forward(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }


    @Override
    public void destroy() {

    }
}
