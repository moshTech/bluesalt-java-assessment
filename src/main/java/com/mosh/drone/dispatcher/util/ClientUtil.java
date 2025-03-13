package com.mosh.drone.dispatcher.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

@UtilityClass
@Slf4j
public class ClientUtil {

  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  static {
    OBJECT_MAPPER
        .registerModules(new JavaTimeModule())
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .writerWithDefaultPrettyPrinter();
  }

  public static String cleanForLog(String rawInput) {
    if (StringUtils.isBlank(rawInput)) {
      return "";
    } else {
      String cleaned = StringEscapeUtils.escapeHtml4(cleanString(rawInput));
      if (!rawInput.equals(cleaned)) {
        cleaned = cleaned + " (Encoded)";
      }

      return cleaned;
    }
  }

  private static String cleanString(String rawInput) {
    return rawInput.replace("\n", "_").replace("\r", "_");
  }
}
