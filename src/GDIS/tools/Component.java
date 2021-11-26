package GDIS.tools;

/**
 * Created By: Assaf, On 02/11/2021
 * Description:
 */
public abstract class Component
{
    private boolean active = true;

    public boolean isActive() { return active; }

    public abstract String getName();
    public abstract void update(float delta);
}
