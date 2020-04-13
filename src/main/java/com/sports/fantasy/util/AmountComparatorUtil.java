package com.sports.fantasy.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.util.StringUtils;
import com.sports.fantasy.model.AmountEntries;

public class AmountComparatorUtil {
  
  public static void amountComparator(List<AmountEntries> amountEntries) {
    Collections.sort(amountEntries, new Comparator<AmountEntries>() {
        @Override
        public int compare(AmountEntries o1, AmountEntries o2) {
            if (StringUtils.hasText(o1.getAmount()) && StringUtils.hasText(o2.getAmount())) {
                Float amount1 = Float.parseFloat(o1.getAmount());
                Float amount2 = Float.parseFloat(o2.getAmount());
                return amount1.compareTo(amount2);
            }
            return 0;
        }
    });
}

}
