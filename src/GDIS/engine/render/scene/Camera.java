package GDIS.engine.render.scene;

import GDIS.engine.entities.Entity;
import GDIS.engine.render.orientation.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created By: Assaf, On 23/11/2021
 * Description:
 */
public class Camera extends Entity implements ICamera
{
    private float fov = 70;
    private float nPlane = 0.1f;
    private float fPlane = 1000;

    private float pitch; // rotation in x
    private float yaw; // rotation in y
    private float roll; // rotation in z

    private Matrix4f projectionMatrix;

    private Entity targetEntity;

    private static int cameraCounter = 0;

    public Camera(Matrix4f projectionMatrix)
    {
        super(cameraCounter++,null);
        this.projectionMatrix = projectionMatrix;
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    @Override
    public Matrix4f getViewMatrix(float alpha)
    {
        Matrix4f viewMatrix = new Matrix4f();

        // interpolate
//        Vector3f interpolatedPosition = new Vector3f();
//        Vector3f interpolatedRotation = new Vector3f();
//        Vector3f interpolatedScale = new Vector3f();

//        viewMatrix.translate(interpolatedPosition);
//
//        Vector3f rotation = transform.getRotation();
//        currentTransform.rotate((float)Math.toRadians(interpolatedRotation.x),new Vector3f(1,0,0));
//        currentTransform.rotate((float)Math.toRadians(interpolatedRotation.y),new Vector3f(0,1,0));
//        currentTransform.rotate((float)Math.toRadians(interpolatedRotation.z),new Vector3f(0,0,1));
//
//        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix,
//                viewMatrix);
//        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
//
//        Vector3f cameraPos = camera.getPosition();
//        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
//        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
//        return viewMatrix;

        return new Matrix4f();
    }
//
//    public void invertPitch(){
//        this.pitch = -pitch;
//    }
//
//    public Vector3f getPosition() {
//        return position;
//    }
//
//    public float getPitch() {
//        return pitch;
//    }
//
//    public float getYaw() {
//        return yaw;
//    }
//
//    public float getRoll() {
//        return roll;
//    }
//
//    private void calculateCameraPosition(float horizDistance, float verticDistance){
//        float theta = player.getRotY() + angleAroundPlayer;
//        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
//        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
//        position.x = player.getPosition().x - offsetX;
//        position.z = player.getPosition().z - offsetZ;
//        position.y = player.getPosition().y + verticDistance + 4;
//    }
//
//    private float calculateHorizontalDistance(){
//        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch+4)));
//    }
//
//    private float calculateVerticalDistance(){
//        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch+4)));
//    }
//
//    private void calculateZoom(){
//        float zoomLevel = Mouse.getDWheel() * 0.03f;
//        distanceFromPlayer -= zoomLevel;
//        if(distanceFromPlayer<5){
//            distanceFromPlayer = 5;
//        }
//    }
//
//    private void calculatePitch(){
//        if(Mouse.isButtonDown(1)){
//            float pitchChange = Mouse.getDY() * 0.2f;
//            pitch -= pitchChange;
//            if(pitch < 0){
//                pitch = 0;
//            }else if(pitch > 90){
//                pitch = 90;
//            }
//        }
//    }
//
//    private void calculateAngleAroundPlayer(){
//        if(org.lwjgl.glfw.Mouse.isButtonDown(0)){
//            float angleChange = Mouse.getDX() * 0.3f;
//            angleAroundPlayer -= angleChange;
//        }
//    }
//
//    public void move(){
//        calculateZoom();
//        calculatePitch();
//        calculateAngleAroundPlayer();
//        float horizontalDistance = calculateHorizontalDistance();
//        float verticalDistance = calculateVerticalDistance();
//        calculateCameraPosition(horizontalDistance, verticalDistance);
//        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
//        yaw%=360;
//    }

    @Override
    public void update(float delta) {
        this.transform.update();
    }
}
