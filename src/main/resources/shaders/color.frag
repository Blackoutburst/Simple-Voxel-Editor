#version 410 core

uniform vec4 color;

in vec2 uv;
out vec4 FragColor;

void main() {
    FragColor = color;
}