package org.contourgara.examination1.application;

import lombok.RequiredArgsConstructor;
import org.contourgara.examination1.application.param.CreateEmployeeParam;
import org.contourgara.examination1.domain.model.Employee;
import org.contourgara.examination1.domain.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CreateEmployeeUseCase は従業員の新規登録を行います。
 */
@RequiredArgsConstructor
@Service
public class CreateEmployeeUseCase {
  private final EmployeeRepository repository;

  /**
   * 従業員の新規登録を実行します。
   *
   * @param param 新規登録する従業員情報。
   * @return 登録された {@link Employee}。
   */
  @Transactional
  public Employee execute(CreateEmployeeParam param) {
    return repository.create(param.convertToModel(repository.getNextSequence()));
  }
}
