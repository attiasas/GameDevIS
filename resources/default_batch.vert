#version 150

in vec3 position;
in vec4 color;
in vec2 texcoord;

in mat4 transform;
uniform float useTransform;

uniform mat4 view;
uniform mat4 projection;

out vec4 vertexColor;
out vec2 textureCoord;

void main() {

    vec4 worldPosition = vec4(position,1.0);
    if(useTransform > 0)
    {
        worldPosition = transform * worldPosition;
    }

    vec4 positionRelativeToCam = view * worldPosition;
    gl_Position = projection * positionRelativeToCam;

    vertexColor = color;
    textureCoord = texcoord;

}
