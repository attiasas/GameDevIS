package GDIS.engine.openglWrapper.basics;

import GDIS.tools.debugger.Logger;

import static org.lwjgl.opengl.GL30.*;

/**
 * Created By: Assaf, On 15/10/2021
 * Description: Represents and wrap the Vertex Array Object, an array of Vbo.
 */
public class Vao
{
    /**
     * Stores the handle of the VAO.
     */
    private final int id;

    /**
     * Creates a Vertex Array Object (VAO).
     */
    public Vao() {
        id = glGenVertexArrays();
        Logger.get(getClass()).log(Logger.Level.DEBUG,"Create [ID="+ id + "]");
    }

    /**
     * Binds the VAO.
     */
    public void bind() {
        Logger.get(getClass()).log(Logger.Level.DEBUG,"Bind [ID="+ id + "]");
        glBindVertexArray(id);
    }

    /**
     * Unbinds the VAO.
     */
    public void unbind() {
        glBindVertexArray(0);
        Logger.get(getClass()).log(Logger.Level.DEBUG,"Unbind [ID="+ id + "]");
    }

    /**
     * Deletes the VAO.
     */
    public void delete()
    {
        Logger.get(getClass()).log(Logger.Level.DEBUG,"Delete [ID="+ id + "]");
        glDeleteVertexArrays(id);
    }

    /**
     * Getter for the Vertex Array Object ID.
     * @return Handle of the VAO
     */
    public int getID() {
        return id;
    }

}
