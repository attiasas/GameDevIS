package GDIS.engine.render.orientation;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created By: Assaf, On 30/10/2021
 * Description:
 */
public class Transform implements Orientation {

    private Vector3f previousPosition; // previous state transform
    private Vector3f previousRotation;
    private Vector3f previousScale;

    private Vector3f deltaPosition; // accumulates changes (after taking into considuration delta in update, for physics/movements..) in order to add into previous
    private Vector3f deltaRotation;
    private Vector3f deltaScale;

    private Vector3f position; // current state transform
    private Vector3f rotation;
    private Vector3f scale;

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;

        this.previousPosition = new Vector3f(position);
        this.previousRotation = new Vector3f(rotation);
        this.previousScale = new Vector3f(scale);

        this.deltaPosition = new Vector3f();
        this.deltaRotation = new Vector3f();
        this.deltaScale = new Vector3f();
    }

    public Transform(Vector3f position)
    {
        this(position,new Vector3f(),new Vector3f(1,1,1));
    }

    public Transform() {
        this(new Vector3f(),new Vector3f(),new Vector3f(1,1,1));
    }

    public Transform setPosition(float x, float y, float z)
    {
        this.position.set(x,y,z);
        this.previousPosition.set(x,y,z);
        return this;
    }

    public void setPosition(Vector3f position)
    {
        setPosition(position.x,position.y,position.z);
    }

    public void setRotation(float x, float y, float z)
    {
        this.rotation.set(x,y,z);
        this.previousRotation.set(x,y,z);
    }

    public void setRotation(Vector3f rotation)
    {
        setRotation(rotation.x,rotation.y,rotation.z);
    }

    public Transform setScale(float x, float y, float z)
    {
        this.scale.set(x,y,z);
        this.previousScale.set(x,y,z);
        return this;
    }

    public void setScale(Vector3f scale)
    {
        setScale(scale.x,scale.y,scale.z);
    }


    public void setChangeInPosition(float dx, float dy, float dz)
    {
        deltaPosition.set(dx,dy,dz);
    }

    public void setChangeInPosition(Vector3f change)
    {
        setChangeInPosition(change.x,change.y,change.z);
    }

    public void setChangeInRotation(float dx, float dy, float dz)
    {
        deltaRotation.set(dx,dy,dz);
    }

    public void setChangeInRotation(Vector3f change)
    {
        setChangeInRotation(change.x,change.y,change.z);
    }

    public void setChangeInScale(float dx, float dy, float dz)
    {
        deltaScale.set(dx,dy,dz);
    }

    public void setChangeInScale(Vector3f change)
    {
        setChangeInScale(change.x,change.y,change.z);
    }


    public void addChangeInPosition(float dx, float dy, float dz)
    {
        deltaPosition.add(dx,dy,dz);
    }

    public void addChangeInPosition(Vector3f change)
    {
        addChangeInPosition(change.x,change.y,change.z);
    }

    public void addChangeInRotation(float dx, float dy, float dz)
    {
        deltaRotation.add(dx,dy,dz);
    }

    public void addChangeInRotation(Vector3f change)
    {
        addChangeInRotation(change.x,change.y,change.z);
    }

    public void addChangeInScale(float dx, float dy, float dz)
    {
        deltaScale.add(dx,dy,dz);
    }

    public void addChangeInScale(Vector3f change)
    {
        addChangeInScale(change.x,change.y,change.z);
    }

    public void resetChanges()
    {
        deltaPosition.set(0);
        deltaRotation.set(0);
        deltaScale.set(0);
    }

    public void update()
    {
        previousPosition.set(position);
        previousRotation.set(rotation);
        previousScale.set(scale);

        position = previousPosition.add(deltaPosition,position);
        rotation = previousRotation.add(deltaRotation,rotation);
        scale = previousScale.add(deltaScale,scale);

    }

    @Override
    public Matrix4f getOrientation(float alpha) {
        // interpolate
        Vector3f interpolatedPosition = new Vector3f();
        Vector3f interpolatedRotation = new Vector3f();
        Vector3f interpolatedScale = new Vector3f();

        previousPosition.lerp(position,alpha,interpolatedPosition);
        previousRotation.lerp(rotation,alpha,interpolatedRotation);
        previousScale.lerp(scale,alpha,interpolatedScale);

        // generate
        Matrix4f currentTransform = new Matrix4f();

        currentTransform.translate(interpolatedPosition);

        currentTransform.rotate((float)Math.toRadians(interpolatedRotation.x),new Vector3f(1,0,0));
        currentTransform.rotate((float)Math.toRadians(interpolatedRotation.y),new Vector3f(0,1,0));
        currentTransform.rotate((float)Math.toRadians(interpolatedRotation.z),new Vector3f(0,0,1));

        currentTransform.scale(interpolatedScale.x,interpolatedScale.y,interpolatedScale.z);

        return currentTransform;
    }

    public Orientation clone()
    {
        return new Transform().clone(this);
    }

    public Orientation clone(Orientation orientation) {
        if(!(orientation instanceof Transform))
            throw new IllegalArgumentException("Given Orientation is not Transform, cannot clone");

        Transform source = (Transform) orientation;

        this.position = source.position;
        this.rotation = source.rotation;
        this.scale = source.scale;

        this.previousPosition = source.previousPosition;
        this.previousRotation = source.previousRotation;
        this.previousScale = source.previousScale;

        this.deltaPosition = source.deltaPosition;
        this.deltaRotation = source.deltaRotation;
        this.deltaScale = source.deltaScale;

        return this;
    }

    public Transform setScale(float v) {
        return setScale(v,v,v);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }
}
