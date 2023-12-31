package org.contourgara.examination1.infrastructure.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.contourgara.examination1.infrastructure.entity.EmployeeEntity;

/**
 * EmployeeMapper は従業員テーブルにアクセスするためのマッパーです。
 */
@Mapper
public interface EmployeeMapper {
  /**
   * 従業員テーブルにあるデータを全件取得します。
   *
   * @return {@link EmployeeEntity} の List。
   */
  @Select("SELECT id, first_name, last_name FROM employees")
  List<EmployeeEntity> findAll();

  /**
   * 従業員テーブルにあるデータから ID で検索します。
   *
   * @param id 検索したい ID。
   * @return {@link EmployeeEntity}。従業員が見つからなかった場合は null が返ります。
   */
  @Select("SELECT id, first_name, last_name FROM employees WHERE id = #{id}")
  EmployeeEntity findById(String id);

  /**
   * 次のシーケンスを取得し、シーケンスを更新します。
   *
   * @return 次のシーケンス。
   */
  @Select("SELECT nextval('EMPLOYEE_ID_SEQ')")
  @Options(flushCache = Options.FlushCachePolicy.TRUE)
  Long getNextSequence();

  /**
   * 従業員を新規登録します。
   * 次のシーケンスを取得してから使用してください。
   *
   * @param entity 登録する従業員。従業員 ID は次のシーケンスの値を使用してください。
   * @return 作成件数。
   */
  @Insert(
      "INSERT INTO employees (id, first_name, last_name) VALUES (#{id}, #{firstName}, #{lastName})"
  )
  Integer create(EmployeeEntity entity);

  /**
   * 従業員を更新します。
   *
   * @param entity 更新する従業員。
   * @return 更新件数。
   */
  @Update(
      "UPDATE employees SET first_Name = #{firstName}, last_Name = #{lastName} WHERE id = #{id}"
  )
  Integer update(EmployeeEntity entity);

  /**
   * 従業員を削除します。
   *
   * @param id 削除する従業員 ID。
   * @return 削除件数。
   */
  @Delete("DELETE from employees WHERE id = #{id}")
  Integer delete(String id);
}
