package GDIS.tools;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public class Counter
{
    private float startCount;

    private float mark;
    private float count;

    private boolean replay;

    private boolean addToCount;

    private boolean resetOnReplay;

    public Counter(boolean resetOnReplay, boolean replay, boolean addToCount, float startCount, float mark)
    {
        setResetOnReplay(resetOnReplay);
        setReplay(replay);
        setCountDirection(addToCount);
        setStartCount(startCount);
        reset(mark);
    }

    public Counter(boolean replay, boolean addToCount, float startCount, float mark)
    {
        this(false,replay,addToCount,startCount,mark);
    }

    public Counter(boolean countUp, float startCount, float mark)
    {
        this(false, countUp,startCount,mark);
    }

    public Counter(float startCount, float mark)
    {
        this(true,startCount,mark);
    }

    public Counter(float mark)
    {
        this(0,mark);
    }

    public Counter()
    {
        this(1);
    }

    public void setReplay(boolean replay)
    {
        this.replay = replay;
    }

    public void setCountDirection(boolean up)
    {
        this.addToCount = up;
    }

    public void setStartCount(float count)
    {
        this.startCount = count;
    }

    public void setResetOnReplay(boolean resetOnReplay) {
        this.resetOnReplay = resetOnReplay;
    }

    public void reset(float mark)
    {
        this.count = this.startCount;
        this.mark = mark;
    }

    public void update(float delta)
    {
        this.count += addToCount ? delta : -delta;
    }

    public float getCount()
    {
        return this.count;
    }

    public boolean isMarkReached()
    {
        boolean reached = addToCount ? this.count >= this.mark : this.count <= this.mark;

        if(reached && replay)
        {
            if(resetOnReplay) reset(this.mark);
            else this.count = this.startCount + (addToCount ? (this.count - this.mark) : -(this.count - this.mark));
        }

        return reached;
    }

    public float getMark() {
        return mark;
    }
}
