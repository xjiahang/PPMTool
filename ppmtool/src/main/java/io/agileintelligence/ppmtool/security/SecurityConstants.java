package io.agileintelligence.ppmtool.security;

public class SecurityConstants {

    public static final String SIGN_UP_URLS = "/api/users/**";
    public static final String H2_URL = "h2-console/**";
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final String TOKEN_PREFIX = "Bearer "; // a space here
    public static final String AUTH_HEADER = "Authorization"; //difference with course definition
    public static final long EXPIRATION_TIME = 30_000; //30 seconds

}
