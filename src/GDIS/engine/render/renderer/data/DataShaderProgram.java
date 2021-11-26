package GDIS.engine.render.renderer.data;

import GDIS.engine.openglWrapper.basics.Attribute;
import GDIS.engine.openglWrapper.basics.VaoFormat;
import GDIS.engine.openglWrapper.shaders.AdvShaderProgram;
import GDIS.engine.openglWrapper.shaders.Shader;
import GDIS.engine.openglWrapper.shaders.uniforms.UniformMatrix;
import GDIS.engine.openglWrapper.shaders.uniforms.UniformSampler;
import GDIS.engine.render.data.Vertex;
import org.joml.Matrix4f;

import static GDIS.engine.render.renderer.data.DataRenderer.TRANSFORM_ATTRIBUTE_NAME;
import static org.lwjgl.opengl.GL11.GL_FLOAT;

/**
 * Created By: Assaf, On 13/11/2021
 * Description:
 */
public class DataShaderProgram extends AdvShaderProgram
{
    private Shader vertexShader = Shader.loadVertexShader("resources/default_data.vert");
    private Shader fragmentShader = Shader.loadFragmentShader("resources/default_data.frag");

    private UniformSampler uniformSampler = new UniformSampler("texImage");
    private UniformMatrix uniformTransform = new UniformMatrix(TRANSFORM_ATTRIBUTE_NAME.toString());
    private UniformMatrix uniformViewMatrix = new UniformMatrix("view");
    private UniformMatrix uniformProjectionMatrix = new UniformMatrix("projection");

    private Attribute positionAttribute = new Attribute("position",3,GL_FLOAT, Vertex.DataType.POSITION.ordinal());
    private Attribute colorAttribute = new Attribute("color",4,GL_FLOAT, Vertex.DataType.COLOR.ordinal());
    private Attribute textureCoordsAttribute = new Attribute("texcoord",2,GL_FLOAT, Vertex.DataType.TEXTURE_COORDS.ordinal()).setOptional(new float[]{-1,-1});
//    public Attribute transformAttribute = new MatrixAttribute(TRANSFORM_ATTRIBUTE_NAME);

    public DataShaderProgram()
    {
        this(new VaoFormat());
    }

    public DataShaderProgram(VaoFormat format)
    {
        attachShaders(vertexShader);
        attachShaders(fragmentShader);

        attachOutputAttributes(0, "fragColor");

        format.addVbo(positionAttribute, colorAttribute, textureCoordsAttribute);

        // TODO: in for mat needs to split into 4 and will need to have the diff location ? maybe add MatrixAttribute extends Attribute?
        //      loader.addInstanceAttribute(quad.getVaoID(),vbo,1,4,INSTANCE_DATA_LENGTH,4);
        //		loader.addInstanceAttribute(quad.getVaoID(),vbo,2,4,INSTANCE_DATA_LENGTH,8);
        //		loader.addInstanceAttribute(quad.getVaoID(),vbo,3,4,INSTANCE_DATA_LENGTH,12);
        //		loader.addInstanceAttribute(quad.getVaoID(),vbo,4,4,INSTANCE_DATA_LENGTH,16);
        //      when assing --> skip from 1 to 5 (1,2,3,4 are the locations....)
        //        super.bindAttribute(0, "position");
        //        super.bindAttribute(1, "modelViewMatrix");
        //        super.bindAttribute(5, "texOffsets");
        //        super.bindAttribute(6, "blendFactor");
//        format.addVbo(transformAttribute);

        setVaoFormat(format);

        attachUniforms(uniformSampler,uniformTransform,uniformViewMatrix,uniformProjectionMatrix);
    }

    public void loadSamplerUnit(int textureUnit) { uniformSampler.loadTexUnit(textureUnit); }

    public void loadView(Matrix4f viewMatrix) { uniformViewMatrix.loadMatrix(viewMatrix); }

    public void loadProjection(Matrix4f projectionMatrix) { uniformProjectionMatrix.loadMatrix(projectionMatrix); }

    public void loadOrientation(Matrix4f transformation) { uniformTransform.loadMatrix(transformation); }
}
