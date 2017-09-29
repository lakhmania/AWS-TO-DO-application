/**
 * <Neha Lalwani>, <001268916>, <lalwani.n@husky.neu.edu>
 * <Nirali Merchant>, <001268909>, <merchant.n@husky.neu.edu>
 * <Chintan Koticha>, <001267049>, <koticha.c@husky.neu.edu>
 * <Apoorva Lakhmani>, <001256312>, <lakhmani.a@husky.neu.edu>
 */

package com.csye6225.demo;

import io.restassured.RestAssured;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class RestAssuredDemoApiTest {

  @Ignore
  @Test
  public void testGetHomePage() throws URISyntaxException {
    RestAssured.when().get(new URI("http://localhost:8080/")).then().statusCode(200);
  }

}
