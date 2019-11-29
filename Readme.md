#Target Disquisition
## Overview
This project is an independent, Java/Swing implementation of some of the ideas found in the 
[Analyze Targets](https://www.shootingsoftware.com/target.htm) module of 
[Shooting Lab™](https://www.shootingsoftware.com/ballistics.htm), 
published by [Recreational Software, Inc](https://www.shootingsoftware.com/index.htm).

The idea is to provide quantitative analysis of shot grouping of a single marksmanship target.

## History
The original version of this project was written in the early 2000's with XCode, as an OS X native
Quartz application. Sometime around 2010ish, it was ported to pure Java/Swing, 
to be run from a jar file on any platform.

##Run
`java -jar TagetDisquisition.jar`

##Usage
1. (optional) click preferences, choose English (imperial) or Metric (SI) units.
2. Calibrate the screen: Click the calibrate button and follow the directions.  You need a ruler.
3. Click the shoot button, and enter the distance the target was shot from.
4. If needed, poke a hole through the target at the point of aim.
5. Hold the target in front of the screen.  Without moving it:
    - Click to enter point of aim.
    - Click each shot.
    - If needed, shift-click to remove the previous shot.
6. Click the "Done" button to display analysis.