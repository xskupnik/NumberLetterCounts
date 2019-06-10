package com.dohnalovar;

import java.util.function.ToIntBiFunction;

/**
 * Created by dohnalovar on 6/9/2019
 */
public class FrequencyIntervalMap {

    private static String[][] WORD = {
        // f0Unit, f0Hundred - included in units and hundreds
        {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"},
        // f1 - included in tenths starting with 1
        {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"},
        // f2 - included in tenth not starting with 0 or 1
        {"", "", "twenty", "thirty", "fourty", "fifty", "sixty", "seventy", "eighty", "ninety"},
        // f3 - included in hundreds
        {"hundred"},
        // f4 - included in hundreds, except 100, 200, ... 900
        {"and"},
        // f5 - included in 1000
        {/*one*/"thousand"} // "one thousand" or "thousand" is correct?
    };

    private static TriFunction<Integer, Integer, Integer, Integer> f0Unit;
    static {
        f0Unit = (index, x, y) -> {
            int i = x / 100;
            int j = y / 100;

            if (i < j) {
                // split to 2 intervals and count frequence in them
                return f0Unit.applyAsInt(index, x, i * 100 + 99) + f0Unit.applyAsInt(index, (i + 1) * 100, y);
            }

            int mi = x % 100;
            int mj = y % 100;

            int count = (mj-mi) / 10;
            int mjj = mj % 10;
            int mii = mi % 10;

            if (count == 0 && index >= mii && index <= mjj) count++; // the same tenth
            else if (index >= mii && index <= mjj) count++;

            int teen = i*100+10+index;
            if (x <= teen && y >= teen ) count--; //don't count indexes from the first tenth
            return count;
        };
    }

    private static TriFunction<Integer, Integer, Integer, Integer> f0Hundred = (index, x, y) -> {
        if ( x >= (index+1) * 100 ) return 0;
        if ( y <= index * 100 ) return 0;
        if ( x < index*100 ) x = index*100;
        if ( y > (index+1)*100-1) y = (index+1)*100-1;

        return y - x + 1;
    };

    private static TriFunction<Integer, Integer, Integer, Integer> f1;
    static {
        f1 = (index, x, y) -> {

            if (y == 1000) y = 999; // 1000 solve in 9th interval, -teen is not included there
            int i = x / 100;
            int j = y / 100;

            if ( i < j ) {
                // split to 2 intervals and count frequence in them
                return f1.applyAsInt(index, x, i * 100 + 99) + f1.applyAsInt(index,(i + 1) * 100, y);
            }

            int mi = x % 100;
            int mj = y % 100;

            mi = (mi <= 10+index) ? 1 : 0;
            mj = (mj >= 10+index) ? 1 : 0;

            return mi * mj; // only when 1 * 1, i. e.  mi < 10+index && mj > 10+index
        };
    }

    private static TriFunction<Integer, Integer, Integer, Integer> f2;
    static {
        f2 = (index, x, y) -> {

            if (y == 1000) y = 999; // 1000 solve in 9th interval, -ty is not included there
            int i = x / 100;
            int j = y / 100;

            if (i < j) {
                // split to 2 intervals and count frequence in them
                return f2.applyAsInt(index, x, i * 100 + 99) + f2.applyAsInt(index, (i + 1) * 100, y);
            }

            int border = index * 10;
            int mi = x % 100;
            if (mi > border + 9) return 0;
            if (mi < border) mi = border; // move to border

            int mj = y % 100;
            if (mj < border) return 0;
            if (mj > border + 9) mj = border + 9; // move to border + 9

            return mj - mi + 1;
        };
    }

    /** word from group WORD[3] "hundred" exists in interval [100, 999] with frequency 900 */
    private static ToIntBiFunction<Integer, Integer> f3 = (x, y) -> {
        if (y < 100) return 0;
        if (x > 999) return 0;
        if (x < 100) x = 100;
        if (y > 999) y = 999;
        return y - x + 1;
    };


    /** word from group WORD[4] "and" exists in intervals:
     *     for each n from [1, 9] in [n*100, n*100+99] with frequency 99
     *  e. g. 100 .. one hundred, 101 .. one hundred and one, ..., 199 .. one hundred and ninety nine
     */
    private static ToIntBiFunction<Integer, Integer> f4;
    static {
        f4 = (x, y) -> {
            int i = x / 100;
            int j = y / 100;

            if (i < j) {
                // split to 2 intervals and count frequence in them
                return f4.applyAsInt(x, i * 100 + 99) + f4.applyAsInt((i + 1) * 100, y);
            }
            if (i == 0) return 0; // no frequency in [1, 99]

            int mi = x % 100;
            int mj = y % 100;
            if (mi > 0) {
                return mj - mi + 1;
            } else {
                return mj - mi;
            }
        };
    }


    /** words from group WORD[5] "thousand" exists in interval [1000, 1000] with frequency 1 */
    private static ToIntBiFunction<Integer, Integer> f5 = (x, y) -> {
        if ( y < 1000 ) return 0;
        else return 1;
    };

    public int numberLetterCounts(int min, int max) {
        int sum = 0;
        int tmp = 0;
        for (int index = 1; index < WORD[0].length; index++) {
            tmp = f0Unit.applyAsInt(index, min, max); //"one", "two", ...
            System.out.println("Units ... frequency( " + WORD[0][index] + " ) = " + tmp);
            sum += tmp * WORD[0][index].length();
        }
        for (int index = 1; index < WORD[0].length; index++) {
            tmp = f0Hundred.applyAsInt(index, min, max); //"one", "two", ...
            System.out.println("Hundreds ... frequency( " + WORD[0][index] + " ) = " + tmp);
            sum += tmp * WORD[0][index].length();
        }
        for (int index = 0; index < WORD[1].length; index++) {
            tmp = f1.applyAsInt(index, min, max); // "ten", "eleven", ...
            System.out.println("frequency( " + WORD[1][index] + " ) = " + tmp);
            sum += tmp * WORD[1][index].length();
        }
        for (int index = 2; index < WORD[2].length; index++) {
            tmp = f2.applyAsInt(index, min, max); // "twenty", "thirty", ...
            System.out.println("frequency( " + WORD[2][index] + " ) = " + tmp);
            sum += tmp * WORD[2][index].length();
        }
        tmp = f3.applyAsInt(min, max); //"hundred"
        System.out.println("frequency( " + WORD[3][0] + " ) = " + tmp);
        sum += tmp * WORD[3][0].length();

        tmp = f4.applyAsInt(min, max); //"and"
        System.out.println("frequency( " + WORD[4][0] + " ) = " + tmp);
        sum += tmp * WORD[4][0].length();

        tmp = f5.applyAsInt(min, max); //"thousand"
        System.out.println("frequency( " + WORD[5][0] + " ) = " + tmp);
        sum += tmp * WORD[5][0].length();

        return sum;
    }

    private static FrequencyIntervalMap ourInstance = new FrequencyIntervalMap();

    public static FrequencyIntervalMap getInstance() {
        return ourInstance;
    }

    private FrequencyIntervalMap() {
    }

}
