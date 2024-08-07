#version 410 core

uniform sampler2D diffuseMap;

in vec2 uv;
in vec3 color;

out vec4 FragColor;

void main() {
    FragColor = vec4(vec3(color), 1.0) * texture(diffuseMap, uv);
}