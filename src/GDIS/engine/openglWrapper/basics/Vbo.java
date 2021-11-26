package GDIS.engine.openglWrapper.basics;

import GDIS.tools.debugger.Logger;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

/**
 * Created By: Assaf, On 15/10/2021
 * Description: Represents and wrap the Vertex Buffer Object, a buffer that holds data that load into the GPU.
 */
public class Vbo
{

    /**
     * Creates a Vertex Buffer Object (VBO).
     */
    public Vbo() {
        id = glGenBuffers();
    }

    /**
     * Binds this VBO with specified target. The target in the tutorial should
     * be <code>GL_ARRAY_BUFFER</code> most of the time.
     *
     * @param target Target to bind
     */
    public void bind(int target) {
        glBindBuffer(target, id);
    }

    /**
     * Upload vertex data to this VBO with specified target, data and usage. The
     * target in the tutorial should be <code>GL_ARRAY_BUFFER</code> and usage
     * should be <code>GL_STATIC_DRAW</code> most of the time.
     *
     * @param target Target to upload
     * @param data   Buffer with the data to upload
     * @param usage  Usage of the data
     */
    public void uploadData(int target, FloatBuffer data, int usage) {
        glBufferData(target, data, usage);
    }

    /**
     * Upload null data to this VBO with specified target, size and usage. The
     * target in the tutorial should be <code>GL_ARRAY_BUFFER</code> and usage
     * should be <code>GL_STATIC_DRAW</code> most of the time.
     *
     * @param target Target to upload
     * @param size   Size in bytes of the VBO data store
     * @param usage  Usage of the data
     */
    public void uploadData(int target, long size, int usage) {
        glBufferData(target, size, usage);
    }

    /**
     * Upload sub data to this VBO with specified target, offset and data. The
     * target in the tutorial should be <code>GL_ARRAY_BUFFER</code> most of the
     * time.
     *
     * @param target Target to upload
     * @param offset Offset where the data should go in bytes
     * @param data   Buffer with the data to upload
     */
    public void uploadSubData(int target, long offset, FloatBuffer data) {
        glBufferSubData(target, offset, data);
    }

    /**
     * Upload element data to this EBO with specified target, data and usage.
     * The target in the tutorial should be <code>GL_ELEMENT_ARRAY_BUFFER</code>
     * and usage should be <code>GL_STATIC_DRAW</code> most of the time.
     *
     * @param target Target to upload
     * @param data   Buffer with the data to upload
     * @param usage  Usage of the data
     */
    public void uploadData(int target, IntBuffer data, int usage) {
        glBufferData(target, data, usage);
    }

    // ====================================

    /**
     * Stores the handle of the VBO.
     */
    private final int id;

    /**
     * Stores the type (GL_ARRAY_BUFFER / GL_ELEMENT_ARRAY_BUFFER)
     */
    private int type = GL_ARRAY_BUFFER;

    /**
     * Stores the usage for this buffer object
     */
    private int usage = GL_STATIC_DRAW;

    /**
     * Private Constructor, stores the information of a vbo. construct representation and not allocating.
     * @param id - the id of the vbo
     * @param type - the type of the vbo
     * @param usage - the usage of the vbo
     */
    private Vbo(int id, int type, int usage)
    {
        this.id = id;
        this.type = type;
        this.usage = usage;
        Logger.get(getClass()).log(Logger.Level.DEBUG,"Create [ID="+ id + ", type=" + type + ", usage=" + usage + "]");
    }

    /**
     * Create and allocate a Vbo with the given parameters
     * @param type - the type of the vbo
     * @param usage - the usage of the vbo
     * @return the created vbo
     */
    public static Vbo create(int type, int usage)
    {
        return new Vbo(glGenBuffers(),type,usage);
    }

    /**
     * bind the current vbo into the shader context.
     * required to call in order to transfer the stored data into the shader program before draw call.
     */
    public void bind()
    {
        glBindBuffer(type, id);
        Logger.get(getClass()).log(Logger.Level.DEBUG,"[ID="+ id + "] Binding To [type=" + type + ", usage=" + usage + "]");
    }

    /**
     * unbind the current vbo
     */
    public void unbind()
    {
        glBindBuffer(type, 0);
        Logger.get(getClass()).log(Logger.Level.DEBUG,"[ID="+ id + "] UnBinding To [type=" + type + ", usage=" + usage + "]");
    }

    /**
     * Prepare Space to store (allocate with null's). after allocates 'storeData' method can be called
     * @param sizeInBytes - allocates the given size into the buffer.
     */
    public void allocateSpace(long sizeInBytes){
        GL15.glBufferData(type, sizeInBytes, usage);
    }

    /**
     * Store data in an allocated buffer (after calling allocate first)
     * @param startInBytes - which index (byte number) to start storing the given data
     * @param data - the given data to store into the vbo
     */
    public void storeData(long startInBytes, IntBuffer data){
        GL15.glBufferSubData(type, startInBytes, data);
    }

    /**
     * Store data in an allocated buffer (after calling allocate first)
     * @param startInBytes - which index (byte number) to start storing the given data
     * @param data - the given data to store into the vbo
     */
    public void storeData(long startInBytes, FloatBuffer data){
        GL15.glBufferSubData(type, startInBytes, data);
    }

    /**
     * Store data in an allocated buffer (after calling allocate first)
     * @param startInBytes - which index (byte number) to start storing the given data
     * @param data - the given data to store into the vbo
     */
    public void storeData(long startInBytes, ByteBuffer data){
        GL15.glBufferSubData(type, startInBytes, data);
    }

    /**
     * Upload a data to this buffer (Allocate + store)
     * @param data - the given data to store into the vbo
     */
    public void uploadData(FloatBuffer data) {
        glBufferData(type, data, usage);
    }

    /**
     * Upload a data to this buffer (Allocate + store)
     * @param data - the given data to store into the vbo
     */
    public void uploadData(IntBuffer data) {
        glBufferData(type, data, usage);
    }

    /**
     * Upload a data to this buffer (Allocate + store)
     * @param data - the given data to store into the vbo
     */
    public void uploadData(ByteBuffer data) {
        glBufferData(type, data, usage);
    }

    /**
     * Deletes this VBO.
     */
    public void delete()
    {
        Logger.get(getClass()).log(Logger.Level.DEBUG,"Create [ID="+ id + ", type=" + type + ", usage=" + usage + "]");
        glDeleteBuffers(id);
    }

    /**
     * Getter for the Vertex Buffer Object ID.
     * @return Handle of the VBO
     */
    public int getID() {
        return id;
    }
}