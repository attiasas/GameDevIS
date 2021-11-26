# OpenGL Wrapper Objects Package

## Infrastructures Guide
Provides simple, easy to use and flexible (General) infrastructures for rendering using OpenGL Objects, hides the complexity
provides the basics. Extend (for complex features or specific usage) or use those object for rendering.

---
> ##### <u>_VaoUser_</u>
> Description:
>
> Usage & Example:
> ```java
> 
> ```
---
> ##### <u>_VaoFormat_</u>
> Description:
>
> Usage & Example:
> ```java
> 
> ```
---
> ##### <u>_AdvShaderProgram_</u>
> Description:
>
> Usage & Example:
> ```java
> 
> ```
---

## Basic Components
Object that Wrap the openGL methods and objects. provides only simple interface and no infrastructures.

| Name          | Information & Usage                                                                                |  
| ------------- | -------------------------------------------------------------------------------------------------- |
| _Vbo_         | Vertex Buffer Object - the basic buffer that stores data for rendering.                            |
| _Vao_         | Vertex Array Object - an array that holds vbo, if more than one vbo exists for the render data.    |
| _Attribute_   | Attribute inside a Vertex Buffer Object - represents the input variables of a shader program.      |
| _SmartVao_    | The basic objects that holds the data that is sent into the GPU for rendering. combines Vbo & Vao. |