package GDIS.engine.render.renderer;

import GDIS.engine.openglWrapper.shaders.AdvShaderProgram;
import GDIS.engine.render.scene.ICamera;

/**
 * Created By: Assaf, On 30/10/2021
 * Description:
 */
public interface IRenderer {

    void prepare(); // called just before renderToBuffer - prepare env for the render process (binds, special flags..)
    void finish(); // called just after renderToBuffer - clear env for next renderer (oposite operation to prepare)
    void renderToBuffer(int bufferId, float alpha); // actual render process into the buffer

    AdvShaderProgram getProgram(); // gets the shader program that the render uses

    void delete();
}
