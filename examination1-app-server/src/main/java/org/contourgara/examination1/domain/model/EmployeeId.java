package org.contourgara.examination1.domain.model;

import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * EmployeeId クラスは従業員 ID を表すクラスです。
 *
 * @param value 従業員 ID。1 以上 999999999 以下の数字で、null であってはなりません。
 */
public record EmployeeId(String value) {
  /**
   * EmployeeId を初期化します。
   *
   * @param value 従業員 ID。1 以上 999999999 以下の数字で、null であってはなりません。
   */
  public EmployeeId {
    if (!isNumeric(value)) throw new IllegalArgumentException("従業員 ID は数字です。");
    if ((Integer.parseInt(value) <= 0) || (Integer.parseInt(value) >= 999999999)) {
      throw new IllegalArgumentException("従業員 ID は 1 以上 999999999 以下です。");
    }
  }
}