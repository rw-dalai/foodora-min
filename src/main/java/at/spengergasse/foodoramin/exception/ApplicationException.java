package at.spengergasse.foodoramin.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {

  private final HttpStatus status;
  private final String code;

  private ApplicationException(HttpStatus status, String code, String message) {
    super(message);
    this.status = status;
    this.code = code;
  }

  public static ApplicationException ofNotFound(String message) {
    return new ApplicationException(HttpStatus.NOT_FOUND, "NOT_FOUND", message);
  }

  public static ApplicationException ofConflict(String message) {
    return new ApplicationException(HttpStatus.CONFLICT, "CONFLICT", message);
  }

  public static ApplicationException ofForbidden(String message) {
    return new ApplicationException(HttpStatus.FORBIDDEN, "FORBIDDEN", message);
  }
}
