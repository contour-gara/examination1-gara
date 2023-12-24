package org.contourgara.examination1.presentation;

import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.extern.slf4j.Slf4j;
import org.contourgara.examination1.application.exception.NotFoundEmployeeException;
import org.contourgara.examination1.presentation.response.ErrorResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * GlobalExceptionHandler は例外を検知し適切なエラーレスポンスを返すクラスです。
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  /**
   * ID 検索した際に従業員が見つからない場合に、このメソッドが実行されます。
   *
   * @param e 従業員が見つからなかった時の例外。
   * @return ErrorResponse。コードは 0003 で、見つからなかった従業員 ID が含まれます。
   */
  @ExceptionHandler(NotFoundEmployeeException.class)
  @ResponseStatus(BAD_REQUEST)
  public ErrorResponse handleNotFoundEmployeeException(NotFoundEmployeeException e) {
    log.warn("指定された ID の従業員が見つかりません。[id = {}]", e.getId(), e);
    return new ErrorResponse(
        "0003",
        String.format("specified employee [id = %s] is not found.", e.getId()),
        emptyList()
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(BAD_REQUEST)
  public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    List<String> details = e.getFieldErrors().stream()
        .peek(error -> log.warn("入力エラーが発生しました。[{} = {}]", error.getField(), error.getRejectedValue()))
        .map(error -> String.format("%s %s", error.getField(), error.getDefaultMessage()))
        .peek(error -> log.warn("入力エラーの内容。[{}]", error))
        .toList();

    return new ErrorResponse(
        "0002",
        "request validation error is occurred.",
        details
    );
  }

  // TODO: 汎用的なハンドラーを作る
}
