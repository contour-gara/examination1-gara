package org.contourgara.examination1.presentation;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.contourgara.examination1.application.FindAllEmployeesUseCase;
import org.contourgara.examination1.domain.model.Employee;
import org.contourgara.examination1.domain.model.EmployeeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest
class EmployeesControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockBean
  FindAllEmployeesUseCase findAllEmployeesUseCase;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mockMvc);
  }

  @Test
  void rootにアクセスできる() {
    // execute & assert
    given()
      .when()
      .get("/")
      .then()
      .status(OK)
      .body(blankOrNullString());
  }

  @Test
  void 全ての従業員情報が取得できる() {
    // setup
    doReturn(
        List.of(
            new Employee(new EmployeeId("1"), "Taro", "Yamada"),
            new Employee(new EmployeeId("2"), "Jiro", "Yamada")
        )
    ).when(findAllEmployeesUseCase).execute();

    // execute & assert
    given()
        .when()
        .get("/v1/employees")
        .then()
        .status(OK)
        .body("employees[0].id", equalTo("1"))
        .body("employees[0].firstName", equalTo("Taro"))
        .body("employees[0].lastName", equalTo("Yamada"))
        .body("employees[1].id", equalTo("2"))
        .body("employees[1].firstName", equalTo("Jiro"))
        .body("employees[1].lastName", equalTo("Yamada"));
  }

  @Nested
  class ID検索 {
    @Test
    void ID1の従業員が取得できる() {
      // execute & assert
      given()
          .when()
          .get("/v1/employees/1")
          .then()
          .status(OK)
          .body("id", equalTo("1"))
          .body("firstName", equalTo("Taro"))
          .body("lastName", equalTo("Yamada"));
    }

    @Test
    void ID2の従業員が取得できる() {
      // execute & assert
      given()
          .when()
          .get("/v1/employees/2")
          .then()
          .status(OK)
          .body("id", equalTo("2"))
          .body("firstName", equalTo("Jiro"))
          .body("lastName", equalTo("Yamada"));
    }

    @Test
    void 存在しないIDで検索した場合() {
      // execute & assert
      given()
          .when()
          .get("/v1/employees/0")
          .then()
          .status(BAD_REQUEST)
          .body("code", equalTo("0003"))
          .body("message", equalTo("specified employee [id = 0] is not found."))
          .body("details", hasSize(0));
    }
  }
}
