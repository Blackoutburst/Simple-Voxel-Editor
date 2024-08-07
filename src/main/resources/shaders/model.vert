#version 410

layout(location = 0) in vec3 aPos;
layout(location = 1) in vec4 aColor;
layout(location = 2) in float aNorm;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec3 FragPos;
out vec4 Color;
out vec3 Normal;

vec3 getNormal(int index) {
    const vec3 normals[6] = vec3[6](
    vec3(0.0f, 1.0f, 0.0f), // TOP
    vec3(0.0f, 0.0f, -1.0f), // FRONT
    vec3(0.0f, 0.0f, 1.0f), // BACK
    vec3(-1.0f, 0.0f, 0.0f), // LEFT
    vec3(1.0f, 0.0f, 0.0f), // RIGHT
    vec3(0.0f, -1.0f, 0.0f) // BOTTOM
    );
    return normals[index];
}

void main() {
    FragPos = aPos;
    Color = aColor;
    Normal = getNormal(int(aNorm));

    gl_Position =  projection * view * model * vec4(FragPos, 1.0);
}