package com.example.android.groupschedulecoordinator;

/**
 * Created by Patrick on 11/27/2016.
 */

import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.example.android.groupschedulecoordinator.User;

public class UserTests {
    @Test
    public void setUserNameTest() {
        try {
            User testUser = new User();
            testUser.setUserName("Bob");
            assertThat(testUser.getUserName(), is("Bob"));
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void setPendingGroupsTest() {
        try {
            User testUser = new User();
            HashMap<String, String> pendingGroups = new HashMap<>();
            pendingGroups.put("1", "foo");
            pendingGroups.put("2", "bar");
            pendingGroups.put("3", "test");
            testUser.setPendingGroups(pendingGroups);
            if (testUser.getPendingGroups().size() != 3) {
                fail("Incorrect list size");
            }
            if (!testUser.getPendingGroups().get("1").equals("foo")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getPendingGroups().get("2").equals("bar")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getPendingGroups().get("3").equals("test")) {
                fail("Incorrect element in list");
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void setAcceptedGroupsTest() {
        try {
            User testUser = new User();
            HashMap<String, String> acceptedGroups = new HashMap<>();
            acceptedGroups.put("1", "foo");
            acceptedGroups.put("2", "bar");
            acceptedGroups.put("3", "test");
            testUser.setAcceptedGroups(acceptedGroups);
            if (testUser.getAcceptedGroups().size() != 3) {
                fail("Incorrect list size");
            }
            if (!testUser.getAcceptedGroups().get("1").equals("foo")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getAcceptedGroups().get("2").equals("bar")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getAcceptedGroups().get("3").equals("test")) {
                fail("Incorrect element in list");
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void setFreeTimesTest() {
        try {
            User testUser = new User();
            HashMap<String, ArrayList<Integer>> freeTimes = new HashMap<>();
            ArrayList<Integer> list1 = new ArrayList<>();
            list1.add(1);
            list1.add(2);
            list1.add(3);
            ArrayList<Integer> list2 = new ArrayList<>();
            list2.add(4);
            list2.add(5);
            list2.add(6);
            ArrayList<Integer> list3 = new ArrayList<>();
            list3.add(7);
            list3.add(8);
            list3.add(9);
            freeTimes.put("1", list1);
            freeTimes.put("2", list2);
            freeTimes.put("3", list3);
            testUser.setFreeTimes(freeTimes);
            if (testUser.getFreeTimes().size() != 3) {
                fail("Incorrect list size");
            }
            if (!testUser.getFreeTimes().get("1").equals(list1)) {
                fail("Incorrect element in list");
            }
            if (!testUser.getFreeTimes().get("2").equals(list2)) {
                fail("Incorrect element in list");
            }
            if (!testUser.getFreeTimes().get("3").equals(list3)) {
                fail("Incorrect element in list");
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void addToPendingGroupTest() {
        try {
            User testUser = new User();
            HashMap<String, String> pendingGroups = new HashMap<>();
            pendingGroups.put("1", "foo");
            pendingGroups.put("2", "bar");
            pendingGroups.put("3", "test");
            testUser.setPendingGroups(pendingGroups);
            testUser.addToPendingGroup("4", "pending");
            if (testUser.getPendingGroups().size() != 4) {
                fail("Incorrect list size");
            }
            if (!testUser.getPendingGroups().get("1").equals("foo")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getPendingGroups().get("2").equals("bar")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getPendingGroups().get("3").equals("test")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getPendingGroups().get("4").equals("pending")) {
                fail("Incorrect element in list");
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void addToPendingGroupTestExisting() {
        try {
            User testUser = new User();
            HashMap<String, String> pendingGroups = new HashMap<>();
            pendingGroups.put("1", "foo");
            pendingGroups.put("2", "bar");
            pendingGroups.put("3", "test");
            testUser.setPendingGroups(pendingGroups);
            testUser.addToPendingGroup("2", "pending");
            if (testUser.getPendingGroups().size() != 3) {
                fail("Incorrect list size");
            }
            if (!testUser.getPendingGroups().get("1").equals("foo")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getPendingGroups().get("2").equals("bar")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getPendingGroups().get("3").equals("test")) {
                fail("Incorrect element in list");
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void rejectValidGroupTest() {
        try {
            User testUser = new User();
            HashMap<String, String> pendingGroups = new HashMap<>();
            pendingGroups.put("1", "foo");
            pendingGroups.put("2", "bar");
            pendingGroups.put("3", "test");
            pendingGroups.put("4", "pending");
            testUser.setPendingGroups(pendingGroups);
            testUser.rejectGroup("2");
            if (testUser.getPendingGroups().size() != 3) {
                fail("Incorrect list size");
            }
            if (!testUser.getPendingGroups().get("1").equals("foo")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getPendingGroups().get("3").equals("test")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getPendingGroups().get("4").equals("pending")) {
                fail("Incorrect element in list");
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void rejectInvalidGroupTest() {
        try {
            User testUser = new User();
            HashMap<String, String> pendingGroups = new HashMap<>();
            pendingGroups.put("1", "foo");
            pendingGroups.put("2", "bar");
            pendingGroups.put("3", "test");
            pendingGroups.put("4", "pending");
            testUser.setPendingGroups(pendingGroups);
            testUser.rejectGroup("5");
            if (testUser.getPendingGroups().size() != 4) {
                fail("Incorrect list size");
            }
            if (!testUser.getPendingGroups().get("1").equals("foo")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getPendingGroups().get("2").equals("bar")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getPendingGroups().get("3").equals("test")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getPendingGroups().get("4").equals("pending")) {
                fail("Incorrect element in list");
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void acceptValidGroupTest() {
        try {
            User testUser = new User();
            HashMap<String, String> pendingGroups = new HashMap<>();
            pendingGroups.put("1", "foo");
            pendingGroups.put("2", "bar");
            pendingGroups.put("3", "test");
            testUser.setPendingGroups(pendingGroups);
            HashMap<String, String> acceptedGroups = new HashMap<>();
            acceptedGroups.put("4", "oof");
            acceptedGroups.put("5", "rab");
            acceptedGroups.put("6", "tset");
            testUser.setAcceptedGroups(acceptedGroups);
            testUser.acceptGroup("2", "bar");
            if (testUser.getPendingGroups().size() != 2) {
                fail("Incorrect list size");
            }
            if (testUser.getAcceptedGroups().size() != 4) {
                fail("Incorrect list size");
            }
            if (!testUser.getPendingGroups().get("1").equals("foo")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getPendingGroups().get("3").equals("test")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getAcceptedGroups().get("2").equals("bar")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getAcceptedGroups().get("4").equals("oof")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getAcceptedGroups().get("5").equals("rab")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getAcceptedGroups().get("6").equals("tset")) {
                fail("Incorrect element in list");
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void removeSelfFromValidGroupTest() {
        try {
            User testUser = new User();
            HashMap<String, String> acceptedGroups = new HashMap<>();
            acceptedGroups.put("1", "foo");
            acceptedGroups.put("2", "bar");
            acceptedGroups.put("3", "test");
            testUser.setAcceptedGroups(acceptedGroups);
            testUser.removeSelfFromGroup("2");
            if (testUser.getAcceptedGroups().size() != 2) {
                fail("Incorrect list size");
            }
            if (!testUser.getAcceptedGroups().get("1").equals("foo")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getAcceptedGroups().get("3").equals("test")) {
                fail("Incorrect element in list");
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void removeSelfFromInvalidGroupTest() {
        try {
            User testUser = new User();
            HashMap<String, String> acceptedGroups = new HashMap<>();
            acceptedGroups.put("1", "foo");
            acceptedGroups.put("2", "bar");
            acceptedGroups.put("3", "test");
            testUser.setAcceptedGroups(acceptedGroups);
            testUser.removeSelfFromGroup("4");
            if (testUser.getAcceptedGroups().size() != 3) {
                fail("Incorrect list size");
            }
            if (!testUser.getAcceptedGroups().get("1").equals("foo")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getAcceptedGroups().get("2").equals("bar")) {
                fail("Incorrect element in list");
            }
            if (!testUser.getAcceptedGroups().get("3").equals("test")) {
                fail("Incorrect element in list");
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void toMapTest() {
        try {
            User testUser = new User();
            testUser.setUserName("Bob");
            HashMap<String, String> pendingGroups = new HashMap<>();
            pendingGroups.put("1", "foo");
            pendingGroups.put("2", "bar");
            pendingGroups.put("3", "test");
            testUser.setPendingGroups(pendingGroups);
            HashMap<String, String> acceptedGroups = new HashMap<>();
            acceptedGroups.put("4", "oof");
            acceptedGroups.put("5", "rab");
            acceptedGroups.put("6", "tset");
            testUser.setAcceptedGroups(acceptedGroups);
            HashMap<String, ArrayList<Integer>> freeTimes = new HashMap<>();
            ArrayList<Integer> list1 = new ArrayList<>();
            list1.add(1);
            list1.add(2);
            list1.add(3);
            ArrayList<Integer> list2 = new ArrayList<>();
            list2.add(4);
            list2.add(5);
            list2.add(6);
            ArrayList<Integer> list3 = new ArrayList<>();
            list3.add(7);
            list3.add(8);
            list3.add(9);
            freeTimes.put("1", list1);
            freeTimes.put("2", list2);
            freeTimes.put("3", list3);
            testUser.setFreeTimes(freeTimes);
            Map<String, Object> testMap = testUser.toMap();
            if (testMap.size() != 4) {
                fail("Incorrect list size");
            }
            if (!testMap.get("userName").equals("Bob")) {
                fail("Incorrect name");
            }
            if (!testMap.get("pendingGroups").equals(pendingGroups)) {
                fail("Incorrect pending groups");
            }
            if (!testMap.get("acceptedGroups").equals(acceptedGroups)) {
                fail("Incorrect accepted groups");
            }
            if (!testMap.get("freeTimes").equals(freeTimes)) {
                fail("Incorrect free times");
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }
}
