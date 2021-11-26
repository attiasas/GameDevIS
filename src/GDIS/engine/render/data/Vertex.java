package GDIS.engine.render.data;

import java.util.Arrays;

/**
 * Created By: Assaf, On 30/10/2021
 * Description:
 */
public class Vertex
{
    private float[] positions = null;
    private float[] colors = null;
    private float[] normals = null;
    private float[] textureCoords = null;

    // Format Data Type
//    public final static int POSITION = -1;
//    public final static int COLOR = -2;
//    public final static int TEXTURE_COORDS = -3;
//    public final static int NORMAL = -4;

    public enum DataType
    {
        POSITION, COLOR, TEXTURE_COORDS, NORMAL
    }

    public float[] getVertexDataByFormatType(int attributeFormatTypeNumber)
    {
        if(attributeFormatTypeNumber < 0 || attributeFormatTypeNumber >= DataType.values().length)
            return null;

        DataType type = DataType.values()[attributeFormatTypeNumber];
        return getVertexDataByFormatType(type);
    }

    public float[] getVertexDataByFormatType(DataType type)
    {
        switch (type)
        {
            case POSITION:
                return positions;
            case COLOR:
                return colors;
            case TEXTURE_COORDS:
                return textureCoords;
            case NORMAL:
                return normals;
            default:
                return null;
        }
    }

    public boolean hasDataTypeByFormatType(int attributeFormatTypeNumber)
    {
        DataType type = DataType.values()[attributeFormatTypeNumber];
        return hasDataTypeByDataType(type);
    }

    public boolean hasDataTypeByDataType(DataType type)
    {
        switch (type)
        {
            case POSITION:
                return hasPositions();
            case COLOR:
                return hasColors();
            case TEXTURE_COORDS:
                return hasTextureCoords();
            case NORMAL:
                return hasNormals();
            default:
                return false;
        }
    }

    public int getFloatCountByFormatType(int attributeFormatTypeNumber)
    {
        if(attributeFormatTypeNumber < 0 || attributeFormatTypeNumber >= DataType.values().length)
            return 0;

        DataType type = DataType.values()[attributeFormatTypeNumber];
        return getFloatCountByDataType(type);
    }

    public int getFloatCountByDataType(DataType type)
    {
        if(!hasDataTypeByDataType(type))
            return 0;

        switch (type)
        {
            case POSITION:
                return this.positions.length;
            case COLOR:
                return this.colors.length;
            case TEXTURE_COORDS:
                return this.textureCoords.length;
            case NORMAL:
                return this.normals.length;
            default:
                return 0;
        }
    }

    public Vertex setPositions(float[] positions)
    {
        this.positions = positions;
        return this;
    }

    public Vertex setColors(float[] colors) {
        this.colors = colors;
        return this;
    }

    public Vertex setColors(Color color, boolean withAlpha)
    {
        this.colors = withAlpha ? new float[]{color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha()} :
                new float[]{color.getRed(),color.getGreen(),color.getBlue()};
        return this;
    }

    public Vertex setColors(Color color)
    {
        setColors(color,true);
        return this;
    }

    public Vertex setNormals(float[] normals) {
        this.normals = normals;
        return this;
    }

    public Vertex setTextureCoords(float[] textureCoords) {
        this.textureCoords = textureCoords;
        return this;
    }

    public float[] getPositions() {
        return positions;
    }

    public float[] getColors() {
        return colors;
    }

    public float[] getNormals() {
        return normals;
    }

    public float[] getTextureCoords() {
        return textureCoords;
    }

    public boolean hasPositions()
    {
        return this.positions != null && this.positions.length > 0;
    }

    public boolean hasColors()
    {
        return this.colors != null && this.colors.length > 0;
    }

    public boolean hasNormals()
    {
        return this.normals != null && this.normals.length > 0;
    }

    public boolean hasTextureCoords()
    {
        return this.textureCoords != null && this.textureCoords.length > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Arrays.equals(positions, vertex.positions) &&
                Arrays.equals(colors, vertex.colors) &&
                Arrays.equals(normals, vertex.normals) &&
                Arrays.equals(textureCoords, vertex.textureCoords);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(positions);
        result = 31 * result + Arrays.hashCode(colors);
        result = 31 * result + Arrays.hashCode(normals);
        result = 31 * result + Arrays.hashCode(textureCoords);
        return result;
    }
}
