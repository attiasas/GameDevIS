package GDIS.engine.openglWrapper.shaders;

import GDIS.engine.openglWrapper.basics.VaoFormat;
import GDIS.engine.openglWrapper.shaders.uniforms.Uniform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Assaf, On 30/10/2021
 * Description:
 */
public class AdvShaderProgram extends ShaderProgram
{
    private VaoFormat format;
    private boolean linked = false;

    private List<Uniform> uniforms = new ArrayList<>(); // TODO: add before and storeLocation after link (override)

    public AdvShaderProgram() { }
    public AdvShaderProgram(VaoFormat format)
    {
        setVaoFormat(format);
    }

    @Override
    public void link() {
        super.link();

        if (!linked)
            throw new IllegalStateException("GDIS Advance Shader Program is not linked with format");

        this.format.linkToProgram(getId());

        setAllUniformLocations(uniforms);
    }

    @Override
    public void use()
    {
        if(!linked)
            throw new IllegalStateException("GDIS Advance Shader Program is not linked with format");

        super.use();
    }

    @Override
    public void release()
    {
        if(!linked)
            throw new IllegalStateException("GDIS Advance Shader Program is not linked with format");

        super.release();
    }

    public void attachUniforms(Uniform... uniforms)
    {
        for (Uniform uniform : uniforms)
        {
            this.uniforms.add(uniform);
        }
    }

    public void setVaoFormat(VaoFormat format)
    {
        this.format = format;
        this.linked = true;
    }

    public VaoFormat getProgramFormat()
    {
        return this.format;
    }

    public boolean isLinked() {
        return linked;
    }
}
