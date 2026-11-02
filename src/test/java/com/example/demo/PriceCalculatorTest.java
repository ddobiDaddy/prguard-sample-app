package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.demo.PriceCalculator.Grade;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PriceCalculatorTest {

  private final PriceCalculator calculator = new PriceCalculator();

  @Test
  void basicMemberPaysFullPrice() {
    assertEquals(
        new BigDecimal("100.00"), calculator.discountedPrice(new BigDecimal("100"), Grade.BASIC));
  }

  @Test
  void silverMemberGetsFivePercent() {
    assertEquals(
        new BigDecimal("95.00"), calculator.discountedPrice(new BigDecimal("100"), Grade.SILVER));
  }

  @Test
  void goldMemberGetsTenPercent() {
    assertEquals(
        new BigDecimal("90.00"), calculator.discountedPrice(new BigDecimal("100"), Grade.GOLD));
  }

  @Test
  void negativeAmountIsRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () -> calculator.discountedPrice(new BigDecimal("-1"), Grade.GOLD));
  }

  @Test
  void nullAmountIsRejected() {
    assertThrows(
        IllegalArgumentException.class, () -> calculator.discountedPrice(null, Grade.GOLD));
  }
}
