package window;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;


public class theChernoClass {

    // Create an index Buffer
    int[] indices = {
            0, 1, 2,
            0, 1, 3
    };

    public void init() {

        int floatSizeBytes = 4;
        float[] positions = {
                // position
                0.5f, -0.5f, 0.0f, //br 0
                -0.5f,  0.5f, 0.0f, //tl 1
                0.5f,  0.5f, 0.0f, //tr 2
                -0.5f, -0.5f, 0.0f //bl 3
        };

        int buffer;
        buffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, buffer); //VERTEX BUFFER.

        FloatBuffer positionBuffer = BufferUtils.createFloatBuffer(positions.length);
        positionBuffer.put(positions).flip();

        glBufferData(GL_ARRAY_BUFFER, positionBuffer, GL_STATIC_DRAW);


        // LAYOUT OF THE VERTEX BUFFER INTO INDICES!!

        // Index : Map a vertex attribute to an index to pass it to the fragment shader.
        // Size : Count (How many floats per attribute at index). (Ex: floats for pos: 3, floats for color: 4, floats for texture: 2, etc).
        // Type: Float.
        // Normalized:
        // Stride: Ammount of bytes between each vertex.
        // Pointer: Pretend you only have 1 vertex. What is the offset (in bytes) for the attribute you want?
        int positionSize = 3;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, positionSize* floatSizeBytes, 0);
        glEnableVertexAttribArray(0);


        int ibo; // Index buffer object.
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo); //ELEMENT ARRAY BUFFER PARA INDICES
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);


        // Vertex Arrays. Not to confuse with vertex Buffer.
        // OpenGL specific.

        // Purpose:Not map every time for a different vertex buffer object. VBO.

    }

    public void update( float dt ) {

        // Count: number of indices
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0 ); // Uses an Element/Index Buffer.

        // Index Buffer + Vertex Buffer + drawElements.

    }


}
