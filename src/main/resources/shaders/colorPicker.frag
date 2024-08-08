#version 410 core

in vec2 uv;

uniform vec4 color;

out vec4 FragColor;

void main() {

    vec3 topMix = mix(vec3(1.0, 1.0, 1.0), color.rgb, uv.x);

    vec3 bottomMix = vec3(0.0, 0.0, 0.0);

    vec3 finalColor = mix(topMix, bottomMix, 1 - uv.y);

    FragColor = vec4(finalColor, color.a);
}