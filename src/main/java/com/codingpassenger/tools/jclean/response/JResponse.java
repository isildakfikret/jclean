package com.codingpassenger.tools.jclean.response;

import com.codingpassenger.tools.jclean.exception.JException;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;

/**
 * Represents general API response models.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class JResponse {
  private final JResponseStatus status;
  private final LocalDateTime time = LocalDateTime.now(ZoneOffset.UTC);
  private final Object data;
  private final Integer code;
  private final String message;

  /**
   * Creates a new instance of {@link JResponse} class.
   *
   * @param status  response result
   * @param data    response payload to be sent with successful or failure responses
   * @param code    error code to be sent with error response
   * @param message error message to be sent error response
   */
  private JResponse(final JResponseStatus status, final Object data, final Integer code, final String message) {
    this.status = status;
    this.data = data;
    this.code = code;
    this.message = message;
  }

  /**
   * Creates a successful response for pagination.
   *
   * @param page    current page number
   * @param limit   limit per page
   * @param total   total to be sent
   * @param content content to be sent
   * @param <E>     type of content items
   * @return Returns created {@link JResponse} instance.
   */
  public static <E> JResponse success(final int page, final int limit, final int total, final Collection<E> content) {
    final var pagePayload = new JPage<>(page, limit, total, content);
    return success(pagePayload);
  }

  /**
   * Creates a successful response.
   *
   * @param data payload to be sent
   * @return Returns created {@link JResponse} instance.
   */
  public static JResponse success(final Object data) {
    return new JResponse(JResponseStatus.SUCCESS, data, null, null);
  }

  /**
   * Creates a failure response.
   *
   * @param data payload to be sent
   * @return Returns created {@link JResponse} instance.
   */
  public static JResponse fail(final Object data) {
    return new JResponse(JResponseStatus.FAIL, data, null, null);
  }

  /**
   * Creates an error response.
   *
   * @param code    error code to be sent
   * @param message error message to be sent
   * @return Returns created {@link JResponse} instance.
   */
  public static JResponse error(final Integer code, final String message) {
    return new JResponse(JResponseStatus.ERROR, null, code, message);
  }

  /**
   * Creates an error response using {@link JException} instance.
   *
   * @param exception exception to be sent
   * @return Returns created {@link JResponse} instance.
   */
  public static JResponse error(final JException exception) {
    return new JResponse(JResponseStatus.ERROR, null, exception.getCode(), exception.getMessage());
  }

  /**
   * Gives response status.
   *
   * @return Returns response result.
   */
  public JResponseStatus getStatus() {
    return status;
  }

  /**
   * Gives the response time.
   *
   * @return Returns date-time as UTC.
   */
  public LocalDateTime getTime() {
    return time;
  }

  /**
   * Gives payload of the response.
   *
   * @return Returns payload.
   */
  public Object getData() {
    return data;
  }

  /**
   * Gives error code of the error response.
   *
   * @return Returns error code.
   */
  public Integer getCode() {
    return code;
  }

  /**
   * Gives error message ot the error response.
   *
   * @return Returns error message.
   */
  public String getMessage() {
    return message;
  }

  private record JPage<E>(int page, int limit, int total, Collection<E> content) {
  }
}
