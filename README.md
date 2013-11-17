Java Raytracer
==============

Project description
-------------------

A Simple Java raytracer written in Java 2D.

The code is based in [Ray Tracing from the Ground Up book][1] and in [this excelent tutorial of flipcode][2].
With some refactorings, corrections and ideas of my own.

Change History
--------------
- 17/11/2013 Ambient occlusion.
- 16/11/2013 [pic1][10][pic2][11] - Disk and hemisphere sample mapping and a Thin Lens Camera. **v1.6**
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

[1]: http://www.raytracegroundup.com/ "Ray tracing from the Ground Up - Kevin Suffern"
[2]: http://www.flipcode.com/archives/Raytracing_Topics_Techniques-Part_1_Introduction.shtml "Flipcode Raytracing Tutorial"
[3]: https://lh6.googleusercontent.com/-lN7F5S4R1LQ/UnbEfeIkyyI/AAAAAAAAAm8/Pameytz-NdQ/w2166-h1218-no/raytracer+HD.png
[4]: https://lh6.googleusercontent.com/-941Nz-j3HlE/UnrwnRnmSAI/AAAAAAAAAn8/R_aC_oi5x9U/w2166-h1218-no/raytracer+HD+2.png
[5]: https://lh3.googleusercontent.com/--yIntWMu9xo/Un-9o1V4ZmI/AAAAAAAAApM/FAdwmTFHOE0/w2166-h1218-no/JavaTracer-v1_2.png
[6]: https://lh4.googleusercontent.com/-cITg5h_czQs/UoGNOa3_1PI/AAAAAAAAAqk/TgiUZUK_wPc/w2166-h1218-no/JavaTracer-v1_3.png
[7]: https://lh4.googleusercontent.com/-bvztZYWeD3g/UobIjPY42HI/AAAAAAAAAr8/YYlYYyUTPG8/w2052-h1154-no/JavaTracer-v1_4.png
[8]: https://lh6.googleusercontent.com/-XPU4i0vyCoI/Uobl0yz1RdI/AAAAAAAAAss/mvfhykmbREg/w2052-h1154-no/JavaTracer-v1_5.png
[9]: https://lh3.googleusercontent.com/-uuXz46IKQdQ/Uofs_QJIgKI/AAAAAAAAAt8/oBLMQSbrTeo/w2236-h1258-no/Java+Raytracer-v1_5_Billiard.png
[10]: https://lh6.googleusercontent.com/-9lyr5F29fq8/Uogopv2JY3I/AAAAAAAAAus/_RlVGEBpDaI/w2166-h1218-no/Java+Raytracer-v1_6_Billiard.png
[11]: https://lh5.googleusercontent.com/-sbpa31fUUqg/UogsAEPO72I/AAAAAAAAAvM/ZqereRyMQ0A/w2166-h1218-no/Java+Raytracer-v1_6_Billiard_2.png