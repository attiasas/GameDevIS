package GDIS.engine.entities;

import GDIS.engine.render.orientation.Transform;
import GDIS.tools.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created By: Assaf, On 02/11/2021
 * Description:
 */
public class Entity
{
    public final long id;
    private EntityManager manager;

    protected Transform transform;
    protected Map<String, Component> components;

    protected Entity(long id, EntityManager manager)
    {
        this.id = id;
        this.manager = manager;

        this.transform = new Transform();
        this.components = new HashMap<>();
    }

    public void registerComponent(Component component)
    {
        String name = component.getName();
        if(components.containsKey(name))
            throw new IllegalArgumentException("Component '" + name + "' already exists");

        components.put(name,component);
    }

    public void update(float delta)
    {
        for(Component component : components.values())
        {
            component.update(delta);
        }

        transform.update();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id == entity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Transform getTransform()
    {
        return transform;
    }
}
