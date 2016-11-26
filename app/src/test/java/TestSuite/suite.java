package TestSuite;

/**
 * Created by Patrick on 11/19/2016.
 */

import com.example.android.groupschedulecoordinator.EventTests;
import com.example.android.groupschedulecoordinator.FreeTimeCalculatorTests;
import com.example.android.groupschedulecoordinator.GroupTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Runs all unit tests
@RunWith(Suite.class)
@Suite.SuiteClasses({EventTests.class, FreeTimeCalculatorTests.class, GroupTests.class})
public class suite {

}
