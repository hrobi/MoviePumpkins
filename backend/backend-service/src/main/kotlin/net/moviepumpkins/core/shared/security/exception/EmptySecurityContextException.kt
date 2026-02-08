package net.moviepumpkins.core.shared.security.exception

import org.springframework.security.core.AuthenticationException

class EmptySecurityContextException() : AuthenticationException("Security context was empty")