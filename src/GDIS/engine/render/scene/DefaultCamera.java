package GDIS.engine.render.scene;

import org.joml.Matrix4f;

/**
 * Created By: Assaf, On 20/10/2021
 * Description:
 */
public class DefaultCamera implements ICamera
{
    private Matrix4f viewMatrix;
    private Matrix4f projectionMatrix;

    public DefaultCamera(Matrix4f projectionMatrix)
    {
        setViewMatrix(new Matrix4f());
        this.projectionMatrix = projectionMatrix;
    }

    @Override
    public Matrix4f getViewMatrix(float alpha) {
        return viewMatrix;
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setViewMatrix(Matrix4f viewMatrix) {
        this.viewMatrix = viewMatrix;
    }

    @Override
    public void update(float delta) { }
}
