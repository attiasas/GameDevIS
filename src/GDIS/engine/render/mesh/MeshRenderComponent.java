package GDIS.engine.render.mesh;

import GDIS.engine.openglWrapper.Texture;
import GDIS.engine.render.orientation.Orientation;
import GDIS.engine.render.renderer.IRenderer;
import GDIS.engine.render.renderer.data.IDataRenderer;
import GDIS.engine.render.renderer.users.VaoUserRenderer;
import GDIS.tools.Component;


/**
 * Created By: Assaf, On 02/11/2021
 * Description:
 */
public class MeshRenderComponent extends Component
{
    private final String name;

    private IRenderer renderer;
    private Mesh mesh;
    private Orientation orientation;
    private Texture texture;

    public MeshRenderComponent(String name, IRenderer renderer, Mesh mesh, Orientation orientation, Texture texture)
    {
        this.name = name;
        this.renderer = renderer;
        this.mesh = mesh;
        this.orientation = orientation;
        this.texture = texture;

        if(renderer instanceof VaoUserRenderer)
        {
            this.mesh.linkWithProgram(renderer.getProgram());
        }
    }

    public MeshRenderComponent(IRenderer renderer, Mesh mesh, Orientation orientation, Texture texture)
    {
        this("RENDER",renderer, mesh, orientation, texture);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(float delta)
    {
        if(isActive())
        {
//            System.out.print("\r\b Mesh Render Component Active, render (times rendered=" + test++ + ")");
            if(renderer instanceof IDataRenderer)
            {
//                System.out.println("IDataRenderer");
                ((IDataRenderer) renderer).render(mesh.getData(),orientation,texture); // batch. not using the vao
            }
            else if(renderer instanceof VaoUserRenderer)
            {
//                System.out.println("VaoUserRenderer");
                ((VaoUserRenderer) renderer).render(mesh,orientation,texture); // using the vao to render
            }
        }
    }
}
