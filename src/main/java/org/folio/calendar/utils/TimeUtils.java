package org.folio.calendar.utils;

import java.time.LocalTime;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.UtilityClass;
import org.folio.calendar.domain.entity.NormalOpening;

/**
 * Utilities for times
 */
@UtilityClass
public class TimeUtils {

  /**
   * Check that two inclusive local time pairs overlap (within the same day)
   *
   * @param start1 Start of time range 1
   * @param end1 End of time range 1
   * @param start2 Start of time range 2
   * @param end2 End of time range 2
   * @return if they overlap
   */
  public static boolean overlaps(
    LocalTime start1,
    LocalTime end1,
    LocalTime start2,
    LocalTime end2
  ) {
    // False if: 2 starts after 1 OR 2 ends before 1 starts
    return !(start2.isAfter(end1) || end2.isBefore(start1));
  }

  /**
   * Check that two inclusive time ranges overlap (within the same day)
   *
   * @param a time range 1
   * @param b time range 2
   * @return if they overlap
   */
  public static boolean overlaps(TimeRange a, TimeRange b) {
    return overlaps(a.getStart(), a.getEnd(), b.getStart(), b.getEnd());
  }

  /**
   * Convert a LocalTime to a string of the format HH:mm
   *
   * @param time the {@link java.time.LocalTime LocalTime} to convert
   * @return the formatted String (HH:mm)
   */
  public static String toTimeString(LocalTime time) {
    return time.format(TimeConstants.TIME_FORMATTER);
  }

  /**
   * Convert a string of the format HH:mm to a LocalTime
   *
   * @param time the formatted String (HH:mm)
   * @return a parsed {@link java.time.LocalTime LocalTime}
   */
  public static LocalTime fromTimeString(String time) {
    return LocalTime.parse(time, TimeConstants.TIME_FORMATTER);
  }

  /**
   * Helper class used to facilitate set overlap search algorithm
   */
  @Data
  @AllArgsConstructor
  private class LocalTimeFromRange implements Comparable<LocalTimeFromRange> {

    private LocalTime time;
    private TimeRange rangeSource;

    private boolean isStart;

    public int compareTo(LocalTimeFromRange other) {
      if (this.getTime().compareTo(other.getTime()) != 0) {
        return this.getTime().compareTo(other.getTime());
      }

      // prioritize starting over ending ranges
      if (this.isStart()) {
        return -1;
      } else {
        return 1;
      }
    }
  }

  /**
   * Find overlaps within a set of time ranges, if any exist
   * @param ranges a set of ranges to evaluate
   * @return
   */
  public static Optional<List<NormalOpening>> getOverlaps(Iterable<TimeRange> ranges) {
    PriorityQueue<LocalTimeFromRange> queue = new PriorityQueue<>();

    // create a sorted queue of each time, with the first times at the beginning
    for (TimeRange range : ranges) {
      queue.add(new LocalTimeFromRange(range.getStart(), range, true));
      queue.add(new LocalTimeFromRange(range.getEnd(), range, false));
    }

    // track the ranges we are currently inside of
    Deque<TimeRange> stack = new LinkedList<>();

    for (LocalTimeFromRange time : queue) {
      if (time.isStart()) {
        // for new ranges, we just add them to the stack
        stack.push(time.getRangeSource());
      } else {
        // a range ended, but we hit an overlap
        if (stack.size() > 1) {
          return Optional.of(stack.stream().map(TimeRange::getSource).collect(Collectors.toList()));
        }
        stack.pop();
      }
    }

    return Optional.empty();
  }
}
