package GDIS.engine.openglWrapper.shaders.uniforms;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class UniformMatrix extends Uniform {
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public UniformMatrix(String name) {
		super(name);
	}
	
	public void loadMatrix(Matrix4f matrix)
	{

//		try (MemoryStack stack = MemoryStack.stackPush()) {
//			FloatBuffer buffer = stack.mallocFloat(4 * 4);
//			buffer = matrix.get(buffer);
////			buffer.flip();
////            value.toBuffer(buffer);
//			glUniformMatrix4fv(super.getLocation(), false, buffer);
//		}
		matrix.get(matrixBuffer);
//		matrixBuffer.flip();
		glUniformMatrix4fv(super.getLocation(), false, matrixBuffer);
	}
}
