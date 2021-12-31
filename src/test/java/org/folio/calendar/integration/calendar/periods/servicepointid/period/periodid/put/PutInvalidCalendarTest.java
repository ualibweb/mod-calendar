package org.folio.calendar.integration.calendar.periods.servicepointid.period.periodid.put;

import static org.folio.calendar.testutils.DateTimeHandler.isCurrentInstant;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import io.restassured.response.Response;
import org.folio.calendar.domain.dto.Error;
import org.folio.calendar.domain.dto.ErrorCode;
import org.folio.calendar.domain.dto.ErrorResponse;
import org.folio.calendar.testconstants.Periods;
import org.folio.calendar.testconstants.UUIDs;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class PutInvalidCalendarTest extends PutCalendarAbstractTest {

  @Test
  void testPutInvalidCalendar() {
    Response response = sendPutRequest(
      Periods.PERIOD_FULL_EXAMPLE_A.withId(UUIDs.UUID_4).withName(""),
      UUIDs.UUID_1,
      UUIDs.UUID_E
    );

    response.then().statusCode(is(HttpStatus.UNPROCESSABLE_ENTITY.value()));

    ErrorResponse errorResponse = response.getBody().as(ErrorResponse.class);

    assertThat("Error timestamp is current", errorResponse.getTimestamp(), isCurrentInstant());
    assertThat(
      "Error HTTP code is correct",
      errorResponse.getStatus(),
      is(HttpStatus.UNPROCESSABLE_ENTITY.value())
    );
    assertThat("One error was returned", errorResponse.getErrors(), hasSize(1));

    Error error = errorResponse.getErrors().get(0);

    assertThat("Error reports that no name was provided", error.getCode(), is(ErrorCode.NO_NAME));
    assertThat(
      "Error message specified missing name error",
      error.getMessage(),
      containsString(String.format("The provided name (\"%s\") was empty", ""))
    );
  }
}