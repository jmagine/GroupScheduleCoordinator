package com.example.android.groupschedulecoordinator;

/**
 * Created by Patrick on 11/25/2016.
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
import com.example.android.groupschedulecoordinator.Group;

public class GroupTests {
    @Test
    public void setGroupNameTest() {
        try {
            Group testGroup = new Group();
            testGroup.setGroupName("foo");
            assertThat(testGroup.getGroupName(), is("foo"));
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void setMembersTest() {
        try {
            Group testGroup = new Group();
            HashMap<String, String> membersList = new HashMap<>();
            membersList.put("2", "bar");
            membersList.put("3", "Jack");
            membersList.put("4", "Jill");
            testGroup.setMembers(membersList);
            if (testGroup.getMembers().size() != 3) {
                fail("Incorrect list size");
            }
            if (!testGroup.getMembers().get("2").equals("bar")) {
                fail("Incorrect members in list");
            }
            if (!testGroup.getMembers().get("3").equals("Jack")) {
                fail("Incorrect members in list");
            }
            if (!testGroup.getMembers().get("4").equals("Jill")) {
                fail("Incorrect members in list");
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void setEventsTest() {
        try {
            Group testGroup = new Group();
            HashMap<String, Event> eventsList = new HashMap<>();
            Event event1 = new Event();
            Event event2 = new Event();
            Event event3 = new Event();
            eventsList.put("2", event1);
            eventsList.put("3", event2);
            eventsList.put("4", event3);
            testGroup.setEvents(eventsList);
            if (testGroup.getEvents().size() != 3) {
                fail("Incorrect list size");
            }
            if (!testGroup.getEvents().get("2").equals(event1)) {
                fail("Incorrect events in list");
            }
            if (!testGroup.getEvents().get("3").equals(event2)) {
                fail("Incorrect events in list");
            }
            if (!testGroup.getEvents().get("4").equals(event3)) {
                fail("Incorrect members in list");
            }
            assertTrue(true);
        }
        catch (Exception e){
            fail("Exception thrown");
        }
    }

    @Test
    public void addMemberTest() {
        try {
            Group testGroup = new Group("testgroup");
            testGroup.addMember("1", "Bob");
            if (testGroup.getMembers().containsKey("1")) {
                assertTrue(testGroup.getMembers().get("1").equals("Bob"));
            }
            else {
                fail("List does not contain key");
            }
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void addExistingMemberTest() {
        try {
            Group testGroup = new Group("testgroup");
            testGroup.addMember("1", "Bob");
            testGroup.addMember("1", "foo");
            if (testGroup.getMembers().containsKey("1")) {
                assertThat(testGroup.getMembers().get("1"), is("Bob"));
            }
            else {
                fail("List does not contain key");
            }
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void addSameNameMemberTest() {
        try {
            Group testGroup = new Group("testgroup");
            testGroup.addMember("1", "Bob");
            testGroup.addMember("2", "Bob");
            if (!testGroup.getMembers().containsKey("1")) {
                fail("List does not contain key");
            }
            if (!testGroup.getMembers().get("1").equals("Bob")) {
                fail("Wrong member in list");
            }
            if (!testGroup.getMembers().containsKey("2")) {
                fail("List does not contain key");
            }
            if (!testGroup.getMembers().get("2").equals("Bob")) {
                fail("Wrong member in list");
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void deleteMemberTest() {
        try {
            Group testGroup = new Group("testgroup");
            testGroup.addMember("1", "Bob");
            testGroup.addMember("2", "foo");
            testGroup.deleteMember("1");
            if (testGroup.getMembers().size() != 1) {
                fail("Incorrect list size");
            }
            if (testGroup.getMembers().containsKey("2")) {
                assertThat(testGroup.getMembers().get("2"), is("foo"));
            }
            else {
                fail("List does not contain key");
            }
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void addEventTest() {
        try {
            Group testGroup = new Group("testgroup");
            testGroup.addEvent("1", "event1");
            assertThat(testGroup.getEvents().size(), is(1));
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void toMapTest() {
        try {
            Group testGroup = new Group("testgroup");
            HashMap<String, String> membersList = new HashMap<>();
            membersList.put("2", "bar");
            membersList.put("3", "Jack");
            membersList.put("4", "Jill");
            testGroup.setMembers(membersList);
            HashMap<String, Event> eventsList = new HashMap<>();
            Event event1 = new Event();
            Event event2 = new Event();
            Event event3 = new Event();
            eventsList.put("2", event1);
            eventsList.put("3", event2);
            eventsList.put("4", event3);
            testGroup.setEvents(eventsList);
            Map<String, Object> result = testGroup.toMap();
            if (!result.get("groupName").equals(testGroup.getGroupName())) {
                fail("Group name incorrect");
            }
            if (!result.get("members").equals(testGroup.getMembers())) {
                fail("Members list incorrect");
            }
            if (!result.get("events").equals(testGroup.getEvents())) {
                fail("Events list incorrect");
            }
            assertTrue(true);
        }
        catch (Exception e) {
            fail("Exception thrown");
        }
    }
}
