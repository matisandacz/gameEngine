package window;

import eventListener.KeyListener;
import eventListener.MouseListener;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import scene.Scene;
import scene.TestScene;
import util.Time;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class Window {

    public static long window;
    public static int width, height;
    public static float dt;
    private String title;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Set event callback
        //glfwSetKeyCallback(window, KeyListener::key_callback);
        // Mouse callbacks
        glfwSetCursorPosCallback(window, MouseListener::mouse_callback);
        glfwSetScrollCallback(window, MouseListener::scroll_callback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.

        GL.createCapabilities();

        // Set the clear color
        glClearColor(0f, 0f, 0f, 0.0f);
        glEnable(GL_DEPTH_TEST);

        // Makes the mouse invisible and forces it to stay inside the window.
        //glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        dt = 0;

        // Create and initialize scene.
        Scene testScene = new TestScene();
        testScene.init();

        theChernoClass theCherno = new theChernoClass();
        theCherno.init();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {


            glfwSwapBuffers(window); // swap the color buffers
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();



            if (dt > 0) {
                //testScene.update(dt);
                theCherno.update(dt);
            }



            // FPS = 1/dt
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;



        }
    }

}
