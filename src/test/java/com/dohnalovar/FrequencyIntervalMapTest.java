package com.dohnalovar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dohnalovar on 7/8/2019
 */
class FrequencyIntervalMapTest {

    @Test
    void numberLetterCounts() {
        FrequencyIntervalMap fim = FrequencyIntervalMap.getInstance();
        int result = fim.numberLetterCounts(1,5);
        assertEquals(19, result);
    }
}