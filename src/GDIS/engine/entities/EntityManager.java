package GDIS.engine.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created By: Assaf, On 02/11/2021
 * Description:
 */
public class EntityManager
{
    private AtomicLong idCounter = new AtomicLong(0);
    private Map<Long, Entity> entityMap;

    public EntityManager()
    {
        entityMap = new HashMap<>();
    }

    public Entity createEntity()
    {
        long id = idCounter.getAndIncrement();
        Entity entity = new Entity(id,this);

        entityMap.put(id,entity);

        return entity;
    }

    public void update(float delta)
    {
        for(Entity entity : entityMap.values())
        {
            entity.update(delta);
        }
    }
}
