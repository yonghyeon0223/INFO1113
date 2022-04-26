package lawnlayer;

import lawnlayer.GameObjects.*;
import processing.core.PApplet;
import processing.core.PImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SampleTest {

    @Test
    public void simpleTest() {
        App app = new App();
        Player p = new Player(app);
    }
}
