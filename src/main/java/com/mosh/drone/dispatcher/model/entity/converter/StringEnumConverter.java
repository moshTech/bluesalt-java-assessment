package com.mosh.drone.dispatcher.model.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Converter
public abstract class StringEnumConverter<E extends Enum<E>>
    implements AttributeConverter<E, String> {

  private final Class<E> enumType;

  protected StringEnumConverter(Class<E> enumType) {
    this.enumType = enumType;
  }

  public String convertToDatabaseColumn(E attribute) {
    return attribute != null ? attribute.name() : null;
  }

  public E convertToEntityAttribute(String dbData) {
    return (E) (dbData != null ? Enum.valueOf(this.enumType, dbData) : null);
  }
}
