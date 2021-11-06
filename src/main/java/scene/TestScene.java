package scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import window.Window;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL20.*;

public class TestScene extends Scene{

    private Shader defaultShader;

    //private VertexBuffer vb;
    private IndexBuffer ib;

    //private int vaoID;

    /*
    private float[] vertexArray = {
            // position               // color
            0.5f, -0.5f, 0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            -0.5f,  0.5f, 0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            0.5f,  0.5f, 0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            -0.5f, -0.5f, 0f,       1.0f, 1.0f, 0.0f, 1.0f // Bottom left  3
    };

     */

    // IMPORTANT: Must be in counter-clockwise order
    private final int[] elementArray = {
            /*
                    1        2
                    3        0
             */
            2, 1, 0, // Top right triangle
            0, 1, 3 // bottom left triangle
    };

    // Transformation matrix
    private Matrix4f transformation = new Matrix4f();

    private Matrix4f testTransform = new Matrix4f();

    // Load and compile vertex and fragment shader.
    // Link shaders.
    @Override
    public void init() {

        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        Vector4f color = new Vector4f(0.1f, 0.0f, 0.9f, 0.0f);
        defaultShader.setUniform4fv("u_Color", color);

        //transformation.identity();
        //transformation.ortho(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);
        //defaultShader.setUniformMat4f("transformation", transformation);

        testTransform.identity();
        testTransform.perspective((float) Math.toRadians(45.0f), (float)Window.width/(float)Window.height, 0.1f, 100.0f);
        defaultShader.setUniformMat4f("transformation", transformation);


        // ============================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ============================================================

        // Create VAO and bind. VAO = Vertex Array.
        //vaoID = glGenVertexArrays();
        //glBindVertexArray(vaoID);

        // Create vertex buffer. 1 Vertex may be composed of many attributes.
        //vb = new VertexBuffer(vertexArray);

        // Layout of Vertex Buffer.
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

        // Create EBO. Index Buffer/Element buffer Object.
        ib = new IndexBuffer(elementArray);

    }

    @Override
    public void update(float dt) {
        //System.out.println(1 / dt + " FPS");

        // Bind shader program
        defaultShader.bind();
        //defaultShader.setUniformMat4f("transformation", transformation);

        // Update transformation matrix.

        // Bind the VAO that we're using
        //glBindVertexArray(vaoID);

        // Bind element/index buffer
        ib.bind();

        //vb.bind();

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        ib.unBind();

        //vb.unBind();

        glBindVertexArray(0);

        defaultShader.unbind();
    }

}
