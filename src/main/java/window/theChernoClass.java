package window;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import renderer.Camera;
import renderer.Renderer;
import renderer.Shader;
import renderer.Texture;
import scene.IndexBuffer;
import scene.VertexBuffer;

import java.nio.FloatBuffer;

import static java.lang.Math.cos;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;


public class theChernoClass {

    // Create an index Buffer
    int[] indices = {
            0, 1, 2,
            0, 1, 3
    };

    private Shader defaultShader;

    private VertexBuffer vbo;
    private IndexBuffer ibo;

    private int vaoID;

    private Renderer renderer = new Renderer();
    private Camera camera = new Camera();
    private Texture texture;

    public void init() {

        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        texture = new Texture("assets/images/wall.jpg");


        float[] vertexArray = {
                // position               // color                      // Texture coordinates
                50.0f,  0f, -50.0f ,              50, 50,                                    // Top right    2
                -50.0f,  0f, 50.0f,             0, 50,                                        // Top left     1
                50.0f, 0f, 50.0f,                50, 0,                                        // Bottom right 0
                -50.0f, 0, -50.0f,              0, 0,                                        // Bottom Left 0
                -50.0f,  0f,50.0f,             0, 50,                                        // Top left     1
                50.0f, 0f, -50.0f,                 50, 0,                                        // Bottom right 0
        };

        /* CON INDICES
        float[] vertexArray = {
                // position               // color
                5.0f, -5.0f, -5.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
                -5.0f,  5.0f, -5.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
                5.0f,  5.0f, -5.0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
        };

         */


        /*
        float vertexArray[] = {
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
        };

         */

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vbo = new VertexBuffer(vertexArray);

        // Layout of Vertex Buffer.
        int floatSizeBytes = 4;
        int positionSize = 3;
        int textureSize = 2;
        int vertexSizeBytes = (positionSize + textureSize) * floatSizeBytes;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, textureSize, GL_FLOAT, false, vertexSizeBytes, (positionSize ) * floatSizeBytes);
        glEnableVertexAttribArray(1);

        ibo = new IndexBuffer(indices);

        defaultShader.setUniformMat4f("model", renderer.getModel());
        defaultShader.setUniformMat4f("view", camera.getView());
        defaultShader.setUniformMat4f("projection", renderer.getProjection());

    }

    public float getTime() {
        return (float) ((float) System.nanoTime() / 1000000000.0);
    }

    public void update( float dt ) {

        defaultShader.bind();
        glBindVertexArray(vaoID); // Vao se encarga de vbo y el layout.
        ibo.bind();

        Camera.processKeyboard();

        defaultShader.setUniformMat4f("model", renderer.getModel());
        defaultShader.setUniformMat4f("view", camera.getView());
        defaultShader.setUniformMat4f("projection", renderer.getProjection());

        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        texture.bind();


        //glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0 ); // Uses an Element/Index Buffer.
        glDrawArrays(GL_TRIANGLES, 0, 6);

        texture.unbind();
        ibo.unBind();
        glBindVertexArray(0);
        defaultShader.unbind();

    }


}
