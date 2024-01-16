package org.hyunggi.mygardenbe.auth.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hyunggi.mygardenbe.common.exception.InvalidTokenRequestException;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (InvalidTokenRequestException e) {
            setErrorResponse(response, e.getMessage());
        }
    }

    private void setErrorResponse(HttpServletResponse response, final String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<Object> apiResponse = ApiResponse.of(HttpStatus.UNAUTHORIZED, message);

        try {
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
