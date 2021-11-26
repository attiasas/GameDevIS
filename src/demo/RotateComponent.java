package demo;

import GDIS.engine.entities.Entity;
import GDIS.tools.Component;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public class RotateComponent extends Component {

    private final float angelPerSecondZ = 90f;
    private final float angelPerSecondY = 45f;
    private final float angelPerSecondX = 0;

    private Entity entity;

    @Override
    public String getName() {
        return "MOVEMENT";
    }

    public RotateComponent(Entity entity)
    {
        this.entity = entity;
    }

    @Override
    public void update(float delta) {
        entity.getTransform().setChangeInRotation(delta * angelPerSecondX,delta * angelPerSecondY,delta * angelPerSecondZ);
    }
}
