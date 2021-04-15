package educanet;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Game {

    public static final float[] vertices = {
            0.2f, 0.2f, 0.0f, // 0 -> Top right
            0.2f, -0.3f, 0.0f, // 1 -> Bottom right
            -0.2f, -0.3f, 0.0f, // 2 -> Bottom left
            -0.2f, 0.2f, 0.0f, // 3 -> Top left
    };

    private static final float[] colors = {
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
    };

    private static final float[] colorSquareBlue = {
            0.5f, 1.0f, 1.0f,
            0.5f, 1.0f, 1.0f,
            0.5f, 1.0f, 1.0f,
            0.5f, 1.0f, 1.0f,
    };

    private static final float[] colorSquareWhite = {
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
    };

    private static final int[] indices = {
            0, 1, 3, // First triangle
            1, 2, 3 // Second triangle
    };

    private static int squareVaoId;
    private static int squareVboId;
    private static int squareEboId;
    private static int colorsId;
    private static int uniformMatrixLocation;

    private static Matrix4f matrix = new Matrix4f()
            .identity()
            .translate(0.25f, 0.25f, 0.25f);
    // 4x4 -> FloatBuffer of size 16
    private static FloatBuffer matrixFloatBuffer = BufferUtils.createFloatBuffer(16);

    public static void init(long window) {
        // Setup shaders
        Shaders.initShaders();

        // Generate all the ids
        squareVaoId = GL33.glGenVertexArrays();
        squareVboId = GL33.glGenBuffers();
        squareEboId = GL33.glGenBuffers();
        colorsId = GL33.glGenBuffers();

        // Get uniform location
        uniformMatrixLocation = GL33.glGetUniformLocation(Shaders.shaderProgramId, "matrix");

        // Tell OpenGL we are currently using this object (vaoId)
        GL33.glBindVertexArray(squareVaoId);

        // Tell OpenGL we are currently writing to this buffer (eboId)
        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, squareEboId);
        IntBuffer ib = BufferUtils.createIntBuffer(indices.length)
                .put(indices)
                .flip();
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW);

        // Change to VBOs...
        // Tell OpenGL we are currently writing to this buffer (vboId)
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareVboId);

        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
                .put(vertices)
                .flip();

        // Send the buffer (positions) to the GPU
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);

        // Clear the buffer from the memory (it's saved now on the GPU, no need for it here)
        MemoryUtil.memFree(fb);

        // Change to Color...
        // Tell OpenGL we are currently writing to this buffer (colorsId)
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, colorsId);

        FloatBuffer cb = BufferUtils.createFloatBuffer(colors.length)
                .put(colors)
                .flip();

        // Send the buffer (positions) to the GPU
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);

        GL33.glUseProgram(Shaders.shaderProgramId);

        // Sending Mat4 to GPU
        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);

        // Clear the buffer from the memory (it's saved now on the GPU, no need for it here)
        MemoryUtil.memFree(cb);
        MemoryUtil.memFree(fb);
    }

    public static void render(long window) {

        // Draw using the glDrawElements function
        GL33.glBindVertexArray(squareVaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }

    private static float xSpeed = (float) 0.01;
    private static float ySpeed = (float) 0.01;
    private static int jenomJednouProsim = 0;

    public static void update(long window) {



        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
            if (jenomJednouProsim < 1) {
                colors[0] = colorSquareBlue[0];
                colors[3] = colorSquareBlue[3];
                colors[6] = colorSquareBlue[6];
                colors[9] = colorSquareBlue[9];

                FloatBuffer cb = BufferUtils.createFloatBuffer(colors.length).put(colors).flip();
                GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cb, GL33.GL_STATIC_DRAW);
                GL33.glVertexAttribPointer(1, 3, GL33.GL_FLOAT, false, 0, 0);
                GL33.glEnableVertexAttribArray(1);
                MemoryUtil.memFree(cb);

                xSpeed = 0.04f;
                ySpeed = 0.04f;
                System.out.println("SET BLUE COLOR AND INCREASED SPEED!");
            }
            jenomJednouProsim++;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_RELEASE) {
            if (jenomJednouProsim > 1) {
                colors[0] = colorSquareWhite[0];
                colors[3] = colorSquareWhite[3];
                colors[6] = colorSquareWhite[6];
                colors[9] = colorSquareWhite[9];

                FloatBuffer cb = BufferUtils.createFloatBuffer(colors.length).put(colors).flip();
                GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cb, GL33.GL_STATIC_DRAW);
                GL33.glVertexAttribPointer(1, 3, GL33.GL_FLOAT, false, 0, 0);
                GL33.glEnableVertexAttribArray(1);
                MemoryUtil.memFree(cb);

                xSpeed = 0.01f;
                ySpeed = 0.01f;
                System.out.println("SET WHITE COLOR AND SET SPEED BACK TO NORMAL!");
            }
            jenomJednouProsim = 0;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) { //dopredu
            matrix = matrix.translate(0f, ySpeed, 0f);
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) { //doleva
            matrix = matrix.translate(-xSpeed, 0f, 0f);
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) { //dozadu
            matrix = matrix.translate(0f, -ySpeed, 0f);
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) { //doprava
            matrix = matrix.translate(xSpeed, 0f, 0f);
        }

        // TODO: Send to GPU only if position updated
        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);
    }

}
