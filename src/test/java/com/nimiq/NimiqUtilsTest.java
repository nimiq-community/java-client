package com.nimiq;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import static com.nimiq.NimiqUtils.coinsToLunas;
import static com.nimiq.NimiqUtils.lunasToCoins;

/**
 * NimiqUtilsTest
 */
public class NimiqUtilsTest {

    @Test
    public void testCoinsToLunas() {
        assertEquals(0, BigDecimal.ZERO.compareTo(lunasToCoins(0)));
        assertEquals(0, BigDecimal.ONE.compareTo(lunasToCoins(100000)));
        assertEquals(0, new BigDecimal("1.23456").compareTo(lunasToCoins(123456)));
    }

    @Test
    public void testLunasToCoins() {
        assertEquals(0, coinsToLunas(BigDecimal.ZERO));
        assertEquals(100000, coinsToLunas(BigDecimal.ONE));
        assertEquals(123456, coinsToLunas(new BigDecimal("1.23456")));
        assertEquals(100000, coinsToLunas(new BigDecimal("1.000001")));
        assertEquals(133333, coinsToLunas(new BigDecimal("1.333333")));
        assertEquals(166667, coinsToLunas(new BigDecimal("1.666666")));
        assertEquals(200000, coinsToLunas(new BigDecimal("1.999999")));
    }
}
