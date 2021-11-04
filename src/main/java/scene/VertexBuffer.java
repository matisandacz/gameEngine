package scene;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;

public class VertexBuffer {
    private int vboID;

    public VertexBuffer(float[] vertexArray) {
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

    }

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
    }

    public void unBind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
