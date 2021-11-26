package GDIS.engine.render.scene;

import org.joml.Matrix4f;

/**
 * Created By: Assaf, On 20/10/2021
 * Description:
 */
public interface ICamera
{
    Matrix4f getViewMatrix(float alpha);
    Matrix4f getProjectionMatrix();
    void update(float delta);
}
