package sample.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sample.strategies.Benevolent;
import sample.strategies.Cheater;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        GameDetailsTest1.class,
        GameparametersDialogControllerTest1.class,
        MapEditorControllerTest1.class,
        NewMapControllerTest1.class,
        PlayerTest1.class,
        LoadgameControllerTest1.class,
        ControllerTest1.class,
        TournamentControllerTest.class,
        AggressiveTest.class,
        BenevolentTest.class,
        CheaterTest.class,
        RandomTest.class
})
/**
 * This class is test suite class.
 * @authod Team43
 */
public class TestSuite {
}
