package org.hyunggi.mygardenbe.common.jwt;

public interface JwtService {
    String getToken(String key, Object value);
}
