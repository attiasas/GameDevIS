package demo;

import GDIS.engine.entities.Entity;
import GDIS.engine.entities.render.EntityRenderComponent;
import GDIS.engine.openglWrapper.Texture;
import GDIS.engine.program.state.StateGDIS;
import GDIS.engine.render.data.Color;
import GDIS.engine.render.data.VaoData;
import GDIS.engine.render.data.Vertex;
import GDIS.engine.render.mesh.Mesh;
import GDIS.engine.render.orientation.Transform;
import GDIS.engine.render.renderer.data.DataRenderer;
import GDIS.engine.render.text.QuadRenderer;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public class MyDemoState extends StateGDIS
{
    private VaoData data;
    private Mesh mesh;
    private Entity entity;

    private VaoData data2;
    private Mesh mesh2;
    private Entity entity2;

    private Texture texture;
    private Texture texture2;
//    private DataRenderer renderer;

    private QuadRenderer quadRenderer;
    @Override
    public boolean initialize()
    {
//        quadRenderer = new QuadRenderer();
//        ((DataRenderer)renderManager.getRenderer(1)).addCoordinatedRenderers(quadRenderer);

//        data = QuadRenderer.generate3DQuadData();

//        Vertex[] vertices = data.getVertices();

//        vertices[0].setColors(Color.WHITE);
//        vertices[1].setColors(new float[]{0f,1f,1f});
//        vertices[2].setColors(new float[]{1f,0f,1f});
//        vertices[3].setColors(new float[]{1f,1f,0f});
//        vertices[4].setColors(Color.BLUE);
//        vertices[5].setColors(Color.RED);
//        vertices[6].setColors(Color.GREEN);
//        vertices[7].setColors(new float[]{0.5f,0.5f,0.5f});

        data = new VaoData();

        data.addVertices(
                new Vertex().setPositions(new float[]{-0.25f,-0.25f,0.25f}).setColors(Color.WHITE),
                new Vertex().setPositions(new float[]{0.25f,-0.25f,0.25f}).setColors(new float[]{0f,1f,1f,1f}),
                new Vertex().setPositions(new float[]{0.25f,0.25f,0.25f}).setColors(new float[]{1f,0f,1f,1f}),
                new Vertex().setPositions(new float[]{-0.25f,0.25f,0.25f}).setColors(new float[]{1f,1f,0f,1f}),
                new Vertex().setPositions(new float[]{0.25f,-0.25f,-0.25f}).setColors(Color.BLUE),
                new Vertex().setPositions(new float[]{-0.25f,-0.25f,-0.25f}).setColors(Color.RED),
                new Vertex().setPositions(new float[]{0.25f,0.25f,-0.25f}).setColors(Color.GREEN),
                new Vertex().setPositions(new float[]{-0.25f,0.25f,-0.25f}).setColors(new float[]{0.5f,0.5f,0.5f,1f})
        );

        data.addTriangle(0,1,2);
        data.addTriangle(0,2,3);

        data.addTriangle(2,1,4);
        data.addTriangle(2,4,6);

        data.addTriangle(0,1,4);
        data.addTriangle(0,4,5);

        data.addTriangle(3,5,0);
        data.addTriangle(3,7,5);

        data.addTriangle(4,5,7);
        data.addTriangle(4,7,6);

        data.addTriangle(3,2,6);
        data.addTriangle(3,6,7);

//        data = QuadRenderer.generate3DQuadData();
        mesh = new Mesh(data);

        int width = stateMachine.getProgram().getConfiguration().getProgramWidth();
        int height = stateMachine.getProgram().getConfiguration().getProgramHeight();
        data2 = getSquare(10f / width,-800f / height,100f / width,200f / height);
        mesh2 = new Mesh(data2);

        texture = Texture.loadTexture("resources/example.png");
        texture2 = Texture.loadTexture("resources/smile.png");

        entity = entityManager.createEntity();
//        entity.getTransform().setScale(0.25f);
        entity.registerComponent(new EntityRenderComponent(entity,renderManager.getRenderer(1),mesh));
        entity.registerComponent(new RotateComponent(entity));

        entity2 = entityManager.createEntity();
        entity2.registerComponent(new EntityRenderComponent(entity2,renderManager.getRenderer(0),mesh2));
        entity2.registerComponent(new RotateComponent(entity2));

        return true;
    }

    public static float transform(float i)
    {
        return (i - 1f) / 2f;
    }

    public static VaoData getSquare(float x, float y, float width, float height)
    {

        float x1 = transform(x);
        float y1 = transform(y);
        float x2 = transform(x + width);
        float y2 = transform(y + height);

        VaoData vaoData = new VaoData();

        vaoData.addVertices(
                new Vertex().setPositions(new float[]{x1,y1,0f}).setColors(Color.BLUE).setTextureCoords(new float[]{-1f,-1f}),
                new Vertex().setPositions(new float[]{x1,y2,0f}).setColors(Color.BLUE).setTextureCoords(new float[]{-1f,-1f}),
                new Vertex().setPositions(new float[]{x2,y2,0f}).setColors(Color.BLUE).setTextureCoords(new float[]{-1f,-1f}),

                new Vertex().setPositions(new float[]{x1,y1,0f}).setColors(Color.RED).setTextureCoords(new float[]{-1f,-1f}),
                new Vertex().setPositions(new float[]{x2,y2,0f}).setColors(Color.RED).setTextureCoords(new float[]{-1f,-1f}),
                new Vertex().setPositions(new float[]{x2,y1,0f}).setColors(Color.RED).setTextureCoords(new float[]{-1f,-1f})
        );

        return vaoData;
    }

    public void getIndexSquare(float x, float y, float width, float height)
    {

    }

    @Override
    public void dispose() {
        mesh.delete();
        mesh2.delete();
        texture.delete();
        texture2.delete();
    }

    float t = 0;
    int ma = 250;
    int mi = -250;
    boolean b = true;
    Transform transform = new Transform();
    @Override
    public boolean update(float delta)
    {
//        ((VaoUserRenderer)renderManager.getRenderer(0)).render((BatchRendererFromBase)renderManager.getRenderer(1),entity.getTransform(),null);
//        ((IDataRenderer)renderManager.getRenderer(0)).render(data,entity.getTransform(),null);
//        ((IDataRenderer)renderManager.getRenderer(0)).render(getSquare(100,100,100,100),entity.getTransform(),null);
//        renderer.begin();
//        texture2.bind(); // TODO: bind on render (not update) transfer it

        t += 10 * (b ? delta : -delta);
        if((b && t >= ma) || (!b && t <= mi)) b = !b;

        // TODO: Fix Render With VaoData!e
      ((DataRenderer)renderManager.getRenderer(1)).render(QuadRenderer.generate2DQuadData(Color.YELLOW,true,true),transform.setScale(t / ma).setPosition(t / ma,0f,0f),texture2);

        ((DataRenderer)renderManager.getRenderer(2)).render(50,50,150 + t,170,Color.BLUE);

        ((DataRenderer)renderManager.getRenderer(2)).render(450,250,400 - t,170,Color.RED);

        ((DataRenderer)renderManager.getRenderer(2)).renderTexture(texture,800,420, Color.WHITE);

        ((DataRenderer)renderManager.getRenderer(3)).render(800 - (2 * t),300,900 - (2 * t),700,Color.GREEN);

        ((DataRenderer)renderManager.getRenderer(2)).renderTexture(texture2,420,-70, Color.WHITE);


//        texture2
//        texture.unbind();
//        renderer.end();
        return true;
    }


    @Override
    public void handleInput() {

    }
}
