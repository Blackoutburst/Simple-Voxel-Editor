#version 410

layout(location = 0) in vec3 aPos;
layout(location = 1) in vec4 aColor;

uniform mat4 view;
uniform mat4 projection;

out vec4 Color;

void main() {
    Color = aColor;

    gl_Position =  projection * view * vec4(aPos, 1.0);
}