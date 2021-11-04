package scene;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL20.*;

public class TestScene extends Scene{

    private int vertexID, fragmentID, shaderProgram;

    private VertexBuffer vb;
    private IndexBuffer ib;

    private final float[] vertexArray = {
            // position               // color
            0.5f, -0.5f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            -0.5f,  0.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            0.5f,  0.5f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            -0.5f, -0.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private final int[] elementArray = {
            /*
                    1        2
                    3        0
             */
            2, 1, 0, // Top right triangle
            0, 1, 3 // bottom left triangle
    };

    private int vaoID;

    private String vertexShaderSrc = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "uniform mat4 mvp;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = mvp * vec4(aPos,1.0);\n" +
            "}";

    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "out vec4 color;\n" +
            "\n" +
            "uniform vec4 u_Color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = u_Color;\n" +
            "}";

    // Load and compile vertex and fragment shader.
    // Link shaders.
    @Override
    public void init() {
        // First load and compile the vertex shader.
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        // Pass the shader source to the GPU
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);

        // Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders and check for errors...
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        // Check for linking errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }

        // Set color uniform variable
        glUseProgram(shaderProgram);
        int Colorlocation = glGetUniformLocation(shaderProgram, "u_Color");
        glUniform4f(Colorlocation, 0.2f, 0.3f, 0.8f, 1.0f);

        // Set matrix uniform variable
        glUseProgram(shaderProgram);
        int mvpLocation = glGetUniformLocation(shaderProgram, "mvp");
        Matrix4f mvpMatrix = new Matrix4f();
        mvpMatrix.identity();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mvpMatrix.get(matBuffer);
        glUniformMatrix4fv(mvpLocation, false, matBuffer);


        // ============================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ============================================================


        // Create VAO and bind. VAO = Vertex Array.
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create vertex buffer. 1 Vertex may be composed of many attributes.
        vb = new VertexBuffer(vertexArray);


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
        System.out.println(1 / dt + " FPS");

        // Bind shader program
        glUseProgram(shaderProgram);
        // Bind the VAO that we're using
        glBindVertexArray(vaoID);
        // Bind element buffer
        //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        ib.bind();

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        ib.unBind();

        glBindVertexArray(0);

        glUseProgram(0);
    }

}
