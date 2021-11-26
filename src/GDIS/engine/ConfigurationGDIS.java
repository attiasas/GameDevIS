package GDIS.engine;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public class ConfigurationGDIS
{
    // information for the Window Class
    private int width = 1280;
    private int height = 720;
    private String title = "GDIS-Engine";
    private boolean vSync;

    // information for the Program Class
    private int ups = -1;
    private int fps = 2000;


    public int getProgramWidth()
    {
        return width;
    }

    public int getProgramHeight()
    {
        return height;
    }

    public String getTitle()
    {
        return title;
    }

    public boolean isVSync()
    {
        return vSync;
    }

    public ConfigurationGDIS setWidth(int width)
    {
        this.width = width;
        return this;
    }

    public ConfigurationGDIS setHeight(int height)
    {
        this.height = height;
        return this;
    }

    public ConfigurationGDIS setTitle(String title)
    {
        this.title = title;
        return this;
    }

    public ConfigurationGDIS setVSync(boolean vSync)
    {
        this.vSync = vSync;
        return this;
    }

    public ConfigurationGDIS setUPS(int ups) {
        this.ups = ups;
        return this;
    }

    public ConfigurationGDIS setFPS(int fps) {
        this.fps = fps;
        return this;
    }

    public int getFPS() {
        return this.fps;
    }

    public int getUPS() {
        return this.ups;
    }

    public boolean isVariableTimeLoop() {
        return this.ups <= 0;
    }
}
