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

/**
 * History Mode Filter
 * <br><br>
 * - 모든 라우트를 메인 라우트로 포워딩
 * - SPA (Single Page Application)를 위한 필터 [Ex. React, Angular, Vue]
 */
public class HistoryModeFilter extends OncePerRequestFilter {
    /**
     * forward할 url 패턴 정규식
     * <br><br>
     * - '/'로 시작하고 그 뒤에 점이 .이 없는 문자열과 일치
     * - '/api'로 시작하는 URL은 제외
     * - /home 또는 /user/profile과 일치, 하지만 /api/data, /api?query, /image.jpg, /script.js 와는 일치하지 않습니다.
     */
    private Pattern patt = Pattern.compile("^(?!/api)/([^.]*)");

    /**
     * forward할 endpoint
     */
    private String endpoint = "/";

    /**
     * url 패턴 Filter 처리
     * <br><br>
     * - forward할 url 패턴 정규식과 일치하면 endpoint로 forward
     * - 그 외에는 filterChain으로 넘김
     *
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     * @param filterChain FilterChain
     * @throws ServletException ServletException
     * @throws IOException      IOException
     */
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
