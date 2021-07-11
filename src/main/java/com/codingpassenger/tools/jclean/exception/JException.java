package com.codingpassenger.tools.jclean.exception;

/**
 * Represents general model of well known errors.
 */
public class JException extends Exception {
  private final int code;

  /**
   * Creates a new instance of {@link JException} class.
   *
   * @param code    error code
   * @param message error message
   */
  public JException(final int code, final String message) {
    super(message);
    this.code = code;
  }

  /**
   * Gives error code.
   *
   * @return Returns error code.
   */
  public int getCode() {
    return code;
  }
}
