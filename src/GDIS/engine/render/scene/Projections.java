package GDIS.engine.render.scene;

import org.joml.Matrix4f;

/**
 * Created By: Assaf, On 23/11/2021
 * Description:
 */
public class Projections
{

    public static Matrix4f ratio(float aspectRatio)
    {
        Matrix4f projection = new Matrix4f();

//        projection.perspective()
//        projection.frustum()

        projection.ortho(-aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);

        return projection;
    }

    public static Matrix4f ratio(int width, int height)
    {
        float aspectRatio = (float)width / (float)height;
        return ratio(aspectRatio);
    }

    public static Matrix4f absolute(int width, int height)
    {
        Matrix4f projection = new Matrix4f();
        projection.ortho(0f, width, 0f, height, -1f, 1f);
        return projection;
    }
}
