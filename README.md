<img align="right" src="https://github.com/user-attachments/assets/8d074b45-4129-48df-b7f6-7174618a5a9d" height="130" width="130">

[![Release](https://img.shields.io/github/release/Blackoutburst/Simple-Voxel-Editor.svg?style=for-the-badge)](https://github.com/Blackoutburst/Simple-Voxel-Editor/releases)
[![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/Blackoutburst/Simple-Voxel-Editor/gradle.yml?style=for-the-badge)](https://github.com/Blackoutburst/Simple-Voxel-Editor/actions)


Simple Voxel Editor (aka SVE) is a simple software to create small voxelized 3D models

# Simple Voxel Editor


![image](https://github.com/user-attachments/assets/5ae5a28d-2c44-40f4-87ec-aac18feba535)


## Controls
- Break Voxel: **Left Mouse**
- Place Voxel: **Right Mouse**
- Rotate Camera: **Left Shift** + **Left Mouse**
- Move Camera: **Left Shift** + **Right Mouse**
- Toggle Grid: **G**
- Toggle Axys: **Q**
- Toggle Panel: **P**
- Reset Camera: **R**
- Open File: **O**
- Save File: **S**

## File Format
SVE files use the simplest format possible\
Each line represent a voxel
```SVE
// X Y Z R G B A
-1.0 0.0 0.0 0.36666667 0.22855559 0.1364815 1.0
-1.0 1.0 0.0 0.36666667 0.22855559 0.1364815 1.0
-1.0 2.0 0.0 0.36666667 0.22855559 0.1364815 1.0
-1.0 3.0 0.0 0.36666667 0.22855559 0.1364815 1.0
...
```

## Building
```
./gradlew build
```
