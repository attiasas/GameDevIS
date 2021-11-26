#version 150

in vec4 vertexColor;
in vec2 textureCoord;

out vec4 fragColor;

uniform sampler2D texImage;

void main() {

    if(textureCoord.x >= 0 && textureCoord.y >= 0)
    {
        vec4 textureColor = texture(texImage, textureCoord);
        fragColor = vertexColor * textureColor;
    }
    else
    {
        fragColor = vertexColor;
    }
}
