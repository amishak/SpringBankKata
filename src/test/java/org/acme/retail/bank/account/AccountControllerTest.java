package org.acme.retail.bank.account;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import org.acme.retail.bank.account.model.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = AccountApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("accountAPITestProfile")
public class AccountControllerTest {

    @Before
    public void setup() {
        RestAssured.port=8080;
        RestAssured.baseURI="http://localhost";
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountFailAccountNull() {
        given()
                .when().contentType("application/json")
                .header("Authorization-X", "customer-auth-token=myToken")
                .body((Account)null)
                .post("/account/register")
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadAccountFailTokenNull() {
        given()
                .when().contentType("application/json")
                .header("Authorization-X", null)
                .body((Account)null)
                .post("/account/register")
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testCreateAccountFailMissToken() {
        given()
                .when().contentType("application/json")
                .header("Authorization-X", "customer-auth-token=")
                .body(new Account("any id"))
                .post("/account/register")
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testCreateAccountFailMissAccountId() {
        given()
                .when().contentType("application/json")
                .header("Authorization-X", "customer-auth-token=anyToken")
                .body(new Account(null))
                .post("/account/register")
                .then()
                .statusCode(SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void testCreateAccountOKReadOK() {
        String customerAuthToken = "random_string";
        String customerId = "customer_1";
        Account accountRegistrationRequest = new Account(customerId);
        given()
            .when().contentType("application/json")
                .header("Authorization-X", "customer-auth-token=" + customerAuthToken)
                .body(accountRegistrationRequest)
                .post("/account/register")
            .then()
                .statusCode(SC_OK)
                .body("accountId", is(customerId));

        given()
                .when().contentType("application/json")
                .header("Authorization-X", "customer-auth-token=" + customerAuthToken)
                .get("/account/1")
                .then()
                .statusCode(SC_OK)
                .body("accountId", is(customerId));
    }

    @Test
    public void testCreateAccountOKReadFailMissToken() {
        String customerId = "customer_1";
        given()
                .when().contentType("application/json")
                .header("Authorization-X", "customer-auth-token=anyToken")
                .body(new Account("customer_1"))
                .post("/account/register")
                .then()
                .statusCode(SC_OK)
                .body("accountId", is(customerId));

        given()
                .when().contentType("application/json")
                .header("Authorization-X", "customer-auth-token=")
                .get("/account/1")
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testCreateAccountOKReadNotFoundWrongId() {
        String customerAuthToken = "random_string";
        String customerId = "customer_1";
        Account accountRegistrationRequest = new Account(customerId);
        given()
                .when().contentType("application/json")
                .header("Authorization-X", "customer-auth-token=" + customerAuthToken)
                .body(accountRegistrationRequest)
                .post("/account/register")
                .then()
                .statusCode(SC_OK)
                .body("accountId", is(customerId));

        given()
                .when().contentType("application/json")
                .header("Authorization-X", "customer-auth-token=" + customerAuthToken)
                .get("/account/-1")
                .then()
                .statusCode(SC_NOT_FOUND);
    }
}
