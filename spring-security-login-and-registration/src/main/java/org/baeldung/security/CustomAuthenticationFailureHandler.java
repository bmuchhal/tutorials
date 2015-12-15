package org.baeldung.security;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private MessageSource messages;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception) throws IOException, ServletException {
        setDefaultFailureUrl("/login.html?error=true");

        super.onAuthenticationFailure(request, response, exception);

        final Locale locale = localeResolver.resolveLocale(request);

        if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
            request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", messages.getMessage("auth.message.disabled", null, locale));
        } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
            request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", messages.getMessage("auth.message.expired", null, locale));
        } else if (exception.getMessage().equalsIgnoreCase("blocked")) {
            request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", messages.getMessage("auth.message.blocked", null, locale));
        } else {
            request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", messages.getMessage("message.badCredentials", null, locale));
        }
    }
}