package GDIS.engine.render.renderer.users;

import GDIS.engine.openglWrapper.Texture;
import GDIS.engine.openglWrapper.basics.VaoFormat;
import GDIS.engine.openglWrapper.basics.VaoUser;
import GDIS.engine.openglWrapper.shaders.AdvShaderProgram;
import GDIS.engine.render.orientation.Orientation;
import GDIS.engine.render.scene.ICamera;
import GDIS.engine.render.scene.Scene;
import GDIS.tools.debugger.Logger;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public class BaseRenderer implements VaoUserRenderer
{
    private int instanceCount = 0;
    private int drawCallCount = 0;

    private BaseShaderProgram program;
    private Map<Texture, Map<VaoUser, List<Orientation>>> objectsToRender;

    protected Scene scene;

    public BaseRenderer(BaseShaderProgram program, Scene scene)
    {
        this.program = program;
        this.scene = scene;

        this.program.link();

        this.objectsToRender = new HashMap<>();
    }

    @Override
    public void render(VaoUser vaoUser, Orientation orientation, Texture texture)
    {
        if(!this.objectsToRender.containsKey(texture))
            this.objectsToRender.put(texture,new HashMap<>());

        if(!this.objectsToRender.get(texture).containsKey(vaoUser))
            this.objectsToRender.get(texture).put(vaoUser, new ArrayList<>());

        this.objectsToRender.get(texture).get(vaoUser).add(orientation);
        instanceCount++;
    }

    @Override
    public void prepare()
    {
        this.program.use();
        this.program.loadSamplerUnit(0);
    }

    @Override
    public void finish()
    {
        Logger.get(getClass()).log(Logger.Level.INFO, "[ID=" + program.getId() + "] Rendered " + instanceCount + " objects [#RenderCalls=" + drawCallCount + "]");
        objectsToRender.clear();

        this.program.release();

        instanceCount = 0;
        drawCallCount = 0;
    }

    @Override
    public void renderToBuffer(int bufferId, float alpha)
    {
        this.program.loadView(this.scene.getCamera().getViewMatrix(alpha));
        this.program.loadProjection(this.scene.getCamera().getProjectionMatrix());

        VaoFormat format = program.getProgramFormat();

        for(Texture texture : objectsToRender.keySet())
        {
            if(texture != null) texture.bind();

            for(VaoUser vaoUser : objectsToRender.get(texture).keySet())
            {
                vaoUser.use();
                format.enableVaoAttributes();

                List<Orientation> toRenderWithMesh = objectsToRender.get(texture).get(vaoUser);
                for(Orientation orientation : toRenderWithMesh)
                {
                    this.program.loadOrientation(orientation.getOrientation(alpha));

                    int indicesCount = vaoUser.getIndicesCount();
                    if(indicesCount > 0)
                    {
                        glDrawElements(GL_TRIANGLES, indicesCount, GL_UNSIGNED_INT, 0);
                    }
                    else
                    {
                        glDrawArrays(GL_TRIANGLES, 0, vaoUser.getVerticesCount());
                    }
                    drawCallCount++;
                }

                format.disableVaoAttributes();
                vaoUser.release();
            }

            if(texture != null) texture.unbind();
        }
    }

    @Override
    public AdvShaderProgram getProgram() {
        return program;
    }

    @Override
    public void delete() {
        program.delete();
    }
}
