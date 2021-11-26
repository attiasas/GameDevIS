package GDIS.engine.render.data;

/**
 * Created By: Assaf, On 15/11/2021
 * Description:
 */
public class SmartVaoData extends VaoData
{

    public VaoData setColor(Color color)
    {
        Vertex[] vertices = getVertices();
        for(Vertex v : vertices)
        {
            v.setColors(color);
        }
        return this;
    }


}
