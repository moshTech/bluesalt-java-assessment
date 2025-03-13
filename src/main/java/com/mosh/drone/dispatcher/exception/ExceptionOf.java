package com.mosh.drone.dispatcher.exception;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
public class ExceptionOf {

  public static final class Business {

    // 400xx
    public static final class BadRequest {

      public static final ExceptionCode BAD_REQUEST =
          new ExceptionCode(
              "400000",
              "The request could not be completed due to malformed syntax. Please crosscheck and try again.");
    }

    // 404xx
    public static final class NotFound {

      public static final ExceptionCode NOT_FOUND =
          new ExceptionCode("404000", "The requested resource was not found in the system");
    }
  }

  public static final class System {

    // 500xx
    public static final class InternalError {

      public static final ExceptionCode SERVER_ERROR =
          new ExceptionCode(
              "500000",
              "An unexpected error occurred while processing your request. Please try again later.");
    }
  }
}
