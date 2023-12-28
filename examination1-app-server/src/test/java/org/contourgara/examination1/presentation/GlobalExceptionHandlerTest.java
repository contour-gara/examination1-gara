package org.contourgara.examination1.presentation;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.contourgara.examination1.TestUtils.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.contourgara.examination1.application.CreateEmployeeUseCase;
import org.contourgara.examination1.application.DeleteEmployeeUseCase;
import org.contourgara.examination1.application.FindAllEmployeesUseCase;
import org.contourgara.examination1.application.FindEmployeeByIdUseCase;
import org.contourgara.examination1.application.exception.NotFoundEmployeeException;
import org.contourgara.examination1.presentation.request.CreateEmployeeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class GlobalExceptionHandlerTest {
  @Autowired
  MockMvc mockMvc;

  @MockBean
  CreateEmployeeUseCase createEmployeeUseCase;

  @MockBean
  DeleteEmployeeUseCase deleteEmployeeUseCase;

  @MockBean
  FindAllEmployeesUseCase findAllEmployeesUseCase;

  @MockBean
  FindEmployeeByIdUseCase findEmployeeByIdUseCase;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mockMvc);
  }

  @Test
  void 存在しないIDで検索した場合() {
    // setup
    doThrow(new NotFoundEmployeeException("0"))
        .when(findEmployeeByIdUseCase)
        .execute("0");

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

  @Nested
  class 新規登録で入力検証が機能しているか {
    @Test
    void 入力されていない場合() {
      // execute & assert
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(marshalToJson(new CreateEmployeeRequest(null, null)))
          .when()
          .post("/v1/employees")
          .then()
          .status(BAD_REQUEST)
          .body("code", equalTo("0002"))
          .body("message", equalTo("request validation error is occurred."))
          .body("details", hasSize(2));
    }

    @Test
    void 空文字の場合() {
      // execute & assert
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(marshalToJson(new CreateEmployeeRequest("", "")))
          .when()
          .post("/v1/employees")
          .then()
          .status(BAD_REQUEST)
          .body("code", equalTo("0002"))
          .body("message", equalTo("request validation error is occurred."))
          .body("details", hasSize(4));
    }

    @Test
    void アルファベット以外がある場合() {
      // execute & assert
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(marshalToJson(new CreateEmployeeRequest("あ", "あ")))
          .when()
          .post("/v1/employees")
          .then()
          .status(BAD_REQUEST)
          .body("code", equalTo("0002"))
          .body("message", equalTo("request validation error is occurred."))
          .body("details", hasSize(2));
    }

    @Test
    void _100文字以上の場合() {
      // execute & assert
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(marshalToJson(new CreateEmployeeRequest("a".repeat(101), "a".repeat(101))))
          .when()
          .post("/v1/employees")
          .then()
          .status(BAD_REQUEST)
          .body("code", equalTo("0002"))
          .body("message", equalTo("request validation error is occurred."))
          .body("details", hasSize(2));
    }
  }

  @Nested
  class 新規登録の入力検証で正しいメッセージが返されているか {
    @Test
    void 入力されていない場合() {
      // execute & assert
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(marshalToJson(new CreateEmployeeRequest(null, "lastName")))
          .when()
          .post("/v1/employees")
          .then()
          .status(BAD_REQUEST)
          .body("code", equalTo("0002"))
          .body("message", equalTo("request validation error is occurred."))
          .body("details", hasSize(1))
          .body("details[0]", equalTo("firstName must not be blank"));
    }

    @Test
    void アルファベット以外がある場合() {
      // execute & assert
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(marshalToJson(new CreateEmployeeRequest("あ", "lastName")))
          .when()
          .post("/v1/employees")
          .then()
          .status(BAD_REQUEST)
          .body("code", equalTo("0002"))
          .body("message", equalTo("request validation error is occurred."))
          .body("details", hasSize(1))
          .body("details[0]", equalTo("firstName must match \"^[a-zA-Z]+$\""));
    }

    @Test
    void _100文字以上の場合() {
      // execute & assert
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(marshalToJson(new CreateEmployeeRequest("a".repeat(101), "lastName")))
          .when()
          .post("/v1/employees")
          .then()
          .status(BAD_REQUEST)
          .body("code", equalTo("0002"))
          .body("message", equalTo("request validation error is occurred."))
          .body("details", hasSize(1))
          .body("details[0]", equalTo("firstName length must be between 0 and 100"));
    }
  }

  @Test
  void 存在しない従業員を削除した場合() {
    // setup
    doThrow(new NotFoundEmployeeException("0"))
        .when(deleteEmployeeUseCase)
        .execute("0");

    // execute & assert
    given()
        .when()
        .delete("/v1/employees/0")
        .then()
        .status(BAD_REQUEST)
        .body("code", equalTo("0003"))
        .body("message", equalTo("specified employee [id = 0] is not found."))
        .body("details", hasSize(0));
  }
}
