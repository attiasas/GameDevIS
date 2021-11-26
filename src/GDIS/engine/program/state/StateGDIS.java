package GDIS.engine.program.state;

import GDIS.engine.ConfigurationGDIS;
import GDIS.engine.entities.EntityManager;
import GDIS.engine.entities.render.EntityRenderer;
import GDIS.engine.render.RenderManager;
import GDIS.engine.render.renderer.data.*;
import GDIS.engine.render.renderer.users.BaseShaderProgram;
import GDIS.engine.render.scene.Camera;
import GDIS.engine.render.scene.DefaultCamera;
import GDIS.engine.render.scene.Projections;
import GDIS.engine.render.scene.Scene;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public abstract class StateGDIS implements State
{
    protected Scene relativeScene; // TODO -> Combine and transfer into GUIManager
    protected Scene absoluteScene;

    protected Camera mainCamera;
    protected Scene scene;

    protected ConfigurationGDIS configuration;
    protected StateMachine stateMachine;
    protected RenderManager renderManager;
    protected EntityManager entityManager;

    public abstract boolean initialize();
    public abstract void dispose();
    public abstract boolean update(float delta);

    public void setEnvVars(StateMachine stateMachine, ConfigurationGDIS configuration)
    {
        this.stateMachine = stateMachine;
        this.configuration = configuration;
    }

    public void setStateRenderers(RenderManager manager)
    {
        manager.addRenderers(new EntityRenderer(scene));

        manager.addRenderers(new DataRenderer(4096,new DataShaderProgram(),relativeScene));
        manager.addRenderers(new DataRenderer(4096,new DataShaderProgram(),absoluteScene));
        manager.addRenderers(new DataRenderer(4096,new DataShaderProgram(),absoluteScene));
    }

    @Override
    public boolean updateState(float delta)
    {
        entityManager.update(delta);

        return update(delta);
    }

    @Override
    public void enter()
    {
        // Main Scene
        mainCamera = new Camera(Projections.ratio(configuration.getProgramWidth(),configuration.getProgramHeight()));
        scene = new Scene(mainCamera);

        // GUI Scene
        relativeScene = new Scene(new DefaultCamera(Projections.ratio(configuration.getProgramWidth(),configuration.getProgramHeight())));
        absoluteScene = new Scene(new DefaultCamera(Projections.absolute(configuration.getProgramWidth(),configuration.getProgramHeight())));

        renderManager = new RenderManager();
        setStateRenderers(renderManager);

        entityManager = new EntityManager();

        if(!initialize())
        {
            stateMachine.popCurrentState();
        }
    }

    @Override
    public void exit() {
        dispose();

        renderManager.dispose();
    }

    @Override
    public void render(float alpha)
    {
        renderManager.renderToBuffer(alpha);
    }
}
