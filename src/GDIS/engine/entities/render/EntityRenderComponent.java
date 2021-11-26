package GDIS.engine.entities.render;

import GDIS.engine.entities.Entity;
import GDIS.engine.openglWrapper.Texture;
import GDIS.engine.render.mesh.Mesh;
import GDIS.engine.render.mesh.MeshRenderComponent;
import GDIS.engine.render.renderer.IRenderer;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public class EntityRenderComponent extends MeshRenderComponent
{
    private Entity entity;

    public EntityRenderComponent(Entity entity,IRenderer renderer, Mesh mesh, Texture texture)
    {
        super(renderer, mesh, entity.getTransform(), texture);
        this.entity = entity;
    }

    public EntityRenderComponent(Entity entity,IRenderer renderer, Mesh mesh)
    {
       this(entity,renderer,mesh,null);
    }
}
