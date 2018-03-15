# // TODO: {name} Runner

A game inspired by Google Chrome's T-rex Runner using Java and OpenGL.  
Semester project for Computer Graphics 4053/5053

## Install

~~So I followed this guide to get JOGL set up with my Eclipse configuration on my computer:~~
~~https://solarianprogrammer.com/2014/12/08/getting-started-jogl-java-opengl-eclipse/~~

~~It should work for you guys. Also, I'm on Java 1.8.0_162. I don't think it should matter but if for whatever reason it's not
working then maybe that is why.~~

via gradle
``` bash
$ gradle installDist
```  
in eclipse  
```
1. Right click on gradle task in Eclipse/Gradle Tasks plugin window
2. Open tab 'Java Home'
3. Set Java home to C:\Program Files\Java\jdk1.8.0_131\jre
  *directory may vary depending on the jdk version*
```

## Usage
(working on making this more convenient)

``` bash
Run the project.bat file in /build/install/template/bin/
```

## Notes
@[Stephon](https://github.com/SGordon94) -  
Also for when you look at the KeyHandler class you'll see these lines:
```Java
runner.getCanvas().getInputMap().put(KeyStroke.getKeyStroke("SPACE"), JUMP);
runner.getCanvas().getActionMap().put(JUMP, new JumpAction(runner));
```

Those are Java Key Bindings which I ended up using because for some reason the way that we did it in the homework wasn't working
even though I had it working on the homework.

I named the project Runner just cuz I didn't wanna call it Project the whole time. If y'all wanna change it go ahead.

Lastly, I used the projection stuff from the homework so I guess this means for our project the origin is in the middle
of the window. I used floats the do the location of the square and apparently the range for it on both the x and y is between
0.0 and 1.0. So like if I wanted to put the square at the very bottom of the window but keep it in the middle of the screen
horizontally then I would set it to (0.0, -1.0f). Not sure why it's like that but that how it works.

Not gonna lie the code is pretty ugly but getting the square to jump was a BITCH so for now I'm done with it lmao
