package GDIS.engine.program;

import GDIS.engine.entities.EntityManager;
import GDIS.engine.render.scene.Scene;
import GDIS.engine.render.text.Renderer;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public class ProgramGDIS extends SimulationProgram
{
    // input handling
    // saveSystem
    // Resource Loading
    // Splash Screen
    // Debugger

//    private Renderer renderer = new Renderer();

    @Override
    public boolean initialize()
    {
        // TODO: Add splash screen if not in debug mode (get the init state, replace with splash and in the end push the init)

//        renderer.init();
        return true;
    }

    @Override
    public void dispose()
    {

//        renderer.dispose();
    }

    @Override
    public void render(float alpha)
    {
        stateMachine.render(alpha);

        /* Draw FPS, UPS and Context version */
//        int height = renderer.getDebugTextHeight("Context");
//        renderer.drawDebugText("FPS: " + timer.getFps() + " | UPS: " + timer.getUPS(), 5, 5 + height);
//        renderer.drawDebugText("Context: " + (renderer.isDefaultContext() ? "3.2 engine.core" : "2.1"), 5, 5);
    }

    @Override
    public boolean update(float delta)
    {
        return stateMachine.update(delta);
    }

    @Override
    public void handleInput()
    {
        stateMachine.handleInput();
    }
}
