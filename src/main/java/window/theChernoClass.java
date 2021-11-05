package window;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import scene.IndexBuffer;
import scene.VertexBuffer;

import java.nio.FloatBuffer;

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
    private Matrix4f transformation = new Matrix4f();
    private VertexBuffer vbo;
    private IndexBuffer ibo;

    private int vaoID;

    public void init() {

        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        transformation.identity();
        transformation.ortho(-6.0f, 5.0f, -5.0f, 5.0f, -5.0f, 5.0f);
        defaultShader.setUniformMat4f("transformation", transformation);

        float[] vertexArray = {
                // position               // color
                5.5f, -5.5f, 0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
                -5.5f,  5.5f, 0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
                5.5f,  5.5f, 0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
                -5.5f, -5.5f, 0f,       1.0f, 1.0f, 0.0f, 1.0f // Bottom left  3
        };

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vbo = new VertexBuffer(vertexArray);

        // Layout of Vertex Buffer.
        int floatSizeBytes = 4;
        int positionSize = 3;
        int colorSize = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

        ibo = new IndexBuffer(indices);


    }

    public void update( float dt ) {

        defaultShader.bind();
        glBindVertexArray(vaoID); // Vao se encarga de vbo y el layout.
        ibo.bind();

        // Count: number of indices
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0 ); // Uses an Element/Index Buffer.


        ibo.unBind();
        glBindVertexArray(0);
        defaultShader.unbind();

    }


}
