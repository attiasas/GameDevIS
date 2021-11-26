package GDIS.engine.render.renderer.users;

import GDIS.engine.openglWrapper.basics.Attribute;
import GDIS.engine.openglWrapper.basics.VaoFormat;
import GDIS.engine.openglWrapper.shaders.AdvShaderProgram;
import GDIS.engine.openglWrapper.shaders.Shader;
import GDIS.engine.openglWrapper.shaders.uniforms.UniformMatrix;
import GDIS.engine.openglWrapper.shaders.uniforms.UniformSampler;
import GDIS.engine.render.data.Vertex;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public class BaseShaderProgram extends AdvShaderProgram {

    private Shader vertexShader = Shader.loadVertexShader("resources/default_entity.vert");
    private Shader fragmentShader = Shader.loadFragmentShader("resources/default_entity.frag");

    private UniformSampler uniformSampler = new UniformSampler("texImage");

    private UniformMatrix uniformTransform = new UniformMatrix("transform");
    private UniformMatrix uniformViewMatrix = new UniformMatrix("view");
    private UniformMatrix uniformProjectionMatrix = new UniformMatrix("projection");

    private Attribute positionAttribute = new Attribute("position",3,GL_FLOAT, Vertex.DataType.POSITION.ordinal());
    private Attribute colorAttribute = new Attribute("color",4,GL_FLOAT, Vertex.DataType.COLOR.ordinal());
    private Attribute textureCoordsAttribute = new Attribute("texcoord",2,GL_FLOAT, Vertex.DataType.TEXTURE_COORDS.ordinal()).setOptional(new float[]{-1,-1});

    public BaseShaderProgram()
    {
        this(new VaoFormat());
    }

    public BaseShaderProgram(VaoFormat format)
    {
        /*Set shaders*/
        attachShaders(vertexShader,fragmentShader);
        /*Set format (input attributes arrange inside the vao for this program)*/
        format.addVbo(positionAttribute);
        format.addVbo(colorAttribute);
        format.addVbo(textureCoordsAttribute);
        setVaoFormat(format);
        /*Set Output*/
        attachOutputAttributes(0,"fragColor");
        /*Set Uniforms*/
        attachUniforms(uniformSampler,uniformTransform,uniformViewMatrix,uniformProjectionMatrix);
    }

    public void loadSamplerUnit(int textureUnit) { uniformSampler.loadTexUnit(textureUnit); }

    public void loadView(Matrix4f viewMatrix) { uniformViewMatrix.loadMatrix(viewMatrix); }

    public void loadProjection(Matrix4f projectionMatrix) { uniformProjectionMatrix.loadMatrix(projectionMatrix); }

    public void loadOrientation(Matrix4f transformation) { uniformTransform.loadMatrix(transformation); }
}
