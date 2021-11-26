package GDIS.engine.program;

import GDIS.engine.ConfigurationGDIS;
import GDIS.engine.WindowGDIS;
import GDIS.engine.program.state.StateMachine;
import GDIS.tools.Counter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public abstract class SimulationProgram
{
    protected WindowGDIS window;
    protected ConfigurationGDIS configuration;
    protected ProgramTimer timer;

    protected StateMachine stateMachine;

    private boolean running;

    public abstract boolean initialize();
    public abstract void dispose();
    public abstract void render(float alpha);
    public abstract boolean update(float delta);
    public abstract void handleInput();

    public SimulationProgram()
    {
        stateMachine = new StateMachine(this);
    }

    public void mainLoop()
    {
        float alpha;

        while (running && !window.isClosing())
        {
            timer.updateLogicLoop();
//            System.out.println("Start Main [time = " + timer.getTime() + "]. Running = " + running + ", ProgramClosing = " + renderManager.isProgramClosing());
            handleInput();

            if (this.configuration.isVariableTimeLoop()) {
                running = update(timer.getDelta());
                timer.updateUPS();

                render(1f);
                timer.updateFps();
            }
            else // FixedTimeLoop (Stable marked UPS)
            {
                Counter accumulator = timer.getAccumulator();

                /* Update game and timer UPS if enough time has passed */
                while (accumulator.isMarkReached()) {
                    running = update(accumulator.getMark());
                    timer.updateUPS();
                }

                /* Calculate alpha value for interpolation */
                alpha = accumulator.getCount() / accumulator.getMark();

                /* Render game and update timer FPS */
                render(alpha);
                timer.updateFps();
            }
//            System.out.println("End Main [time = " + timer.getTime() + "]. Running = " + running + ", ProgramClosing = " + renderManager.isProgramClosing());

            /* Update window to show the new screen */
            window.updateScreen();

            /* Synchronize if v-sync is disabled */
            if (!window.isVSyncEnabled()) {
                sync(this.configuration.getFPS());
            }

        }
    }

    /**
     * Synchronizes the game at specified frames per second.
     *
     * @param fps Frames per second
     */
    public void sync(int fps) {
        double lastLoopTime = timer.getTime();
        double now = timer.getTime();
        float targetTime = 1f / fps;

        while (now - lastLoopTime < targetTime) {
            Thread.yield();

            /* This is optional if you want your game to stop consuming too much
             * CPU but you will loose some accuracy because Thread.sleep(1)
             * could sleep longer than 1 millisecond */
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimulationProgram.class.getName()).log(Level.SEVERE, null, ex);
            }

            now = timer.getTime();
        }
    }

    public void setWindow(WindowGDIS window)
    {
        this.window = window;
        this.configuration = window.getConfiguration();
    }

    public void run()
    {
        if(this.window == null || this.configuration == null)
            throw new IllegalStateException("Window has not been set");

        timer = new ProgramTimer(1f / configuration.getUPS());

        running = initialize();

        timer.init();
        stateMachine.initialize();

        mainLoop();

        dispose();
    }

    public StateMachine getStateMachine() {
        return stateMachine;
    }

    public ConfigurationGDIS getConfiguration() {
        return configuration;
    }
}
