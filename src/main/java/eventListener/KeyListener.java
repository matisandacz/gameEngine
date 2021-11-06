package eventListener;

import renderer.Camera;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {

    private static KeyListener instance;
    private static boolean keyPressed[] = new boolean[350];

    public static void key_callback(long window, int key, int scancode, int action, int mods) {
        Camera.processKeyboard();
    }

    public static boolean isKeyPressed(int keyCode) {
        return keyPressed[keyCode];
    }

}
