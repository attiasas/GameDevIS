package GDIS.engine.render.data;

import GDIS.engine.openglWrapper.basics.Attribute;
import GDIS.engine.openglWrapper.basics.VaoFormat;

import java.util.*;

/**
 * Created By: Assaf, On 30/10/2021
 * Description:
 */
public class VaoData
{
    private List<Vertex> vertices;
    private List<Integer> indices; // represents triangles, point to a vertex by index (stride of 3 in the array between each triangle)

    public VaoData()
    {
        vertices = new ArrayList<>();
        indices = new ArrayList<>();
    }

    public void addVertices(Vertex... vertices)
    {
        for(Vertex vertex : vertices)
        {
            this.vertices.add(vertex);
        }
    }

    private void addIndices(int... indices)
    {
        if(indices == null)
            throw new IllegalArgumentException("indices argument is null");

        if(indices.length % 3 != 0)
            throw new IllegalArgumentException("indices argument length is not divisible by 3");

        for(int index : indices)
        {
            if(index < 0 || index >= this.vertices.size())
                throw new IllegalArgumentException("Index '" + index + "' is out of bounds, cannot find matching vertex");

            this.indices.add(index);
        }
    }

    public void addTriangle(int idxV1, int idxV2, int idxV3)
    {
        addIndices(idxV1,idxV2,idxV3);
    }

    public boolean hasIndices()
    {
        return !this.indices.isEmpty();
    }

    public int[] getIndices()
    {
        int[] result = new int[this.indices.size()];
        for(int i = 0; i < this.indices.size(); i++)
        {
            result[i] = this.indices.get(i);
        }

        return result;
    }

    public VaoData setColor(Color color)
    {
        for(Vertex v : this.vertices)
        {
            v.setColors(color);
        }
        return this;
    }

    public Vertex[] getVertices()
    {
        Vertex[] result = new Vertex[this.vertices.size()];
        for(int i = 0; i < this.vertices.size(); i++)
        {
            result[i] = this.vertices.get(i);
        }

        return result;
    }

    public int getNumberOfVerticesInData()
    {
        return this.vertices.size();
    }

    public int getNumberOfTrianglesInData()
    {
        // TODO: if has index => if mode normal divide by 3 indices.len, if additive -> check!, if has no index divide by 3 vertices.size
        return hasIndices() ? this.indices.size() / 3 : getNumberOfVerticesInData() < 3 ? 0 : this.vertices.size() / 3;
    }

    public void validate()
    {
        if(this.vertices.isEmpty())
            return;

        // validate all vertex has the same format
        int[] floatCountByDataType = new int[Vertex.DataType.values().length];
        for(int i = 0; i < this.vertices.size(); i++)
        {
            Vertex vertex = this.vertices.get(i);

            for(Vertex.DataType type : Vertex.DataType.values())
            {
                if(i == 0) floatCountByDataType[type.ordinal()] = vertex.getFloatCountByDataType(type);
                else
                {
                    if(floatCountByDataType[type.ordinal()] != vertex.getFloatCountByDataType(type))
                        throw new IllegalStateException("Data is not consistent, found two vertices with different float count of data type '" + type + "' (" + floatCountByDataType[type.ordinal()] + ", " + vertex.getFloatCountByDataType(type) + ")");
                }
            }
        }
    }


    public boolean isCompatible(VaoFormat format)
    {
        try {
            for(int vboNum = 0; vboNum < format.getVboCount(); vboNum++)
            {
                isCompatible(format.getVboAttributes(vboNum));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private int isCompatible(List<Attribute> format)
    {
        // init
        validate();

        Set<Integer> formatNumberFound = new HashSet<>();
        int stride = 0;

        for(Attribute attribute : format)
        {
            int attributeFormatTypeNumber = attribute.getFormatTypeNumber();
            if(attributeFormatTypeNumber < 0)
            {
                if(formatNumberFound.contains(attributeFormatTypeNumber))
                    throw new IllegalArgumentException("Attribute Format Type Number '" + attributeFormatTypeNumber + "' exists twice in the same Vbo-Attributes");
                formatNumberFound.add(attributeFormatTypeNumber);

                int dataCountPerVertex = this.vertices.get(0).getFloatCountByFormatType(attributeFormatTypeNumber);
                if(!attribute.isOptional())
                {
                    if(!this.vertices.get(0).hasDataTypeByFormatType(attributeFormatTypeNumber))
                        throw new IllegalArgumentException("Data is not compatible, can't find data for Attribute '" + attribute.getName() + "' (Format Type Number = " + attributeFormatTypeNumber + "')");
                    if(dataCountPerVertex != attribute.getCount())
                        throw new IllegalArgumentException("Data is not compatible, number of floats per vertex in data (count=" + dataCountPerVertex + ") and in attribute '" + attribute.getName() + "' (count=" + attribute.getCount() + ") (Format Type Number = " + attributeFormatTypeNumber + ") are not equal.");
                }
            }
            stride += attribute.getCount();
        }

        return stride;
    }

    public float[] getDataByFormat(List<Attribute> format)
    {
        if(this.vertices.isEmpty())
            return null;

        // validate vertex information is uniform format and match the given format
        int stride = isCompatible(format);

        // parse to format
        float[] dataByFormat = new float[this.vertices.size() * stride];
        int pointer = 0;

        for(int index = 0; index < this.vertices.size(); index++)
        {
            Vertex vertex = this.vertices.get(index);

            for(int atrribNum = 0; atrribNum < format.size(); atrribNum++)
            {
                Attribute current = format.get(atrribNum);

                float[] currentData = vertex.getVertexDataByFormatType(current.getFormatTypeNumber());
//                System.out.println(current + " | Data: " + Arrays.toString(currentData));
                for(int dataPointer = 0; dataPointer < current.getCount(); dataPointer++)
                {
                    if(currentData != null && currentData.length > 0)
                    {
                        dataByFormat[pointer++] = currentData[dataPointer];
                    }
                    else if(current.isOptional())
                    {
                        dataByFormat[pointer++] = ((float[])current.getDefaultValue())[dataPointer]; // pad optional and not found with -1.
                    }
                    else
                        throw new IllegalArgumentException("VaoData is not compatible with the given format, Attribute '" + current.getName() + "' is not optional and not exists in the data");
                }
            }
        }

        return dataByFormat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VaoData vaoData = (VaoData) o;
        return vertices.equals(vaoData.vertices) &&
                indices.equals(vaoData.indices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, indices);
    }

}
