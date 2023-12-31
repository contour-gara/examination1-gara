package org.contourgara.examination1.application.param;

import static org.assertj.core.api.Assertions.*;

import org.contourgara.examination1.domain.model.Employee;
import org.contourgara.examination1.domain.model.EmployeeId;
import org.junit.jupiter.api.Test;

class CreateEmployeeParamTest {
  @Test
  void モデルに変換できる() {
    // setup
    CreateEmployeeParam sut = new CreateEmployeeParam("Hanako", "Shirato");

    // execute
    Employee actual = sut.convertToModel(3L);

    // assert
    Employee expected = new Employee(new EmployeeId("3"), "Hanako", "Shirato");

    assertThat(actual).isEqualTo(expected);
  }
}
