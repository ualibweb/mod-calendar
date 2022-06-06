package org.folio.calendar.integration.api.calendar.periods.servicepointid.period.periodid.get;

import static org.folio.calendar.testutils.DateTimeHandler.isCurrentInstant;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import io.restassured.response.Response;
import org.folio.calendar.domain.dto.ErrorCodeDTO;
import org.folio.calendar.domain.dto.ErrorDTO;
import org.folio.calendar.domain.dto.ErrorResponseDTO;
import org.folio.calendar.testconstants.UUIDs;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class GetSpecificNonExistentCalendarTest extends GetSpecificCalendarAbstractTest {

  @Test
  void testNonExistentCalendar() {
    Response response = sendGetRequest(UUIDs.UUID_0, UUIDs.UUID_2);
    response.then().statusCode(is(HttpStatus.NOT_FOUND.value()));

    ErrorResponseDTO errorResponse = response.getBody().as(ErrorResponseDTO.class);

    assertThat("Error timestamp is current", errorResponse.getTimestamp(), isCurrentInstant());
    assertThat(
      "Error HTTP code is correct",
      errorResponse.getStatus(),
      is(HttpStatus.NOT_FOUND.value())
    );
    assertThat("One error was returned", errorResponse.getErrors(), hasSize(1));

    ErrorDTO error = errorResponse.getErrors().get(0);

    assertThat(
      "Error reports that no calendar was found",
      error.getCode(),
      is(ErrorCodeDTO.NOT_FOUND)
    );
    assertThat(
      "Error message specified calendar not found error",
      error.getMessage(),
      containsString(String.format("No calendar was found with the specified query"))
    );
  }

  @Test
  void testCalendarOnWrongServicePoint() {
    Response response = sendGetRequest(UUIDs.UUID_0, UUIDs.UUID_D);
    response.then().statusCode(is(HttpStatus.NOT_FOUND.value()));

    ErrorResponseDTO errorResponse = response.getBody().as(ErrorResponseDTO.class);

    assertThat("Error timestamp is current", errorResponse.getTimestamp(), isCurrentInstant());
    assertThat(
      "Error HTTP code is correct",
      errorResponse.getStatus(),
      is(HttpStatus.NOT_FOUND.value())
    );
    assertThat("One error was returned", errorResponse.getErrors(), hasSize(1));

    ErrorDTO error = errorResponse.getErrors().get(0);

    assertThat(
      "Error reports that no calendar was found",
      error.getCode(),
      is(ErrorCodeDTO.NOT_FOUND)
    );
    assertThat(
      "Error message specified that the calendar does not correlate",
      error.getMessage(),
      containsString(
        String.format("The period requested does exist, however, is not assigned to service point ")
      )
    );
  }
}