package com.bruce.security.exceptions;
import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {
  public InvalidTokenException(String token) {
    super("token is invalid, token = " + token);
  }
  public InvalidTokenException(String message, Throwable cause) {
    super(message, cause);
  }
}
