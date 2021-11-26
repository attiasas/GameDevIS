package GDIS.engine.render.orientation;

import org.joml.Matrix4f;

/**
 * Created By: Assaf, On 30/10/2021
 * Description:
 */
public interface Orientation
{
    Matrix4f getOrientation(float alpha);
    default Matrix4f getOrientation()
    {
        return getOrientation(1f);
    }

}
