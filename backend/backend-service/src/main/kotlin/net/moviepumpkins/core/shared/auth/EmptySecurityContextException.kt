package net.moviepumpkins.core.shared.auth

import org.springframework.security.core.AuthenticationException

class EmptySecurityContextException() : AuthenticationException("Security context was empty")