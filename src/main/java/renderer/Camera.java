/*
*
* Sobre el segundo parametro de lookAt.
* Es el punto al que la camara mira. TARGET. La direccion de la camara se calcula automaticamente en base a
* su posicion y al target.
*
* Si queremos poder movernos para la derecha pero sin fijar el punto donde la camara mira
* en el origen, tenemos que colocar cameraFront(direccion inicial de la camara, hacia el eje -Z)
* + cameraPosition.
*
* Por ahora:
*
* Podemos movernos para la derecha y para la izquierda en X, modificando el target de forma neceesaria
* automaticamente.
* Podermos movernos para adelante y atras modificando Z.
*
*
* * */

package renderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import window.Window;

import java.util.Vector;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private static Matrix4f view = new Matrix4f();

    private static Vector3f cameraPosition = new Vector3f(0, 0,5.0f);
    public static Vector3f cameraUp = new Vector3f(0, 1.0f, 0);
    public Vector3f target = new Vector3f(0, 0, 0);

    private static Vector3f cameraFront = new Vector3f(0, 0, -1.0f);
    private static Vector3f cameraRight = new Vector3f(0, 0, 0);

    private Vector3f test = new Vector3f(0, 0, 0);

    public static float yaw = -90;
    public static float pitch = 0;

    public Camera() {
        view.identity();
        test.add(cameraFront);
        test.add(cameraPosition);
        view.lookAt(cameraPosition, test, cameraUp);

    }

    public static void processScrolling(long window, double xoffset, double yoffset) {
        Renderer.FOV -= (float) yoffset * sensitivity;

        if (Renderer.FOV < 1.0f) {
            Renderer.FOV = 1.0f;
        }
        if (Renderer.FOV > 45.0f) {
            Renderer.FOV = 45.0f;
        }
    }

    public void updatePosition(float dx, float dy, float dz) {
        cameraPosition.x +=dx;
        cameraPosition.y += dy;
        cameraPosition.z += dz;
    }

    public void setPosition(float posX, float posY, float posZ) {
        cameraPosition.x = posX;
        cameraPosition.y = posY;
        cameraPosition.z = posZ;
    }

    public Matrix4f getView() {

        test.x = 0;
        test.y = 0;
        test.z = 0;
        test.add(cameraFront);
        test.add(cameraPosition);

        view.identity();
        view.lookAt(cameraPosition, test, cameraUp);
        //view.translate(cameraPosition);
        return view;
    }

    /*
    * TODO: ME ROTA BIEN A LA DERECHA PERO ME MUEVO PARA ATRAS.
    * CREO QUE ESTE PROBLEMA SE DEBE A QUE NECESITO ACTUALIZAR EL CAMERARIGHT.
    * RECORDEMOS QUE TARGET = 0. SIEMPRE VOY A ESTAR MIRANDO AL ORIGEN Y LA DIRECCION
    * EN LA QUE MIRA LA CAMARA SE VA A ESTAR ACTUALIZANDO CONSTANTEMENTE...
    * POR ENDE, EL RIGHT TAMBIEN SE DEBE ESTAR ACTUALIANDO CONSTANTEMENTE.
    * */
    public static void processKeyboard() {
        float cameraSpeed = 2.5f * Window.dt;

        int STATE_KEY_UP = glfwGetKey(Window.window, GLFW_KEY_W);
        if (STATE_KEY_UP == GLFW_PRESS)
        {
            cameraPosition.x += cameraFront.x *cameraSpeed;
            cameraPosition.y += cameraFront.y *cameraSpeed;
            cameraPosition.z += cameraFront.z *cameraSpeed;
        }
        int STATE_KEY_DOWN = glfwGetKey(Window.window, GLFW_KEY_S);
        if (STATE_KEY_DOWN == GLFW_PRESS)
        {
            cameraPosition.x -= cameraFront.x *cameraSpeed;
            cameraPosition.y -= cameraFront.y *cameraSpeed;
            cameraPosition.z -= cameraFront.z *cameraSpeed;
        }
        int STATE_KEY_RIGHT = glfwGetKey(Window.window, GLFW_KEY_D);
        if (STATE_KEY_RIGHT == GLFW_PRESS)
        {
            //cameraPos -= glm::normalize(glm::cross(cameraFront, cameraUp)) * cameraSpeed;
            cameraRight.x = cameraFront.x;
            cameraRight.y = cameraFront.y;
            cameraRight.z = cameraFront.z;
            cameraRight.cross(cameraUp);
            cameraRight.normalize();

            cameraPosition.x += cameraRight.x * cameraSpeed;
            cameraPosition.y += cameraRight.y * cameraSpeed;
            cameraPosition.z += cameraRight.z * cameraSpeed;
        }
        int STATE_KEY_LEFT = glfwGetKey(Window.window, GLFW_KEY_A);
        if (STATE_KEY_LEFT == GLFW_PRESS)
        {
            cameraRight.x = cameraFront.x;
            cameraRight.y = cameraFront.y;
            cameraRight.z = cameraFront.z;
            cameraRight.cross(cameraUp);
            cameraRight.normalize();

            cameraPosition.x -= cameraRight.x * cameraSpeed;
            cameraPosition.y -= cameraRight.y * cameraSpeed;
            cameraPosition.z -= cameraRight.z * cameraSpeed;
        }

    }

    private static float lastX = (float) (Window.width / 2.0);
    private static float lastY = (float) (Window.height / 2.0);
    private static float sensitivity = 0.1f;

    public static void processMouse(long window, double xpos, double ypos, boolean firstMouse){

        if(firstMouse) {
            lastX = (float) xpos;
            lastY = (float) ypos;
        }

        float xoffset = (float) (xpos - lastX);
        float yoffset = (float) (ypos - lastY);

        xoffset *= sensitivity;
        yoffset *= sensitivity;

        lastX = (float) xpos;
        lastY = (float) ypos;

        Camera.yaw += xoffset;
        Camera.pitch += yoffset;

        if(Camera.pitch > 89.0f) {
            Camera.pitch = 89.0f;
        }
        if(Camera.pitch < -89.0f){
            Camera.pitch = -89.0f;
        }

        // Update cameraFront
        cameraFront.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        cameraFront.y = -(float) Math.sin(Math.toRadians(pitch));
        cameraFront.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        cameraFront.normalize();

    }
}

