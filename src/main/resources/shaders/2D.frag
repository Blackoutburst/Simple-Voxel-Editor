#version 410 core

uniform sampler2D diffuseMap;

in vec2 uv;
out vec4 FragColor;

void main() {
    FragColor = vec4(1.0) * texture(diffuseMap, uv);
    //FragColor = vec4(uv,0.0, 1.0);
}