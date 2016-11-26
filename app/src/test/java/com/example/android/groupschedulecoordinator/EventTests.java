package com.example.android.groupschedulecoordinator;

/**
 * Created by Patrick on 11/19/2016.
 */

import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import com.example.android.groupschedulecoordinator.Event;

public class EventTests {
    @Test
    public void setHostIDTest() {
        ArrayList<String> pendingUsers = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));
        Event dummyEvent = new Event("testEvent", pendingUsers);
        dummyEvent.setHostID("hoster");
        assertThat(dummyEvent.getHostID(), is("hoster"));
    }

    @Test
    public void getEventIDTest() {
        ArrayList<String> pendingUsers = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));
        Event dummyEvent = new Event("testEvent", pendingUsers);
        assertThat(dummyEvent.getEventID(), is("testEvent"));
    }

    @Test
    public void setStartTestValid() {
        ArrayList<String> pendingUsers = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));
        Event dummyEvent = new Event("testEvent", pendingUsers);
        dummyEvent.setStart(2);
        assertThat(dummyEvent.getStart(), is(2));
    }

    @Test
    public void setStartTestNotValid() {
        try {
            ArrayList<String> pendingUsers = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));
            Event dummyEvent = new Event("testEvent", pendingUsers);
            dummyEvent.setStart(-1);
            fail("Negative integers not allowed");
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void setEndTestValid() {
        ArrayList<String> pendingUsers = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));
        Event dummyEvent = new Event("testEvent", pendingUsers);
        dummyEvent.setEnd(3);
        assertThat(dummyEvent.getEnd(), is(3));
    }

    @Test
    public void setEndTestNotValid() {
        try {
            ArrayList<String> pendingUsers = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));
            Event dummyEvent = new Event("testEvent", pendingUsers);
            dummyEvent.setEnd(-1);
            fail("Negative integers not allowed");
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void setReadyTest() {
        ArrayList<String> pendingUsers = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));
        Event dummyEvent = new Event("testEvent", pendingUsers);
        dummyEvent.setReady(true);
        assertThat(dummyEvent.getReady(), is(true));
    }

    @Test
    public void setPendingUsersTest() {
        try {
            ArrayList<String> pendingUsers = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));
            Event dummyEvent = new Event("testEvent", pendingUsers);
            pendingUsers.add("user4");
            dummyEvent.setPendingUsers(pendingUsers);
            if (!dummyEvent.getPendingUsers().get(0).equals("user1")) {
                fail();
            }
            if (!dummyEvent.getPendingUsers().get(1).equals("user2")) {
                fail();
            }
            if (!dummyEvent.getPendingUsers().get(2).equals("user3")) {
                fail();
            }
            if (!dummyEvent.getPendingUsers().get(3).equals("user4")) {
                fail();
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void setCompletedUsersTest() {
        try {
            ArrayList<String> pendingUsers = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));
            Event dummyEvent = new Event("testEvent", pendingUsers);
            ArrayList<String> completedUsers = new ArrayList<>(Arrays.asList("user1", "user2", "user3", "user4"));
            dummyEvent.setCompletedUsers(completedUsers);
            if (!dummyEvent.getCompletedUsers().get(0).equals("user1")) {
                fail();
            }
            if (!dummyEvent.getCompletedUsers().get(1).equals("user2")) {
                fail();
            }
            if (!dummyEvent.getCompletedUsers().get(2).equals("user3")) {
                fail();
            }
            if (!dummyEvent.getCompletedUsers().get(3).equals("user4")) {
                fail();
            }
            assertTrue(true);
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    public void moveValidUserTest() {
        try {
            ArrayList<String> pendingUsers = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));
            Event dummyEvent = new Event("testEvent", pendingUsers);
            dummyEvent.moveUser("user2");
            if (!dummyEvent.getPendingUsers().get(0).equals("user1")) {
                fail("Pending users list incorrectly changed");
            }
            if (!dummyEvent.getPendingUsers().get(1).equals("user3")) {
                fail("Pending users list incorrectly changed");
            }
            if (!dummyEvent.getCompletedUsers().get(0).equals("user2")) {
                fail("Completed users list incorrectly changed");
            }
            if (dummyEvent.getCompletedUsers().size() == 1) {
                assertTrue(true);
            }
            else {
                fail("Completed users list incorrectly changed");
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            fail("Invalid index accessed");
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void moveInvalidUserTest() {
        try {
            ArrayList<String> pendingUsers = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));
            Event dummyEvent = new Event("testEvent", pendingUsers);
            dummyEvent.moveUser("user4");
            if (!dummyEvent.getPendingUsers().get(0).equals("user1")) {
                fail("Pending users list incorrectly changed");
            }
            if (!dummyEvent.getPendingUsers().get(1).equals("user2")) {
                fail("Pending users list incorrectly changed");
            }
            if (!dummyEvent.getPendingUsers().get(2).equals("user3")) {
                fail("Pending users list incorrectly changed");
            }
            if (dummyEvent.getCompletedUsers().size() == 0) {
                assertTrue(true);
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            fail("Invalid index accessed");
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void toMapTest() {
        ArrayList<String> pendingUsers = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));
        Event dummyEvent = new Event("testEvent", pendingUsers);
        dummyEvent.setStart(2);
        dummyEvent.setEnd(3);
        dummyEvent.setReady(true);
        Map<String,Object> mappedEvent = dummyEvent.toMap();
        if (!mappedEvent.get("name").equals("testEvent")) {
            fail("Incorrect event name");
        }
        if ((int)mappedEvent.get("start") != 2) {
            fail("Incorrect start");
        }
        if ((int)mappedEvent.get("end") != 3) {
            fail("Incorrect end");
        }
        if (!(boolean)mappedEvent.get("isReady")) {
            fail("Incorrect isReady");
        }
        if (mappedEvent.get("pendingUsers") != dummyEvent.getPendingUsers()) {
            fail("Incorrect pendingUsers");
        }
        if (mappedEvent.get("completedUsers") != dummyEvent.getCompletedUsers()) {
            fail("Incorrect completedUsers");
        }
        assertTrue(true);
    }
}
