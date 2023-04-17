package ru.javawebinar.topjava.web.filter;

import javax.servlet.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class AdditionalEncodingSpringSecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        servletRequest.setCharacterEncoding(StandardCharsets.UTF_8.name());
        servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        filterChain.doFilter(servletRequest, servletResponse);
    }


}
