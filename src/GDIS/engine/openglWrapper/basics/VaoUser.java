package GDIS.engine.openglWrapper.basics;

import GDIS.engine.openglWrapper.shaders.AdvShaderProgram;
import GDIS.tools.debugger.Logger;

import java.util.List;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;

/**
 * Created By: Assaf, On 30/10/2021
 * Description:
 */
public abstract class VaoUser
{
    protected SmartVao vao;
    protected VaoFormat format;
    protected boolean linked = false;

    private int type;
    private int usage;

    public VaoUser(int usage) {
        this.type = GL_ARRAY_BUFFER;
        this.usage = usage;
    }

    public abstract int getIndicesCount();
    public abstract int getVerticesCount();

    public void use()
    {
        if(!linked)
            throw new IllegalStateException("Vao has not been linked with format");

        vao.bind();
    }

    public void release()
    {
        if(!linked)
            throw new IllegalStateException("Vao has not been linked with format");

        vao.unbind();
    }

    public void linkWithProgram(AdvShaderProgram program)
    {
        // reset
        if(vao != null) vao.delete();
        vao = new SmartVao();

        program.use();
        this.format = program.getProgramFormat();

        // create vbo by format and link it to the given vbo-attributes by the format.
        for(int vboNum = 0; vboNum < format.getVboCount(); vboNum++)
        {
            int vboStride = format.getVboStride(vboNum);
            int[] vboOffsets = format.getVboOffsets(vboNum);
            List<Attribute> vboAttrib = format.getVboAttributes(vboNum);

            String attrib = "";
            String attInf = "";
            String s = "[ID=" + vao.getID() + "] Creating Vbo" + ", Attributes -> ";
//            System.out.print("Creating Vbo #" + vboIndex + ", Attributes -> ");
            for(int i = 0; i < vboAttrib.size(); i++) {
                attrib += "" + vboAttrib.get(i);
                attInf += "Attribute[name=" + vboAttrib.get(i).getName() + ", offsetInBytes=" + vboOffsets[i] + "]";
//                System.out.print();
                if(i != vboAttrib.size() - 1)
                {
                    attrib += ", ";
                    attInf += ", ";
                }
//                if(i == vboAttrib.size() - 1)
//                    s += "\n"; //System.out.println();
//                else s+= ", "; //System.out.print(", ");
            }
            Logger.get(getClass()).log(Logger.Level.INFO,s + attrib);

//            float[] attributeData = getData().getMeshDataByFormat(vboAttrib);
            int vboIndex = vao.createVbo(type,usage);

            Vbo vbo = vao.getVbo(vboIndex);
//            Vbo vbo = vao.createVboAndUploadData(GL_ARRAY_BUFFER,GL_STATIC_DRAW, attributeData);

            // link input attributes
            s = "[ID=" + vao.getID() + "] Linking Vbo (index=" + vboIndex + ") [StrideInBytes=" + vboStride + "], Attributes -> ";
            Logger.get(getClass()).log(Logger.Level.INFO,s + attInf);
            for(int attribNum = 0; attribNum < vboAttrib.size(); attribNum++)
            {
                Attribute current = vboAttrib.get(attribNum);
//                vao.bind(vboIndex);
                vbo.bind();
                current.linkToVbo(vboStride,vboOffsets[attribNum]);
            }

        }

//        if(data.hasIndices())
//        {
//            vao.createVboAndUploadData(GL_ELEMENT_ARRAY_BUFFER,GL_STATIC_DRAW,data.getIndices());
//        }

        program.release();
        linked = true;
    }

    public void delete()
    {
        if(vao != null) vao.delete();
    }

    public SmartVao getVao() { return this.vao; }
}
