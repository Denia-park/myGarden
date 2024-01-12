package org.hyunggi.mygardenbe.common.view.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Pattern;

//Forwards all routes to the main route
//This is for SPA (Single Page Application) [Ex. React, Angular, Vue]
public class HistoryModeFilter extends OncePerRequestFilter {
    // '/'로 시작하고 그 뒤에 점이 .이 없는 문자열과 일치
    // '/api'로 시작하는 URL은 제외
    // /home 또는 /user/profile과 일치, 하지만 /api/data, /api?query, /image.jpg, /script.js 와는 일치하지 않습니다.
    private Pattern patt = Pattern.compile("^(?!/api)/([^.]*)");
    // Main route
    private String endpoint = "/";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        if (this.patt.matcher(request.getRequestURI()).matches()) {
            RequestDispatcher rd = request.getRequestDispatcher(endpoint);
            rd.forward(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
