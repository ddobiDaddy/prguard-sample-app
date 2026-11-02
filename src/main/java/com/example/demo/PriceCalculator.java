package com.example.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** 주문 금액에 회원 등급 할인을 적용하는 예제 클래스 (모든 점검을 통과하는 "정상" 코드). */
public final class PriceCalculator {

  private static final BigDecimal SILVER_RATE = new BigDecimal("0.05");
  private static final BigDecimal GOLD_RATE = new BigDecimal("0.10");

  /** 회원 등급. */
  public enum Grade {
    BASIC,
    SILVER,
    GOLD
  }

  /**
   * 할인 후 금액을 계산한다.
   *
   * @param amount 주문 금액 (0 이상)
   * @param grade 회원 등급
   * @return 소수점 둘째 자리에서 반올림한 할인 후 금액
   */
  public BigDecimal discountedPrice(BigDecimal amount, Grade grade) {
    if (amount == null || amount.signum() < 0) {
      throw new IllegalArgumentException("amount must be zero or positive");
    }
    BigDecimal rate = rateOf(grade);
    return amount.subtract(amount.multiply(rate)).setScale(2, RoundingMode.HALF_UP);
  }

  private static BigDecimal rateOf(Grade grade) {
    switch (grade) {
      case GOLD:
        return GOLD_RATE;
      case SILVER:
        return SILVER_RATE;
      default:
        return BigDecimal.ZERO;
    }
  }
}
