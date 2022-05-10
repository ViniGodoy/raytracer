Java Raytracer
==============

Project description
-------------------

A Simple Java raytracer written in Java 2D.

The code is based in [Ray Tracing from the Ground Up book][1] and in [this excellent tutorial of flipcode][2].
With some refactorings, corrections and ideas of my own.

Change History
--------------
- 09/05/2022 Updated project to Java 18
- 23/03/2014 Updated project to Java 8
- 29/12/2013 [pic][15] - Added compound objects and some default compound beveled objects. **v1.7**
- 27/12/2013 [pic][14] - Added transformations.
- 26/12/2013 [pic][13] - Box, Torus, Annulus and Open Cylinder primitives. Added ConvexPartSpheres.
- 24/11/2013 [pic][12] - Area lights, bounding box, triangle and rectangle geometric objects.
- 17/11/2013 Ambient occlusion.
- 16/11/2013 [pic1][10] [pic2][11] - Disk and hemisphere sample mapping and a Thin Lens Camera. **v1.6**
- 15/11/2013 [pic1][8] [pic2][9] - Created the billiard scene and added shadows. **v1.5**
- 15/11/2013 [pic][7] - Interlaced2 drawing order and specular reflections. **v1.4**
- 13/11/2013 Updated tracer to gradually trace the scene with given drawing order.
- 12/11/2013 Fixed Lambertian BRDF and changed demo light to point light.
- 11/11/2013 [pic][6] - Implemented ambient and directional light and matte material. **v1.3**
- 10/11/2013 [pic][5] - Implemented a Pin Hole configurable perspective camera. **v1.2**
- 09/11/2013 Refactored sampling classes and added Multi-Jittered sampling algorithm.
- 06/11/2013 [pic][4] - Added Random, Jittered, N-Hooks and Hammersley sampling algorithms. **v1.1**
- 04/11/2013 Added regular sampling for anti-aliasing.
- 03/11/2013 [pic][3] - Restarted from scratch with a new architecture. **v1.0**
- 16/06/2013 Added refraction.
- 10/06/2013 Refactored ray tracer classes.
- 02/06/2013 First version of ray tracer.

[1]: https://github.com/hadryansalles/ray-tracing-from-the-ground-up "Ray tracing from the Ground Up - Kevin Suffern"
[2]: http://www.flipcode.com/archives/Raytracing_Topics_Techniques-Part_1_Introduction.shtml "Flipcode Raytracing Tutorial"
[3]: /examples/01_balls.png
[4]: /examples/02_balls.png
[5]: /examples/03_balls.png
[6]: /examples/04_balls.png
[7]: /examples/05_balls.png
[8]: /examples/06_balls.png
[9]: /examples/07_balls.png
[10]: /examples/08_billiard.png
[11]: /examples/09_billiard.png
[12]: /examples/10_billiard.png
[13]: /examples/11_billiard.png
[14]: /examples/11_billiard.png
[15]: /examples/12_objects.png