package eventListener;

import renderer.Camera;
import window.Window;

public class MouseListener {

    private static boolean firstMouse = true;

    public static void mouse_callback(long window, double xpos, double ypos) {
        Camera.processMouse(window, xpos, ypos, firstMouse);
        if (firstMouse) {
            firstMouse = false;
        }
    }

    public static void scroll_callback(long window, double xoffset, double yoffset) {
        Camera.processScrolling(window, xoffset, yoffset);
    }
}
