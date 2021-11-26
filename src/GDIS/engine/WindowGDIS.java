package GDIS.engine;

import GDIS.engine.openglWrapper.Window;
import GDIS.engine.program.SimulationProgram;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public class WindowGDIS
{
    private ConfigurationGDIS configuration;

    private Window window;
    private GLFWErrorCallback errorCallback;

    private SimulationProgram program = null;

    private boolean running = false;

    public WindowGDIS(ConfigurationGDIS configuration) {
        this.configuration = configuration;
    }

    public void setProgram(SimulationProgram program)
    {
        this.program = program;
        this.program.setWindow(this);
    }

    public void show() {
        initialize();
        program.run();
        dispose();
    }

    public boolean isClosing() {
        return this.window.isClosing();
    }

    public ConfigurationGDIS getConfiguration() {
        return configuration;
    }

    private void initialize()
    {
        if(this.configuration == null || this.program == null)
            throw new IllegalStateException("Configurations/program has not been set");
        if(running)
            throw new IllegalStateException("Window is already running");

        /* Set error callback */
        errorCallback = GLFWErrorCallback.createPrint();
        glfwSetErrorCallback(errorCallback);

        /* Initialize GLFW */
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW!");
        }

        window = new Window(configuration.getProgramWidth(),configuration.getProgramHeight(),configuration.getTitle(),configuration.isVSync());

        running = true;
    }

    private void dispose()
    {
        /* Release window and its callbacks */
        window.destroy();

        /* Terminate GLFW and release the error callback */
        glfwTerminate();
        errorCallback.free();

        running = false;
    }

    public void updateScreen()
    {
        window.update();
    }

    public boolean isVSyncEnabled() {
        return window.isVSyncEnabled();
    }
}
