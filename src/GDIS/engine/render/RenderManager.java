package GDIS.engine.render;

import GDIS.engine.render.renderer.IRenderer;
import GDIS.engine.render.scene.Scene;
import GDIS.tools.debugger.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public class RenderManager
{
    private List<IRenderer> rendererList;

    public RenderManager()
    {
        rendererList = new ArrayList<>();
    }

    public void addRenderers(IRenderer... renderers)
    {
        for(IRenderer renderer : renderers)
        {
            rendererList.add(renderer);
        }
    }

    public void renderToBuffer(float alpha)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Logger.get(getClass()).log(Logger.Level.INFO, "-- Start Render -------");
        for(int i = 0; i < rendererList.size(); i++)
        {
            IRenderer renderer = rendererList.get(i);

            renderer.prepare();
            renderer.renderToBuffer(0,alpha);

            // TODO: can readFrom renderer #drawCalls and #instances drawn, can use in debug window or report to debugger/Reporter, has to be before finish (resets there..) maybe add to interface...
            renderer.finish();
        }
        Logger.get(getClass()).log(Logger.Level.INFO, "-- End Render ---------");
    }

    public void dispose()
    {
        for(IRenderer renderer : rendererList)
        {
            renderer.delete();
        }
    }

    public IRenderer getRenderer(int i)
    {
        return rendererList.get(i);
    }
}
