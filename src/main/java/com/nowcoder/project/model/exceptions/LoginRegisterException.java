package com.nowcoder.project.model.exceptions;

/**
 * 注册和登录时的异常 Created by nowcoder on 2018/08/07 下午3:12
 */
public class LoginRegisterException extends RuntimeException {

  public LoginRegisterException() {
    super();
  }

  public LoginRegisterException(String message) {
    super(message);
  }

  public LoginRegisterException(String message, Throwable cause) {
    super(message, cause);
  }

  public LoginRegisterException(Throwable cause) {
    super(cause);
  }
}
