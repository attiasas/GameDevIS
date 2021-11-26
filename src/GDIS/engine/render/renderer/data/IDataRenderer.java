package GDIS.engine.render.renderer.data;

import GDIS.engine.openglWrapper.Texture;
import GDIS.engine.render.data.VaoData;
import GDIS.engine.render.orientation.Orientation;
import GDIS.engine.render.renderer.IRenderer;

/**
 * Created By: Assaf, On 30/10/2021
 * Description:
 */
public interface IDataRenderer extends IRenderer
{
    void render(VaoData data, Orientation orientation, Texture texture);
}
