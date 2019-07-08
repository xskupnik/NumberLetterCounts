/*
If the numbers 1 to 5 are written out in words: one, two, three, four, five,
 then there are 3 + 3 + 5 + 4 + 4 = 19 letters used in total.

If all the numbers from 1 to 1000 (one thousand) inclusive were written out in words,
 how many letters would be used?


NOTE: Do not count spaces or hyphens. For example, 342 (three hundred and forty-two)
 contains 23 letters and 115 (one hundred and fifteen) contains 20 letters.
 The use of "and" when writing out numbers is in compliance with British usage.
*/

package com.dohnalovar;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {
            Scanner sc = new Scanner(System.in);
            FrequencyIntervalMap object = FrequencyIntervalMap.getInstance();

            do {
                System.out.print("Get lower bound (>=1) : ");
                int x = sc.nextInt();
                if (x < 1) throw new Exception("Lower bound must be >= 1");
                System.out.print("Get upper bound (<=1000): ");
                int y = sc.nextInt();
                if (y > 1000) throw new Exception("Upper bound must be <= 1000");

                System.out.println("Number letter counts in interval [ "
                    + x + " , "
                    + y + " ] is "
                    + object.numberLetterCounts(x, y));

                System.out.println("Press '0' for exit ");

            } while ( !sc.next().contains("0") );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
