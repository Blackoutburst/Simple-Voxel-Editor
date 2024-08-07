#version 410 core

layout(location = 0) in vec2 aPos;
layout(location = 1) in vec2 aUv;
layout(location = 2) in vec3 aColor;

out vec2 uv;
out vec2 fragPos;
out vec3 color;

uniform mat4 projection;
uniform mat4 model;

void main() {
    uv = aUv;
    color = aColor;
    gl_Position = projection * model * vec4(aPos, 0.0, 1.0);
}