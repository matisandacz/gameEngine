package scene;

import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

public class IndexBuffer {
    private int eboID;

    public IndexBuffer(int[] elementArray) {
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

    }

    public void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
    }

    public void unBind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
