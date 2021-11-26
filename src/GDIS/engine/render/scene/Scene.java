package GDIS.engine.render.scene;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public class Scene
{
    private ICamera camera;

    public Scene(ICamera camera)
    {
        setCamera(camera);
    }

    public ICamera getCamera() {
        return camera;
    }

    public void setCamera(ICamera camera) {
        this.camera = camera;
    }
}
