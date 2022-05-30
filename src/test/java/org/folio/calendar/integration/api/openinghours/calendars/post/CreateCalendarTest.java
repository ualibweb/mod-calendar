package org.folio.calendar.integration.api.openinghours.calendars.post;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import io.restassured.response.Response;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.folio.calendar.domain.dto.CalendarDTO;
import org.folio.calendar.domain.dto.ErrorCodeDTO;
import org.folio.calendar.domain.dto.ErrorDTO;
import org.folio.calendar.domain.dto.ErrorResponseDTO;
import org.folio.calendar.domain.entity.Calendar;
import org.folio.calendar.domain.mapper.CalendarMapper;
import org.folio.calendar.integration.BaseApiAutoDatabaseTest;
import org.folio.calendar.integration.ValidationSchema;
import org.folio.calendar.testconstants.Calendars;
import org.folio.calendar.testconstants.UUIDs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class CreateCalendarTest extends BaseApiAutoDatabaseTest {

  public static final String CREATE_CALENDAR_API_ROUTE = "/opening-hours/calendars";

  @Autowired
  private CalendarMapper calendarMapper;

  /**
   * Send a Calendar creation request
   * @param calendar the calendar to create
   * @return the Response
   */
  protected Response sendCalendarCreationRequest(Calendar calendar) {
    return ra(ValidationSchema.OPENING_HOURS)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .body(calendarMapper.toDto(calendar))
      .post(getRequestUrl(CREATE_CALENDAR_API_ROUTE));
  }

  @Test
  void testUnassignedCalendarCreation() {
    Calendar calendarWithNoAssignments = Calendars.CALENDAR_COMBINED_EXAMPLE_A
      .withId(null)
      .withServicePoints(Set.of());
    Response response = sendCalendarCreationRequest(calendarWithNoAssignments);
    response.then().statusCode(is(HttpStatus.CREATED.value()));
    Calendar result = calendarMapper.fromDto(response.getBody().as(CalendarDTO.class));
    assertThat(
      "The newly created calendar is the same as the one provided",
      result.withId(calendarWithNoAssignments.getId()),
      is(equalTo(calendarWithNoAssignments))
    );
  }

  @Test
  void testCalendarCreation() {
    Response response = sendCalendarCreationRequest(Calendars.CALENDAR_COMBINED_EXAMPLE_A);
    response.then().statusCode(is(HttpStatus.CREATED.value()));
    Calendar result = calendarMapper.fromDto(response.getBody().as(CalendarDTO.class));
    assertThat(
      "The newly created calendar is the same as the one provided",
      result.withId(Calendars.CALENDAR_COMBINED_EXAMPLE_A.getId()),
      is(equalTo(Calendars.CALENDAR_COMBINED_EXAMPLE_A))
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCalendarDuplicateCreation() {
    Response response = sendCalendarCreationRequest(Calendars.CALENDAR_COMBINED_EXAMPLE_A);
    response.then().statusCode(is(HttpStatus.CREATED.value()));
    Calendar result = calendarMapper.fromDto(response.getBody().as(CalendarDTO.class));
    assertThat(
      "The newly created calendar is the same as the one provided",
      result.withId(Calendars.CALENDAR_COMBINED_EXAMPLE_A.getId()),
      is(equalTo(Calendars.CALENDAR_COMBINED_EXAMPLE_A))
    );

    response = sendCalendarCreationRequest(Calendars.CALENDAR_COMBINED_EXAMPLE_B);
    response.then().statusCode(is(HttpStatus.CONFLICT.value()));
    ErrorResponseDTO errorResponse = response.getBody().as(ErrorResponseDTO.class);
    assertThat(errorResponse.getStatus(), is(equalTo(HttpStatus.CONFLICT.value())));
    assertThat(errorResponse.getErrors(), hasSize(1));
    ErrorDTO error = errorResponse.getErrors().get(0);
    assertThat(error.getCode(), is(ErrorCodeDTO.CALENDAR_DATE_OVERLAP));
    assertThat(
      error.getMessage(),
      is(
        "This calendar overlaps with another calendar (“scam impending encode idly” from Jan 1, 2021 to Apr 30, 2021)"
      )
    );
    assertThat(
      ((LinkedHashMap<String, List<String>>) error.getData()).get("conflictingServicePointIds"),
      contains(UUIDs.UUID_2.toString())
    );
  }
}
