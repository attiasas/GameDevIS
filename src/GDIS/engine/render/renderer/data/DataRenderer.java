package GDIS.engine.render.renderer.data;

import GDIS.engine.openglWrapper.Texture;
import GDIS.engine.openglWrapper.basics.*;
import GDIS.engine.openglWrapper.shaders.AdvShaderProgram;
import GDIS.engine.render.data.Color;
import GDIS.engine.render.data.VaoData;
import GDIS.engine.render.orientation.Orientation;
import GDIS.engine.render.orientation.Transform;
import GDIS.engine.render.scene.ICamera;
import GDIS.engine.render.scene.Scene;
import GDIS.tools.debugger.Logger;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;

/**
 * Created By: Assaf, On 13/11/2021
 * Description:
 */
public class DataRenderer extends VaoUser implements IDataRenderer
{
    private int instanceCount = 0;
    private int drawCallCount = 0;

    // renderer part
    private DataShaderProgram program;
    public static final CharSequence TRANSFORM_ATTRIBUTE_NAME = "transform";
    private Scene scene;

//    private int orientationVboIndex;
    private int indexVbo;
    private List<CoordinatedRenderer> coordinatedRenderers;

    // batch part
    private final int MAX_VERTEX_CAPACITY;

    protected Batch currentBatch;
    private List<Batch> toRender;
    private Queue<Batch> reUseBatchHolder;

    private class Batch
    {
        public Texture texture;

        public FloatBuffer[] buffers;
        public int numVertices;

        public IntBuffer indices;
        public int numIndices;

        public List<Orientation> orientations;

        public Batch(int numOfBuffers)
        {
            texture = null;

            buffers = new FloatBuffer[numOfBuffers];
            for(int bufferIndex = 0 ; bufferIndex < buffers.length; bufferIndex++)
            {
                buffers[bufferIndex] = MemoryUtil.memAllocFloat(MAX_VERTEX_CAPACITY);
            }
            numVertices = 0;

            indices = MemoryUtil.memAllocInt(MAX_VERTEX_CAPACITY);
            numIndices = 0;

            orientations = new ArrayList<>();
        }

        public void reset()
        {
            texture = null;

            for(int bufferIndex = 0 ; bufferIndex < buffers.length; bufferIndex++)
            {
                buffers[bufferIndex].clear();
            }
            numVertices = 0;

            indices.clear();
            numIndices = 0;

            orientations.clear();
        }

        public void delete()
        {
            for(int bufferIndex = 0 ; bufferIndex < buffers.length; bufferIndex++)
            {
                MemoryUtil.memFree(buffers[bufferIndex]);
            }
            MemoryUtil.memFree(indices);
        }
    }

    public DataRenderer(int maxBatchCapacity, DataShaderProgram program, Scene scene)
    {
        super(GL_DYNAMIC_DRAW);
        MAX_VERTEX_CAPACITY = maxBatchCapacity;

        this.program = program;
        this.scene = scene;

        coordinatedRenderers = new ArrayList<>();

        init();

//        this.orientationVboIndex = format.getAttributeVboIndex(TRANSFORM_ATTRIBUTE_NAME);
    }

    public void addCoordinatedRenderers(CoordinatedRenderer... renderers)
    {
        for(CoordinatedRenderer renderer : renderers)
        {
            renderer.setDumpRenderer(this);
            coordinatedRenderers.add(renderer);
        }

    }

    /** Initializes the renderer. */
    private void init()
    {
        // == Init: VAO
        // long sizeInBytes = MAX_VERTEX_CAPACITY * Float.BYTES;
        program.link();

        // VaoUser part
        linkWithProgram(program);

        indexVbo = vao.createVbo(GL_ELEMENT_ARRAY_BUFFER,GL_DYNAMIC_DRAW);

        // == Init: Batches
        toRender = new ArrayList<>();
        reUseBatchHolder = new LinkedList<>();

        /* Create FloatBuffer */
        currentBatch = new Batch(format.getVboCount());
    }

    public void commitCurrentBatch()
    {
        if(currentBatch.numVertices > 0 && !currentBatch.orientations.isEmpty()) // only if the current one already been used...
        {
            // add current to render list
            toRender.add(currentBatch);
            // replace current with new batch
            currentBatch = reUseBatchHolder.isEmpty() ? new Batch(format.getVboCount()) : reUseBatchHolder.poll(); // next batch
        }
    }

    @Override
    public void render(VaoData data, Orientation orientation, Texture texture)
    {
        // TODO: after transforming batch --> implement render(data, ori, text), first just add ori to current one (to pipline, in renderToBuffer and in Batch)
        if(!data.isCompatible(format)) return;

        float[][] formatData = new float[format.getVboCount()][];

        // conditions to end current batch and start a new one
        boolean bufferMaxed = false;
        for(int vboNum = 0; !bufferMaxed && vboNum < format.getVboCount(); vboNum++)
        {
            // parse to format
            formatData[vboNum] = data.getDataByFormat(format.getVboAttributes(vboNum));
            if(formatData[vboNum] == null)
                throw new IllegalStateException("Internal: data is compatible but received null");

            bufferMaxed = currentBatch.buffers[vboNum].remaining() < formatData[vboNum].length;
        }

        if(bufferMaxed || currentBatch.texture != texture || (currentBatch.numVertices > 0 && (data.hasIndices() ? currentBatch.numIndices > 0 : currentBatch.numIndices == 0)))
        {
            // commit current and receive the next 'current' batch
            commitCurrentBatch();
            // set the new batch texture
            currentBatch.texture = texture;
        }

        // add data to buffers
        for(int vboNum = 0; !bufferMaxed && vboNum < format.getVboCount(); vboNum++)
        {
            currentBatch.buffers[vboNum].put(formatData[vboNum]);
        }
        // add indices to buffer if exists.
        if(data.hasIndices())
        {
            int[] indices = data.getIndices();
            int basePointer = currentBatch.numVertices;

            for(int i = 0; i < indices.length; i++)
            {
                currentBatch.indices.put(indices[i] + basePointer);
            }
            currentBatch.numIndices += indices.length;
        }
        // has to be after indices: basePointer needed to be added to each index (offset base on old vertex)
        currentBatch.numVertices += data.getNumberOfVerticesInData();

        // add orientation (instance count)
        currentBatch.orientations.add(orientation);
        instanceCount++;
    }

    boolean modeIndex = true; // TODO: DELETE! (and method below)
    public void render(Orientation orientation, Texture texture, float s1, float t1, float s2, float t2, float x1, float y1, float x2, float y2, Color c)
    {
        // conditions to end current batch and start a new one
        if(currentBatch.buffers[0].remaining() < 8 * 6 || currentBatch.texture != texture)
        {
            commitCurrentBatch();

            // set the new batch texture
            currentBatch.texture = texture;
        }

        // add vertex data (define triangles if indices not exists)
        float r = c.getRed();
        float g = c.getGreen();
        float b = c.getBlue();
        float a = c.getAlpha();

        if(!modeIndex)
        {
            currentBatch.buffers[0].put(x1).put(y1).put(0f).put(r).put(g).put(b).put(a).put(s1).put(t1);
            currentBatch.buffers[0].put(x1).put(y2).put(0f).put(r).put(g).put(b).put(a).put(s1).put(t2);
            currentBatch.buffers[0].put(x2).put(y2).put(0f).put(r).put(g).put(b).put(a).put(s2).put(t2);

            currentBatch.buffers[0].put(x1).put(y1).put(0f).put(r).put(g).put(b).put(a).put(s1).put(t1);
            currentBatch.buffers[0].put(x2).put(y2).put(0f).put(r).put(g).put(b).put(a).put(s2).put(t2);
            currentBatch.buffers[0].put(x2).put(y1).put(0f).put(r).put(g).put(b).put(a).put(s2).put(t1);

            currentBatch.numVertices += 6;
        }
        else
        {
            currentBatch.buffers[0].put(x1).put(y1).put(0f).put(r).put(g).put(b).put(a).put(s1).put(t1);
            currentBatch.buffers[0].put(x1).put(y2).put(0f).put(r).put(g).put(b).put(a).put(s1).put(t2);
            currentBatch.buffers[0].put(x2).put(y2).put(0f).put(r).put(g).put(b).put(a).put(s2).put(t2);

//            currentBatch.buffers[0].put(x1).put(y1).put(r).put(g).put(b).put(a).put(s1).put(t1);
//            currentBatch.buffers[0].put(x2).put(y2).put(r).put(g).put(b).put(a).put(s2).put(t2);
            currentBatch.buffers[0].put(x2).put(y1).put(0f).put(r).put(g).put(b).put(a).put(s2).put(t1);

            currentBatch.indices.put(currentBatch.numVertices).put(currentBatch.numVertices + 1).put(currentBatch.numVertices + 2); // first triangle
            currentBatch.indices.put(currentBatch.numVertices).put(currentBatch.numVertices + 2).put(currentBatch.numVertices + 3); // second triangle

            currentBatch.numVertices += 4;
            currentBatch.numIndices += 6;
        }

        // add orientation (instance count)
        currentBatch.orientations.add(orientation);
        instanceCount++;
    }


    @Override
    public void prepare()
    {
        this.program.use();
        program.loadSamplerUnit(0);

        use();
        this.format.enableVaoAttributes();

        commitCurrentBatch(); // flush to lock all batches for rendering
    }

    @Override
    public void finish()
    {
        this.format.disableVaoAttributes();
        release();

        this.program.release();

        Logger.get(getClass()).log(Logger.Level.INFO, "[ID=" + program.getId() + "] Rendered " + instanceCount + " objects [#RenderCalls=" + drawCallCount + "] (#Batches=" + toRender.size() + ")");

        /* Clear and reset for next render */
        while (!toRender.isEmpty())
        {
            Batch batch = toRender.remove(0);
            batch.reset();
            reUseBatchHolder.offer(batch);
        }

        instanceCount = 0;
        drawCallCount = 0;
    }

    private void loadDataIntoVao(Batch batch, float alpha)
    {
        for(int vboNum = 0; vboNum < format.getVboCount(); vboNum++)
        {
//            if(vboNum == orientationVboIndex)
//            {
//                try (MemoryStack stack = MemoryStack.stackPush())
//                {
//                    FloatBuffer buffer = stack.mallocFloat(batch.orientations.size() * program.transformAttribute.getCount());
//
//                    for(int i = 0; i < batch.orientations.size(); i++)
//                    {
//                        float[] orientData = new float[program.transformAttribute.getCount()];
//                        batch.orientations.get(i).getOrientation(alpha).get(orientData);
//                        buffer.put(orientData);
//                    }
//
//                    buffer.flip();
//                    vao.uploadData(vboNum,buffer);
//                }
//            }
//            else
//            {
//                batch.buffers[vboNum].flip();
//                vao.uploadData(vboNum,batch.buffers[vboNum]);
//            }

            batch.buffers[vboNum].flip();
            vao.uploadData(vboNum,batch.buffers[vboNum]);
        }

        if(batch.numIndices > 0)
        {
            batch.indices.flip();
            vao.uploadData(indexVbo,batch.indices);
        }
//        // vao.bind is built in here...
////                vao.storeData(0,0,batch.vertices);
//        vao.uploadData(0,batch.vertices);
    }

    protected void prepareInstance(Batch batch, int instanceId, float alpha)
    {
        program.loadOrientation(batch.orientations.get(instanceId).getOrientation(alpha));
    }

    @Override
    public void renderToBuffer(int bufferId, float alpha)
    {
        if(!toRender.isEmpty())
        {
            this.program.loadView(this.scene.getCamera().getViewMatrix(alpha));
            this.program.loadProjection(this.scene.getCamera().getProjectionMatrix());

            for(int batchIndex = 0; batchIndex < toRender.size(); batchIndex++)
            {
                Batch batch = toRender.get(batchIndex);
                loadDataIntoVao(batch,alpha);

                if(batch.texture != null) batch.texture.bind();

//                // TODO: transfer orientation as attribute -> no need for loop (and loadUniform), pass alpha to loadDataIntoVao
                for(int instance = 0; instance < batch.orientations.size(); instance++)
                {
                    prepareInstance(batch,instance,alpha);

                    /* Draw batch */
                    if(batch.numIndices > 0)
                    {
                        glDrawElements(GL_TRIANGLES, batch.numIndices, GL_UNSIGNED_INT, 0);
                    }
                    else
                    {
                        glDrawArrays(GL_TRIANGLES, 0, batch.numVertices);
                    }
                    drawCallCount++;
                }

//                Matrix4f model = new Matrix4f();
//                program.loadOrientation(model);

                /* Draw batch */
//                glDrawArrays(GL_TRIANGLES, 0, batch.numVertices);
//                drawCallCount++;

                if(batch.texture != null) batch.texture.unbind();
            }
        }
    }

    // == TODO Move To Advance Renderers (Square, Text...) ====================
    public void render(float x1, float y1, float x2, float y2, Color c)
    {
        render(new Transform(), null,-1,-1,-1,-1,x1,y1,x2,y2,c);
    }

    public void renderTexture(Texture texture, float x, float y, Color c)
    {
        /* Vertex positions */
        float x1 = x;
        float y1 = y;
        float x2 = x1 + texture.getWidth();
        float y2 = y1 + texture.getHeight();

        /* Texture coordinates */
        float s1 = 0f;
        float t1 = 0f;
        float s2 = 1f;
        float t2 = 1f;

        render(new Transform(),texture,s1, t1, s2, t2,x1,y1,x2,y2,c);
    }
    // == TODO ================================================================

    @Override
    public int getIndicesCount() {
        throw new IllegalStateException("Data Renderer Cannot be Render, dont have unique data.");
    }

    @Override
    public int getVerticesCount() {
        throw new IllegalStateException("Data Renderer Cannot be Render, dont have unique data.");
    }

    /**
     * Dispose renderer and clean up its used data.
     */
    @Override
    public void delete()
    {
        super.delete();

        while (!reUseBatchHolder.isEmpty())
        {
            Batch batch = reUseBatchHolder.poll();
            batch.delete();
        }
        while (!toRender.isEmpty())
        {
            Batch batch = toRender.remove(0);
            batch.delete();
        }
        currentBatch.delete();

        program.delete();
    }

    @Override
    public AdvShaderProgram getProgram() {
        return program;
    }
}
