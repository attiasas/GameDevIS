package GDIS.engine.openglWrapper.basics;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Assaf, On 30/10/2021
 * Description:
 */
public class SmartVao extends Vao
{
    private List<Vbo> relatedVbo = new ArrayList<>();

    public void bind(int... vboIndices)
    {
        super.bind();
        for(int i = 0; i < vboIndices.length; i++)
        {
            relatedVbo.get(vboIndices[i]).bind();
        }
    }

    public void unbind(int... vboIndices)
    {
        for(int i = 0; i < vboIndices.length; i++)
        {
            relatedVbo.get(vboIndices[i]).unbind();
        }
        super.unbind();
    }

    /**
     * Binds the VAO.
     */
    @Override
    public void bind() {

        super.bind();

        for(Vbo vbo : relatedVbo)
        {
            vbo.bind();
        }
    }

    @Override
    public void unbind()
    {
        for(Vbo vbo : relatedVbo)
        {
            vbo.unbind();
        }

        super.unbind();
    }

    /**
     * Deletes the VAO.
     */
    @Override
    public void delete() {

        for(Vbo vbo : relatedVbo)
        {
            vbo.delete();
        }

        super.delete();
    }

    public int createVbo(int type, int usage)
    {
        super.bind();

        Vbo vbo = Vbo.create(type,usage);
        int index = relatedVbo.size();
        relatedVbo.add(vbo);

        return index;
    }

    public Vbo getVbo(int vboId)
    {
        return relatedVbo.get(vboId);
    }

    public int getNumberOfVbo() { return relatedVbo.size(); }

    public void allocateSpace(int vboId, long sizeInBytes)
    {
        bind(vboId);
        Vbo vbo = getVbo(vboId);
//        vbo.bind();
        vbo.allocateSpace(sizeInBytes);
    }

    // == Store Operation (After allocation ) ===================================================

    public void storeData(int vboId, long startInBytes, FloatBuffer data)
    {
        bind(vboId);
        Vbo vbo = getVbo(vboId);
//        vbo.bind();
        vbo.storeData(startInBytes,data);
    }

    public void storeData(int vboId, long startInBytes, IntBuffer data)
    {
        bind(vboId);
        Vbo vbo = getVbo(vboId);
//        vbo.bind();
        vbo.storeData(startInBytes,data);
    }

    public void storeData(int vboId, long startInBytes, ByteBuffer data)
    {
        bind(vboId);
        Vbo vbo = getVbo(vboId);
//        vbo.bind();
        vbo.storeData(startInBytes,data);
    }

    public void storeData(int vboId, long startInBytes, float[] data)
    {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(data.length);
            buffer.put(data);
            buffer.flip();

            storeData(vboId,startInBytes,buffer);
        }
    }

    public void storeData(int vboId, long startInBytes, int[] data)
    {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer buffer = stack.mallocInt(data.length);
            buffer.put(data);
            buffer.flip();

            storeData(vboId,startInBytes,buffer);
        }
    }

    public void storeData(int vboId, long startInBytes, byte[] data)
    {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer buffer = stack.malloc(data.length);
            buffer.put(data);
            buffer.flip();

            storeData(vboId,startInBytes,buffer);
        }
    }

    // ==========================================================================================

    // == Upload Operation (Allocate + Store) ===================================================

    public void uploadData(int vboId, FloatBuffer data)
    {
        bind(vboId);
        Vbo vbo = getVbo(vboId);
//        vbo.bind();
        vbo.uploadData(data);
    }

    public void uploadData(int vboId, IntBuffer data)
    {
        bind(vboId);
        Vbo vbo = getVbo(vboId);
//        vbo.bind();
        vbo.uploadData(data);
    }

    public void uploadData(int vboId, ByteBuffer data)
    {
        bind(vboId);
        Vbo vbo = getVbo(vboId);
//        vbo.bind();
        vbo.uploadData(data);
    }

    public void uploadData(int vboId, float[] data)
    {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(data.length);
            buffer.put(data);
            buffer.flip();

            uploadData(vboId,buffer);
        }
    }

    public void uploadData(int vboId, int[] data)
    {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer buffer = stack.mallocInt(data.length);
            buffer.put(data);
            buffer.flip();

            uploadData(vboId,buffer);
        }
    }

    public void uploadData(int vboId, byte[] data)
    {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer buffer = stack.malloc(data.length);
            buffer.put(data);
            buffer.flip();

            uploadData(vboId,buffer);
        }
    }

    // ==========================================================================================

    // === Create + Upload ======================================================================

    public Vbo createVboAndUploadData(int type, int usage, float[] data)
    {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(data.length);
            buffer.put(data);
            buffer.flip();

            return createVboAndUploadData(type,usage,buffer);
        }
    }

    public Vbo createVboAndUploadData(int type, int usage, int[] data)
    {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer buffer = stack.mallocInt(data.length);
            buffer.put(data);
            buffer.flip();

            return createVboAndUploadData(type,usage,buffer);
        }
    }

    public Vbo createVboAndUploadData(int type, int usage, byte[] data)
    {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer buffer = stack.malloc(data.length);
            buffer.put(data);
            buffer.flip();

            return createVboAndUploadData(type,usage,buffer);
        }
    }

    public Vbo createVboAndUploadData(int type, int usage, FloatBuffer data)
    {
        super.bind();
        Vbo vbo = getVbo(createVbo(type,usage));
        vbo.bind();
        vbo.uploadData(data);

        return vbo;
    }

    public Vbo createVboAndUploadData(int type, int usage, IntBuffer data)
    {
        super.bind();
        Vbo vbo = getVbo(createVbo(type,usage));
        vbo.bind();
        vbo.uploadData(data);

        return vbo;
    }

    public Vbo createVboAndUploadData(int type, int usage, ByteBuffer data)
    {
        super.bind();
        Vbo vbo = getVbo(createVbo(type,usage));
        vbo.bind();
        vbo.uploadData(data);

        return vbo;
    }

    // ==========================================================================================
}
