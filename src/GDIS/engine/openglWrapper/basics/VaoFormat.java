package GDIS.engine.openglWrapper.basics;

import GDIS.tools.debugger.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created By: Assaf, On 16/10/2021
 * Description: container of attributes (Set), definition of how the Vao should be stored to run on a smile shader program
 */
public class VaoFormat
{

    private List<List<Attribute>> vaoFormat = new ArrayList<>();
    private Set<CharSequence> attributeList = new HashSet<>();

    private int[] vaoStrides;
    private int[][] vaoOffsets;

    public VaoFormat()
    {

    }

    public void enableVaoAttributes()
    {
        for(int vboNum = 0; vboNum < getVboCount(); vboNum++)
        {
            List<Attribute> attributes = vaoFormat.get(vboNum);
            for(Attribute attribute : attributes)
            {
                attribute.enable();
            }
        }
    }

    public void disableVaoAttributes()
    {
        for(int vboNum = 0; vboNum < getVboCount(); vboNum++)
        {
            List<Attribute> attributes = vaoFormat.get(vboNum);
            for(Attribute attribute : attributes)
            {
                attribute.disable();
            }
        }
    }

    public Attribute getAttribute(CharSequence name)
    {
        if(!attributeList.contains(name))
            throw new IllegalArgumentException("The format does not contain attribute '" + name + "'");

        Attribute attribute = null;
        for(int vboNum = 0; attribute == null && vboNum < getVboCount(); vboNum++)
        {
            List<Attribute> vboAttrib = vaoFormat.get(vboNum);
            for(int attNum = 0; attribute == null && attNum < vboAttrib.size(); attNum++)
            {
                if(vboAttrib.get(attNum).getName().equals(name))
                    attribute = vboAttrib.get(attNum);
            }
        }

        return attribute;
    }

    public int getAttributeVboIndex(CharSequence name)
    {
        if(!attributeList.contains(name))
            throw new IllegalArgumentException("The format does not contain attribute '" + name + "'");

        for(int vboNum = 0; vboNum < getVboCount(); vboNum++)
        {
            List<Attribute> vboAttrib = vaoFormat.get(vboNum);
            for(int attNum = 0; attNum < vboAttrib.size(); attNum++)
            {
                if(vboAttrib.get(attNum).getName().equals(name))
                {
                    return vboNum;
                }
            }
        }

        return -1;
    }

    public int getVboCount()
    {
        return  vaoFormat.size();
    }

    public int addVbo(Attribute... attributes)
    {
        int vboNum = vaoFormat.size();

        List<Attribute> vboFormat = new ArrayList<>();

        for(int i = 0; i < attributes.length; i++)
        {
            Attribute current = attributes[i];
            if(attributeList.contains(current.getName()))
                throw new IllegalArgumentException("Attribute '" + current.getName() + "' already exist in this format");

            vboFormat.add(current);
            attributeList.add(current.getName());
        }

        vaoFormat.add(vboFormat);

        calculateStridesAndOffsets();
        return vboNum;
    }

    private void calculateStridesAndOffsets()
    {
        vaoStrides = new int[getVboCount()];
        vaoOffsets = new int[getVboCount()][];

        for(int vboNum = 0; vboNum < getVboCount(); vboNum++)
        {
            List<Attribute> vboFormat = vaoFormat.get(vboNum);

            int stride = 0;
            int[] offsets = new int[vboFormat.size()];

            for(int i = 0; i < vboFormat.size(); i++)
            {
                offsets[i] = stride;
                stride += vboFormat.get(i).getSize();
            }

            vaoStrides[vboNum] = stride;
            vaoOffsets[vboNum] = offsets;
        }
    }

    public List<Attribute> getVboAttributes(int vboNum)
    {
        return vaoFormat.get(vboNum);
    }

    public int getVboStride(int vboNum)
    {
        return vaoStrides[vboNum];
    }

    public int[] getVboOffsets(int vboNum)
    {
        return vaoOffsets[vboNum];
    }

    public void linkToProgram(int id)
    {
        Logger.get(getClass()).log(Logger.Level.DEBUG,"Link To Program [ID="+ id + "]");
        for(int vboNum = 0; vboNum < getVboCount(); vboNum++)
        {
            List<Attribute> vboFormat = vaoFormat.get(vboNum);

            for(Attribute attribute : vboFormat)
            {
                attribute.linkToProgram(id);
            }
        }
    }

}
