package com.sports.fantasy.util;

import java.util.Random;

public class AlphaNumericUtil {
  
  private static Random rand = new Random();

  private AlphaNumericUtil() {
    throw new IllegalStateException("Utility class");
  }


  public static String getAlphaNumericValue(int length) {
    String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int index = (int)(alphaNumeric.length() * Math.random());
      sb.append(alphaNumeric.charAt(index));
    }
    return sb.toString();
  }

  public static String getOrderNumber() {
    return "Order_" + rand.nextInt(1000000);
  }
}
