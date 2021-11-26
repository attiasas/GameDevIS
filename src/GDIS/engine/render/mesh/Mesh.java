package GDIS.engine.render.mesh;

import GDIS.engine.openglWrapper.basics.Attribute;
import GDIS.engine.openglWrapper.basics.VaoUser;
import GDIS.engine.openglWrapper.shaders.AdvShaderProgram;
import GDIS.engine.render.data.VaoData;
import GDIS.tools.debugger.Logger;

import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

/**
 * Created By: Assaf, On 30/10/2021
 * Description:
 */
public class Mesh extends VaoUser {

    private VaoData data;

    private int usage = GL_STATIC_DRAW;

    public Mesh() {super(GL_STATIC_DRAW);}
    public Mesh(VaoData data)
    {
        this();
        setData(data);
    }

    public void setData(VaoData data)
    {
        this.data = data;
        if(linked) loadDataIntoVao();
    }
    public VaoData getData()
    {
        return this.data;
    }

    public void loadDataIntoVao()
    {
        if(this.data == null)
            throw new IllegalStateException("This Mesh does not contain data");

        for(int vboNum = 0; vboNum < format.getVboCount(); vboNum++)
        {
            List<Attribute> vboAttrib = format.getVboAttributes(vboNum);
            float[] attributeData = data.getDataByFormat(vboAttrib);
//            System.out.print("Linking Vbo #" + vboNum + ", Attributes -> ");
//            for(int i = 0; i < vboAttrib.size(); i++) {
//                System.out.print("" + vboAttrib.get(i).getName());
//                if(i == vboAttrib.size() - 1)
//                    System.out.println();
//                else System.out.print(", ");
//            }
            Logger.get(getClass()).log(Logger.Level.INFO,"[ID=" + vao.getID() + "] Upload Data To Vbo (index=" + vboNum + "), count:" + attributeData.length);
            Logger.get(getClass()).log(Logger.Level.INFO,"[ID=" + vao.getID() + "] Uploaded Data:" + Arrays.toString(attributeData));
            vao.uploadData(vboNum,attributeData);
            // Or (above statement equals to the two statements below
//            vao.allocateSpace(vboNum,attributeData.length * Float.BYTES);
//            vao.storeData(vboNum,0,attributeData);
        }

        if(data.hasIndices())
        {
            Logger.get(getClass()).log(Logger.Level.INFO,"[ID=" + vao.getID() + "] Create And Upload Data To <INDEX> Vbo, count: " + data.getIndices().length);
            Logger.get(getClass()).log(Logger.Level.INFO,"[ID=" + vao.getID() + "] Uploaded Data:" + Arrays.toString(data.getIndices()));
            vao.createVboAndUploadData(GL_ELEMENT_ARRAY_BUFFER,usage,data.getIndices());
        }
    }

    @Override
    public int getIndicesCount() {
        return data.hasIndices() ? data.getIndices().length : 0;
    }

    @Override
    public int getVerticesCount() {
        return data.getNumberOfVerticesInData();
    }

    @Override
    public void linkWithProgram(AdvShaderProgram program)
    {
        super.linkWithProgram(program);

        loadDataIntoVao();
    }

}
