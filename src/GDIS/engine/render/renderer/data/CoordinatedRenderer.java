package GDIS.engine.render.renderer.data;

import GDIS.engine.openglWrapper.Texture;
import GDIS.engine.render.data.VaoData;
import GDIS.engine.render.orientation.Orientation;

/**
 * Created By: Assaf, On 09/11/2021
 * Description:
 */
public abstract class CoordinatedRenderer
{
    private IDataRenderer dumpRenderer;

    public CoordinatedRenderer() { this(null); }

    public CoordinatedRenderer(IDataRenderer dumpRenderer)
    {
        this.dumpRenderer = dumpRenderer;
    }

    public void setDumpRenderer(IDataRenderer dumpRenderer)
    {
        this.dumpRenderer = dumpRenderer;
    }

    public void render(VaoData data, Orientation orientation, Texture texture)
    {
        if(dumpRenderer == null)
            throw new IllegalStateException("BatchRenderer has not been set.");

        dumpRenderer.render(data,orientation,texture);
    }
}
