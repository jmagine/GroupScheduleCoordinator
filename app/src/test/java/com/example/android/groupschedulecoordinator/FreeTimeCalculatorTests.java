package com.example.android.groupschedulecoordinator;

import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import com.example.android.groupschedulecoordinator.FreeTimeCalculator;

/**
 * Created by Patrick on 11/19/2016.
 */

public class FreeTimeCalculatorTests {
    @Test
    public void addPersonTimeTest() {
        try {
            FreeTimeCalculator calc = new FreeTimeCalculator(6, 12, 1);
            int initialSize = calc.listOfPeopleTimes.size();
            ArrayList<Integer> personTime = new ArrayList<>(Arrays.asList(
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
            calc.addPersonTime(personTime);
            if (calc.listOfPeopleTimes.size() != initialSize + 1) {
                fail("Addition of person time failed");
            }
            for (int i = 0; i < personTime.size(); ++i) {
                if (calc.listOfPeopleTimes.get(0).get(i) != 1) {
                    fail("Incorrect person time added");
                }
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void fillPossibleTimesTestAlternateOverlap() {
        FreeTimeCalculator calc = new FreeTimeCalculator(6, 12, 1);
        int initialSize = calc.listOfPeopleTimes.size();
        ArrayList<Integer> personTime = new ArrayList<>(Arrays.asList(
                1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1,
                1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        ArrayList<Integer> personTime2 = new ArrayList<>(Arrays.asList(
                1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0,
                1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        calc.addPersonTime(personTime);
        calc.addPersonTime(personTime2);
        calc.fillPossibleTimes();
        for (int i = 0; i < calc.possibleTimes.size(); ++i) {
            System.out.println(calc.possibleTimes.get(i));
        }
        if (calc.possibleTimes.get(0) != 6) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(1) != 10) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(2) != 7) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(3) != 9) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(4) != 11) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(5) != 8) {
            fail("Incorrect list of possible times");
        }
        assertTrue(true);
    }

    @Test
    public void fillPossibleTimesTestAlternateOverlap2() {
        FreeTimeCalculator calc = new FreeTimeCalculator(6, 12, 2);
        int initialSize = calc.listOfPeopleTimes.size();
        ArrayList<Integer> personTime = new ArrayList<>(Arrays.asList(
                1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1,
                1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        ArrayList<Integer> personTime2 = new ArrayList<>(Arrays.asList(
                1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0,
                1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        calc.addPersonTime(personTime);
        calc.addPersonTime(personTime2);
        calc.fillPossibleTimes();
        for (int i = 0; i < calc.possibleTimes.size(); ++i) {
            System.out.println(calc.possibleTimes.get(i));
        }
        if (calc.possibleTimes.get(0) != 6) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(1) != 9) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(2) != 10) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(3) != 7) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(4) != 8) {
            fail("Incorrect list of possible times");
        }
        assertTrue(true);
    }

    @Test
    public void fillPossibleTimesTestAlternateNoOverlap() {
        FreeTimeCalculator calc = new FreeTimeCalculator(6, 12, 1);
        int initialSize = calc.listOfPeopleTimes.size();
        ArrayList<Integer> personTime = new ArrayList<>(Arrays.asList(
                1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1,
                1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        ArrayList<Integer> personTime2 = new ArrayList<>(Arrays.asList(
                1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0,
                0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        calc.addPersonTime(personTime);
        calc.addPersonTime(personTime2);
        calc.fillPossibleTimes();
        for (int i = 0; i < calc.possibleTimes.size(); ++i) {
            System.out.println(calc.possibleTimes.get(i));
        }
        if (calc.possibleTimes.get(0) != 6) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(1) != 7) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(2) != 8) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(3) != 9) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(4) != 10) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(5) != 11) {
            fail("Incorrect list of possible times");
        }
        assertTrue(true);
    }

    @Test
    public void fillPossibleTimesTestAllOut() {
        FreeTimeCalculator calc = new FreeTimeCalculator(24, 36, 1);
        int initialSize = calc.listOfPeopleTimes.size();
        ArrayList<Integer> personTime = new ArrayList<>(Arrays.asList(
                1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1,
                1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        ArrayList<Integer> personTime2 = new ArrayList<>(Arrays.asList(
                1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0,
                1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        calc.addPersonTime(personTime);
        calc.addPersonTime(personTime2);
        calc.fillPossibleTimes();
        for (int i = 0; i < calc.possibleTimes.size(); ++i) {
            System.out.println(calc.possibleTimes.get(i));
        }
        if (calc.possibleTimes.get(0) != 24) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(1) != 25) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(2) != 26) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(3) != 27) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(4) != 28) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(5) != 29) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(6) != 30) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(7) != 31) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(8) != 32) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(9) != 33) {
            fail("Incorrect list of possible times");
        }
        assertTrue(true);
    }

    @Test
    public void fillPossibleTimesTestAllIn() {
        FreeTimeCalculator calc = new FreeTimeCalculator(24, 36, 1);
        int initialSize = calc.listOfPeopleTimes.size();
        ArrayList<Integer> personTime = new ArrayList<>(Arrays.asList(
                1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1,
                1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        ArrayList<Integer> personTime2 = new ArrayList<>(Arrays.asList(
                1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0,
                1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        calc.addPersonTime(personTime);
        calc.addPersonTime(personTime2);
        calc.fillPossibleTimes();
        for (int i = 0; i < calc.possibleTimes.size(); ++i) {
            System.out.println(calc.possibleTimes.get(i));
        }
        if (calc.possibleTimes.get(0) != 24) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(1) != 25) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(2) != 26) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(3) != 27) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(4) != 28) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(5) != 29) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(6) != 30) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(7) != 31) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(8) != 32) {
            fail("Incorrect list of possible times");
        }
        if (calc.possibleTimes.get(9) != 33) {
            fail("Incorrect list of possible times");
        }
        assertTrue(true);
    }
}
