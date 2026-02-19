package at.spengergasse.foodoramin.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(ApplicationException.class)
  public ProblemDetail handleApplicationException(ApplicationException ex, HttpServletRequest request) {
    return toProblem(ex.getStatus(), ex.getCode(), ex.getMessage(), request.getRequestURI());
  }

  @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
  public ProblemDetail handleBadRequest(Exception ex, HttpServletRequest request) {
    String detail = ex.getMessage();

    if (ex instanceof MethodArgumentNotValidException manv) {
      detail =
          manv.getBindingResult().getFieldErrors().stream()
              .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
              .collect(Collectors.joining(", "));
    }

    return toProblem(HttpStatus.BAD_REQUEST, "BAD_REQUEST", detail, request.getRequestURI());
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleUnhandled(Exception ex, HttpServletRequest request) {
    return toProblem(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "INTERNAL_ERROR",
        "Unexpected error",
        request.getRequestURI());
  }

  private ProblemDetail toProblem(HttpStatus status, String code, String detail, String path) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(status);
    problemDetail.setTitle(status.getReasonPhrase());
    problemDetail.setDetail(detail);
    problemDetail.setProperty("code", code);
    problemDetail.setProperty("path", path);
    return problemDetail;
  }
}
