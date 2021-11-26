package GDIS.engine.program.state;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public interface State
{
    public void enter();
    public void exit();
    public void handleInput();
    public boolean updateState(float delta);
    public void render(float alpha);
}
