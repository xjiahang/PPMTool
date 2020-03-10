package io.agileintelligence.ppmtool.security;

import io.agileintelligence.ppmtool.domain.User;
import io.agileintelligence.ppmtool.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static io.agileintelligence.ppmtool.security.SecurityConstants.AUTH_HEADER;
import static io.agileintelligence.ppmtool.security.SecurityConstants.TOKEN_PREFIX;

//lecture 86
// validate token in the request header to make user can access to resource
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //lecture86
    @Autowired
    private JwtTokenProvider tokenProvider;
    //lecture86
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    //lecture 86
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getJWTFromRequest(httpServletRequest);
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
                Long id = tokenProvider.getUserIdFromToken(token);
                User user = customUserDetailsService.loadUserById(id);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user, null, Collections.emptyList()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    //lecture86
    private String getJWTFromRequest(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(AUTH_HEADER);
        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length(), token.length());
        }

        return null;
    }
}
