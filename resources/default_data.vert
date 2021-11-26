#version 150

in vec3 position;
in vec4 color;
in vec2 texcoord;

out vec4 vertexColor;
out vec2 textureCoord;

uniform mat4 transform;
uniform mat4 view;
uniform mat4 projection;

void main() {

    vec4 worldPosition = transform * vec4(position,1.0);
    vec4 positionRelativeToCam = view * worldPosition;
    gl_Position = projection * positionRelativeToCam;

    vertexColor = color;
    textureCoord = texcoord;
}
