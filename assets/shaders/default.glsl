#type vertex
#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec2 aTextureCoords;




out vec2 fTextureCoords;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{

    fTextureCoords = aTextureCoords;
    gl_Position = projection * view * model * vec4(aPos,1.0);
}

#type fragment
#version 330 core

out vec4 color;
uniform sampler2D TEX_SAMPLER;

in vec2 fTextureCoords;

void main()
{
    color = texture(TEX_SAMPLER, fTextureCoords);
}