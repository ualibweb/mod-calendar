package org.folio.calendar.testconstants;

import java.util.Arrays;
import lombok.experimental.UtilityClass;
import org.folio.calendar.domain.dto.OpeningDayInfo;

@UtilityClass
public class OpeningDayInfoRelativeConstants {

  public static final OpeningDayInfo CLOSED_ALL_DAY = OpeningDayInfo
    .builder()
    .open(false)
    .allDay(true)
    .exceptional(false)
    .build();
  public static final OpeningDayInfo OPEN_ALL_DAY = OpeningDayInfo
    .builder()
    .open(true)
    .allDay(true)
    .exceptional(false)
    .build();
  public static final OpeningDayInfo OPEN_00_00_TO_12_30_AND_23_00_TO_23_59 = OpeningDayInfo
    .builder()
    .open(true)
    .allDay(false)
    .openingHour(
      Arrays.asList(OpeningHourRanges.RANGE_00_00_TO_12_30, OpeningHourRanges.RANGE_23_00_TO_23_59)
    )
    .exceptional(false)
    .build();
  public static final OpeningDayInfo OPEN_00_00_TO_12_30 = OpeningDayInfo
    .builder()
    .open(true)
    .allDay(false)
    .openingHour(Arrays.asList(OpeningHourRanges.RANGE_00_00_TO_12_30))
    .exceptional(false)
    .build();
  public static final OpeningDayInfo OPEN_04_00_TO_14_59 = OpeningDayInfo
    .builder()
    .open(true)
    .allDay(false)
    .openingHour(Arrays.asList(OpeningHourRanges.RANGE_04_00_TO_14_59))
    .exceptional(false)
    .build();
  public static final OpeningDayInfo OPEN_15_00_TO_23_59 = OpeningDayInfo
    .builder()
    .open(true)
    .allDay(false)
    .openingHour(Arrays.asList(OpeningHourRanges.RANGE_15_00_TO_23_59))
    .exceptional(false)
    .build();
  public static final OpeningDayInfo OPEN_23_00_TO_23_59 = OpeningDayInfo
    .builder()
    .open(true)
    .allDay(false)
    .openingHour(Arrays.asList(OpeningHourRanges.RANGE_23_00_TO_23_59))
    .exceptional(false)
    .build();

  public static final OpeningDayInfo OPEN_INVALID = OpeningDayInfo
    .builder()
    .open(true)
    .allDay(false)
    .openingHour(Arrays.asList(OpeningHourRanges.INVALID_RANGE))
    .exceptional(false)
    .build();
}
