package renderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import window.Window;

public class Renderer {

    private Matrix4f model = new Matrix4f();
    private Matrix4f projection = new Matrix4f();

    public static float FOV = 45.0f;

    public Renderer() {
        model.identity();
        //model.translate(1.0f, -1.0f, 0.0f);
        //model.rotate((float) Math.toRadians(-55.0f), new Vector3f(1.0f, 0.0f, 0.0f));

        projection.identity();
        projection.perspective(FOV, (float) Window.width/(float)Window.height, 0.1f, 100.0f);
    }

    public Matrix4f getModel() {
        return model;
    }

    public Matrix4f getProjection() {
        projection.identity();
        projection.perspective(FOV, (float) Window.width/(float)Window.height, 0.1f, 100.0f);
        return projection;
    }

}
