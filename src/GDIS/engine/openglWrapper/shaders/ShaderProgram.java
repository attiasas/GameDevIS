package GDIS.engine.openglWrapper.shaders;

import GDIS.engine.openglWrapper.shaders.uniforms.Uniform;
import GDIS.tools.debugger.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

/**
 * Created By: Assaf, On 15/10/2021
 * Description:
 */
public class ShaderProgram {

    /**
     * Stores the handle of the program.
     */
    protected final int id;

    private List<Shader> shaderList = new ArrayList<>();


    /**
     * Creates a shader program.
     */
    public ShaderProgram() {
        id = glCreateProgram();
        Logger.get(getClass()).log(Logger.Level.DEBUG,"Created [ID="+ id + "]");
    }

    /**
     * Attach a shader to this program.
     *
     * @param shaders Shaders to get attached
     */
    public void attachShaders(Shader... shaders)
    {
        for(Shader shader : shaders)
        {
            shaderList.add(shader);
        }
    }

    /**
     * Binds the fragment out color variable.
     *
     * @param number Color number you want to bind
     * @param name   Variable name
     */
    public void attachOutputAttributes(int number, CharSequence name) {
        glBindFragDataLocation(id, number, name);
    }

    /**
     * Link this program and check it's status afterwards.
     */
    public void link()
    {
        if(shaderList.isEmpty())
            throw new IllegalStateException("The Program does not have shaders attached!");

        for(Shader shader : shaderList)
        {
            glAttachShader(id, shader.getID());
        }

        Logger.get(getClass()).log(Logger.Level.DEBUG,"Link [ID="+ id + "] To Shaders");
        glLinkProgram(id);

        checkStatus();

        for(Shader shader : shaderList)
        {
            glDetachShader(id, shader.getID());
            shader.delete();
        }
    }

    /**
     * Gets the location of an attribute variable with specified name.
     *
     * @param name Attribute name
     *
     * @return Location of the attribute
     */
    public int getAttributeLocation(CharSequence name) {
        return glGetAttribLocation(id, name);
    } // TODO: REMOVE

    /**
     * Enables a vertex attribute.
     *
     * @param location Location of the vertex attribute
     */
    public void enableVertexAttribute(int location) {
        glEnableVertexAttribArray(location);
    } // TODO: REMOVE

    /**
     * Disables a vertex attribute.
     *
     * @param location Location of the vertex attribute
     */
    public void disableVertexAttribute(int location) {
        glDisableVertexAttribArray(location);
    } // TODO: REMOVE

    /**
     * Sets the vertex attribute pointer.
     *
     * @param location Location of the vertex attribute
     * @param size     Number of values per vertex
     * @param stride   Offset between consecutive generic vertex attributes in
     *                 bytes
     * @param offset   Offset of the first component of the first generic vertex
     *                 attribute in bytes
     */
    public void pointVertexAttribute(int location, int size, int stride, int offset) { // TODO: REMOVE
        glVertexAttribPointer(location, size, GL_FLOAT, false, stride, offset);
    }

    public void setAllUniformLocations(Uniform... uniforms)
    {
        for(Uniform uniform : uniforms)
        {
            uniform.storeUniformLocation(id);
        }
    }

    public void setAllUniformLocations(List<Uniform> uniforms)
    {
        for(Uniform uniform : uniforms)
        {
            uniform.storeUniformLocation(id);
        }
    }

    public int getId()
    {
        return this.id;
    }

    /**
     * Use this shader program.
     */
    public void use()
    {
        glUseProgram(id);
        Logger.get(getClass()).log(Logger.Level.DEBUG,"Use [ID="+ id + "]");
    }

    public void release()
    {
        glUseProgram(0);
        Logger.get(getClass()).log(Logger.Level.DEBUG,"Release [ID="+ id + "]");
    }

    /**
     * Checks if the program was linked successfully.
     */
    public void checkStatus() {
        int status = glGetProgrami(id, GL_LINK_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetProgramInfoLog(id));
        }
    }

    /**
     * Deletes the shader program.
     */
    public void delete() {
        glDeleteProgram(id);
        Logger.get(getClass()).log(Logger.Level.DEBUG,"Delete [ID="+ id + "]");
    }

}
