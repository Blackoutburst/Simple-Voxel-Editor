#version 410

#define ambientStrength 0.5
#define specularStrength 0.1

uniform vec3 viewPos;

in vec3 FragPos;
in vec4 Color;
in vec3 Normal;

out vec4 FragColor;

void main() {
    vec3 normal = normalize(Normal);
    vec3 lightDir = vec3(0.4, 0.5, 0.25);
    vec3 lightColor = vec3(1.0, 1.0, 1.0);

    float diff = max(dot(normal, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;

    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 reflectDir = reflect(-lightDir, normal);

    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 0.0);
    vec3 specular = specularStrength * spec * lightColor;

    vec3 ambient = ambientStrength * lightColor;

    vec3 result = (ambient + diffuse + specular) * Color.xyz;

    FragColor = vec4(result, Color.w);
}