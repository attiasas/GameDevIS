package GDIS.engine.program;

import GDIS.tools.Counter;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * Created By: Assaf, On 26/10/2021
 * Description:
 */
public class ProgramTimer
{
    private int fps;

    private int ups;

    private int fpsCount;

    private int upsCount;

    private Counter oneSecTimeCounter;

    private double lastTimeStamp;

    private Counter accumulator;

    private float delta;

    public ProgramTimer(float interval)
    {
        oneSecTimeCounter = new Counter(1);
        accumulator = new Counter(interval);
    }

    public void init()
    {
        oneSecTimeCounter.setReplay(true);
        accumulator.setReplay(true);

        lastTimeStamp = getTime();
    }


    // call first thing in a game loop to update the time elapsed and update accumulator (to prepare alpha) if needed
    public void updateLogicLoop()
    {
        double time = getTime();
        float delta = (float) (time - lastTimeStamp);
        lastTimeStamp = time;

        oneSecTimeCounter.update(delta);
        accumulator.update(delta);

        if(oneSecTimeCounter.isMarkReached())
        {
            fps = fpsCount;
            fpsCount = 0;

            ups = upsCount;
            upsCount = 0;
        }

        this.delta = delta;
    }

    public float getDelta()
    {
        return this.delta;
    }

    public void updateFps()
    {
        this.fpsCount += 1;
    }

    public void updateUPS()
    {
        this.upsCount += 1;
    }

    public int getFps()
    {
        return fps > 0 ? fps : fpsCount;
    }

    public int getUPS() {
        return ups > 0 ? ups : upsCount;
    }

    public Counter getAccumulator()
    {
        return accumulator;
    }

    /**
     * Returns the time elapsed since <code>glfwInit()</code> in seconds.
     *
     * @return System time in seconds
     */
    public double getTime()
    {
        return glfwGetTime();
    }
}
