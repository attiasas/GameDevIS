package GDIS.engine.openglWrapper.basics;

import GDIS.tools.debugger.Logger;

import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_2_10_10_10_REV;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created By: Assaf, On 15/10/2021
 * Description: Represent an Attribute of a vbo.
 *              An attribute is the data that is transfer to a input variable inside a shader
 */
public class Attribute
{
    /**
     * The name of the input attribute of the shader program. has to match to the name of the input.
     */
    private CharSequence name;

    /**
     * The location of the attribute, the index identifier (ID) of the attribute in a specific shader program.
     */
    protected int location = -1;

    /**
     * The number of data, of the specific type, for the input
     */
    private int count;

    /**
     * The data type of this attribute: from GL11.DataTypes. (FL_FLOAT,GL_INT...)
     */
    private int type;

    /**
     * Is normalize data
     */
    private boolean normalized;

    /**
     * The format type number of the attribute. allows to extract data from Vertex by decide mapping numbers.
     */
    private int formatTypeNumber = -1;

    /**
     * The Default value for this attribute,
     * allows to specify optional attributes to load general and vary data to the same shader program.
     * this data will be return and stored for a data that does not contain this format type number.
     */
    protected Object defaultValue = null;

    /**
     * Constructor
     * @param name - the name of the attribute, has to match the name of the input variable in the shader program.
     * @param count - the number of data slots of data types that this attribute needed
     * @param type - the data type of this attribute (from GL11 -> DataTypes)
     * @param normalized - is the attribute data needed normalize
     * @param formatTypeNumber - the mapper number to extract data from VaoData
     * @param defaultValue - a default data that will be return in a data extract from VaoData if not exists in it.
     *                       if null this attribute is not optional. value has to match the attribute definition.
     */
    public Attribute(CharSequence name, int count, int type, boolean normalized, int formatTypeNumber, Object defaultValue)
    {
        this.name = name;
        this.count = count;
        this.type = type;
        this.normalized = normalized;
        this.formatTypeNumber = formatTypeNumber;
        setOptional(defaultValue);
    }

    /**
     * Constructor - for Required Attribute (no Default value)
     * @param name - the name of the attribute, has to match the name of the input variable in the shader program.
     * @param count - the number of data slots of data types that this attribute needed
     * @param type - the data type of this attribute (from GL11 -> DataTypes)
     * @param normalized - is the attribute data needed normalize
     * @param formatTypeNumber - the mapper number to extract data from VaoData
     */
    public Attribute(CharSequence name, int count, int type, boolean normalized, int formatTypeNumber)
    {
        this(name,count,type,normalized,formatTypeNumber,null);
    }

    /**
     * Constructor
     * @param name - the name of the attribute, has to match the name of the input variable in the shader program.
     * @param count - the number of data slots of data types that this attribute needed
     * @param type - the data type of this attribute (from GL11 -> DataTypes)
     * @param formatTypeNumber - the mapper number to extract data from VaoData
     * @param defaultValue - a default data that will be return in a data extract from VaoData if not exists in it.
     *                       if null this attribute is not optional. value has to match the attribute definition.
     */
    public Attribute(CharSequence name, int count, int type, int formatTypeNumber, Object defaultValue)
    {
        this(name,count,type,false,formatTypeNumber,defaultValue);
    }

    /**
     * Constructor
     * @param name - the name of the attribute, has to match the name of the input variable in the shader program.
     * @param count - the number of data slots of data types that this attribute needed
     * @param type - the data type of this attribute (from GL11 -> DataTypes)
     * @param defaultValue - a default data that will be return in a data extract from VaoData if not exists in it.
     *                       if null this attribute is not optional. value has to match the attribute definition.
     */
    public Attribute(CharSequence name, int count, int type, Object defaultValue)
    {
        this(name,count,type,-1,defaultValue);
    }

    /**
     * Constructor - for Required Attribute (no Default value)
     * @param name - the name of the attribute, has to match the name of the input variable in the shader program.
     * @param count - the number of data slots of data types that this attribute needed
     * @param type - the data type of this attribute (from GL11 -> DataTypes)
     */
    public Attribute(CharSequence name, int count, int type)
    {
        this(name,count,type,null);
    }

    /**
     * Constructor - for Required Attribute (no Default value)
     * @param name - the name of the attribute, has to match the name of the input variable in the shader program.
     * @param count - the number of data slots of data types that this attribute needed
     * @param type - the data type of this attribute (from GL11 -> DataTypes)
     * @param formatTypeNumber - the mapper number to extract data from VaoData
     */
    public Attribute(CharSequence name, int count, int type, int formatTypeNumber) {
        this(name,count,type,false,formatTypeNumber);
    }

    /**
     * Check if the given default value matches the attribute definition.
     * @param defaultValue - a default data that will be return in a data extract from VaoData if not exists in it.
     * @return true if matches or null (no default), false otherwise
     */
    private boolean isValidDefaultType(Object defaultValue)
    {
        if(defaultValue == null) return true; // Not Optional, has no default value

        if(type == GL_FLOAT && (defaultValue instanceof float[]))
        {
            return ((float[]) defaultValue).length == count;
        }

        return false;
    }

    /**
     * set the optional value of this attribute, if the default equals to null this attribute is required without default.
     * @param defaultValue - value to set. has to match the attribute
     * @return this attribute to chain settings. throws exception if not matches
     */
    public Attribute setOptional(Object defaultValue)
    {
        if(!isValidDefaultType(defaultValue))
            throw new IllegalArgumentException("Default Value Provided does not match to the other attribute arguments that was given (type and/or count)");

        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return location == attribute.location &&
                count == attribute.count &&
                type == attribute.type &&
                normalized == attribute.normalized &&
                name.equals(attribute.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, count, type, normalized);
    }

    /**
     * calculate the size in bytes of this attribute
     * @return the size in bytes of this attribute
     */
    public int getSize()
    {
        int bytePerType;
        switch (type)
        {
            case GL_UNSIGNED_INT_2_10_10_10_REV:
                return Integer.BYTES;
            case GL_UNSIGNED_INT:
            case GL_INT:
            case GL_FLOAT:
                bytePerType = Float.BYTES;
                break;
            case GL_UNSIGNED_BYTE:
            case GL_BYTE:
                bytePerType = Byte.BYTES;
                break;
            case GL_UNSIGNED_SHORT:
            case GL_SHORT:
                bytePerType = Short.BYTES;
            default:
                // error "Unsupported data type for VAO attribute"
                bytePerType = 0;
        }

        return count * bytePerType;
    }

    /**
     * Link the attribute to the given program and stores the location. after calling this method, you need to call the
     * method linkToVbo
     * @param programId - the id of the program that this attribute is stored in
     * @return the location (ID) of this attribute in the program.
     */
    public int linkToProgram(int programId)
    {
        location = glGetAttribLocation(programId, name);

        if(location < 0)
            throw new IllegalStateException("Attribute '" + name + "' does not exist in the given program");
        Logger.get(getClass()).log(Logger.Level.DEBUG,"[name=" + name + "] Link To Program [ID=" + programId + "] -> location = "+ location + "");
        return location;
    }

    /**
     * Specify the position of this attribute inside the vbo relative to the data of a render object.
     * this method can only be called after this attribute is linked with a program.
     * @param stride - the total size in bytes that the render object stored in
     * @param offset - the offset from the start of the object attributes that the current attribute data starts in.
     */
    public void linkToVbo(int stride, int offset) {
        if(location < 0)
            throw new IllegalStateException("Attribute has not been link to a program");

        enable();
        Logger.get(getClass()).log(Logger.Level.DEBUG,"Link To VBO [name=" + name + "+, location="+ location + "]");
        glVertexAttribPointer(location, count, type, false, stride, offset);
    }

    /**
     * Enable the attribute to use inside the shader program. has to be called before draw calls
     */
    public void enable()
    {
        if(location < 0)
            throw new IllegalStateException("Attribute has not been link to program");

        Logger.get(getClass()).log(Logger.Level.DEBUG,"Enable [name=" + name + "+, location="+ location + "]");
        glEnableVertexAttribArray(location);
    }

    /**
     * Disable the attribute
     */
    public void disable()
    {
        if(location < 0)
            throw new IllegalStateException("Attribute has not been link to program");
        Logger.get(getClass()).log(Logger.Level.DEBUG,"Disable [name=" + name + "+, location="+ location + "]");
        glDisableVertexAttribArray(location);
    }

    /**
     * Get the number of data slots that this attribute needs
     * @return the count (number of data slots) that this attributes need
     */
    public int getCount()
    {
        return count;
    }

    /**
     * is this attribute is normalized
     * @return - true if normalized
     */
    public boolean isNormalized() {
        return normalized;
    }

    /**
     * Get the data type (range in GL11. DataTypes) of this attribute
     * @return - the data type of this attribute
     */
    public int getType() {
        return type;
    }

    /**
     * Get the Variable name of this attribute, the same name that is defined in the input shader program
     * @return the name of this attribute
     */
    public CharSequence getName()
    {
        return name;
    }

    /**
     * Get the format type number of the attribute. allows to extract data from Vertex by decide mapping numbers.
     * @return the format type number
     */
    public int getFormatTypeNumber() {
        return formatTypeNumber;
    }

    /**
     * Sets the format type number of the attribute. allows to extract data from Vertex by decide mapping numbers.
     * @param formatTypeNumber - the mapping number to set as the format type number
     */
    public void setFormatTypeNumber(int formatTypeNumber) {
        this.formatTypeNumber = formatTypeNumber;
    }

    /**
     * return if this attribute is optional (have default value) or required.
     * @return true if optional, false otherwise
     */
    public boolean isOptional()
    {
        return defaultValue != null;
    }

    /**
     * get the default value of this attribute, if this attribute is optional.
     * @return the default value of this attribute, if null is return this attribute is required.
     */
    public Object getDefaultValue() { return defaultValue; }

    @Override
    public String toString() {
        return "Attribute{" +
                "name=" + name +
                ", location=" + location +
                ", dataCount=" + count +
                ", sizeInBytes=" + getSize() +
                ", formatTypeNumber=" + formatTypeNumber +
                ", optional=" + isOptional() +
                '}';
    }
}
