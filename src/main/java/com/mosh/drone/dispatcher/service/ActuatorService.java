package com.mosh.drone.dispatcher.service;

import com.mosh.drone.dispatcher.model.response.HealthStatusResponse;
import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ActuatorService {

  private final Environment env;

  private final JdbcTemplate jdbcTemplate;

  private final RedisConnectionFactory redisConnectionFactory;

  public HealthStatusResponse getHealth() {
    var activeProfiles = env.getActiveProfiles();

    return HealthStatusResponse.builder()
        .status("OK")
        .db(isDatabaseUp() ? "UP" : "DOWN")
        .datetime(new Date())
        .profile(activeProfiles.length > 0 ? activeProfiles[0] : "default")
        .timezone(getTimezone())
        .startDate(getApplicationStartDatetime())
        .upDuration(getUpDuration())
        .redis(isRedisUp() ? "UP" : "DOWN")
        .build();
  }

  private boolean isDatabaseUp() {
    try {
      jdbcTemplate.queryForObject("SELECT 1", Integer.class);
      return true;
    } catch (Exception e) {
      log.error("Failed to run query on database", e);
      return false;
    }
  }

  private String getTimezone() {
    var tz = TimeZone.getDefault();
    var cal = Calendar.getInstance(tz);
    var offsetInMillis = tz.getOffset(cal.getTimeInMillis());

    var offset =
        String.format(
            "%02d:%02d",
            Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
    offset = (offsetInMillis >= 0 ? "+" : "-") + offset;

    return env.getProperty("user.timezone") + " - " + tz.getID() + " (" + offset + ")";
  }

  private Date getApplicationStartDatetime() {
    var startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
    return new Date(startTime);
  }

  private String getUpDuration() {
    var duration =
        Duration.between(getApplicationStartDatetime().toInstant(), new Date().toInstant());
    var durationWithoutMillis = duration.minusMillis(duration.toMillisPart());
    return durationWithoutMillis
        .toString()
        .substring(2)
        .replaceAll("(\\d[HMS])(?!$)", "$1 ")
        .toLowerCase();
  }

  private boolean isRedisUp() {
    try (var connection = redisConnectionFactory.getConnection()) {
      return "PONG".equals(connection.ping());
    } catch (Exception e) {
      return false;
    }
  }
}
