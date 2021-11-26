package GDIS.engine.entities.render;

import GDIS.engine.render.renderer.users.BaseShaderProgram;
import GDIS.engine.render.renderer.users.BaseRenderer;
import GDIS.engine.render.scene.Scene;
import org.joml.Matrix4f;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public class EntityRenderer extends BaseRenderer
{
    public EntityRenderer(Scene scene)
    {
        super(new EntityShaderProgram(),scene);
    }

    public EntityRenderer(BaseShaderProgram program, Scene scene) {
        super(program,scene);
    }
}
