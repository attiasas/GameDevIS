package GDIS.engine.render.renderer.users;

import GDIS.engine.openglWrapper.Texture;
import GDIS.engine.openglWrapper.basics.VaoUser;
import GDIS.engine.render.orientation.Orientation;
import GDIS.engine.render.renderer.IRenderer;

/**
 * Created By: Assaf, On 30/10/2021
 * Description:
 */
public interface VaoUserRenderer extends IRenderer
{
     void render(VaoUser vaoUser, Orientation orientation, Texture texture);
}
