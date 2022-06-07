package org.folio.calendar.unit.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.temporal.ChronoField;
import org.folio.calendar.domain.dto.SingleDayOpeningRangeDTO;
import org.folio.calendar.testconstants.Times;
import org.folio.calendar.utils.TemporalRange;
import org.folio.calendar.utils.TimeUtils;
import org.junit.jupiter.api.Test;

class TimeUtilsAllDayTest {

  @Test
  void testBaseFunction() {
    assertThat(TimeUtils.isAllDay(Times.TIME_00_00, Times.TIME_23_59), is(true));
    assertThat(TimeUtils.isAllDay(Times.TIME_04_00, Times.TIME_23_59), is(false));
    assertThat(TimeUtils.isAllDay(Times.TIME_00_00, Times.TIME_23_00), is(false));
    assertThat(TimeUtils.isAllDay(Times.TIME_04_00, Times.TIME_23_00), is(false));
  }

  @Test
  void testTruncation() {
    assertThat(
      TimeUtils.isAllDay(Times.TIME_00_00.with(ChronoField.SECOND_OF_MINUTE, 30), Times.TIME_23_59),
      is(true)
    );
  }

  @Test
  void testOverloads() {
    assertThat(
      TimeUtils.isAllDay(new TemporalRange<>(Times.TIME_00_00, Times.TIME_23_59)),
      is(true)
    );
    assertThat(
      TimeUtils.isAllDay(new TemporalRange<>(Times.TIME_04_00, Times.TIME_23_59)),
      is(false)
    );
    assertThat(
      TimeUtils.isAllDay(new TemporalRange<>(Times.TIME_00_00, Times.TIME_23_00)),
      is(false)
    );
    assertThat(
      TimeUtils.isAllDay(new TemporalRange<>(Times.TIME_04_00, Times.TIME_23_00)),
      is(false)
    );

    assertThat(
      TimeUtils.isAllDay(
        SingleDayOpeningRangeDTO
          .builder()
          .startTime(Times.TIME_00_00)
          .endTime(Times.TIME_23_59)
          .build()
      ),
      is(true)
    );
    assertThat(
      TimeUtils.isAllDay(
        SingleDayOpeningRangeDTO
          .builder()
          .startTime(Times.TIME_04_00)
          .endTime(Times.TIME_23_59)
          .build()
      ),
      is(false)
    );
    assertThat(
      TimeUtils.isAllDay(
        SingleDayOpeningRangeDTO
          .builder()
          .startTime(Times.TIME_00_00)
          .endTime(Times.TIME_23_00)
          .build()
      ),
      is(false)
    );
    assertThat(
      TimeUtils.isAllDay(
        SingleDayOpeningRangeDTO
          .builder()
          .startTime(Times.TIME_04_00)
          .endTime(Times.TIME_23_00)
          .build()
      ),
      is(false)
    );
  }
}
