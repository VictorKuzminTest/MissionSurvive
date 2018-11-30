package com.missionsurvive.geom;

public class GeoHelper {
    public static final int TO_THE_RIGHT = 0, TO_THE_LEFT = 1, LOWER = 1, HIGHER = 0, INSIDE = 2;

    /**
     * Determines the space, based on the amount of space that a substance or object occupies.
     * @param eventX
     * @param eventY
     * @param objX
     * @param objY
     * @param objWidth
     * @param objHeight
     * @return
     */
    public static boolean inBoundsVolume(int eventX, int eventY, int objX, int objY, int objWidth, int objHeight) {
        if(eventX > objX && eventX < objX + objWidth - 1 &&
                eventY > objY && eventY < objY + objHeight - 1)
            return true;
        else
            return false;
    }

    /**
     * Determines the space directly through coordinates.
     * @param eventX
     * @param eventY
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public static boolean inBoundsSpace(int eventX, int eventY, int left, int top, int right, int bottom){
        if(eventX > left && eventX < right && eventY > top && eventY < bottom){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Determines whether eventX coordinate to the right of the object, to the left or inside the object space.
     * @param eventX
     * @param left
     * @param right
     * @return
     */
    public static int inBoundsSpaceX(int eventX, int left, int right){
        int xPos = 0;

        if(eventX > left && eventX < right){
            return xPos = INSIDE;
        }
        else if(eventX < left){
            return xPos = TO_THE_LEFT;
        }
        else if(eventX > right){
            return xPos = TO_THE_RIGHT;
        }
        return xPos;
    }

    /**
     * Determines whether eventY coordinate higher, lower or inside the object space.
     * @param eventY
     * @param top
     * @param bottom
     * @return
     */
    public static int inBoundsSpaceY(int eventY, int top, int bottom){
        int yPos = 0;

        if(eventY > top && eventY < bottom){
            return yPos = INSIDE;
        }
        else if(eventY > bottom){
            return yPos = LOWER;
        }
        else if(eventY < top){
            return yPos = HIGHER;
        }
        return yPos;
    }

    /**comments to public static boolean overlapRectangles(...).
       _____
     |_____|
     rect1LeftX < rect2RightX &&
     rect1RightX > rect2LeftX &&
     rect1BottomY > rect2TopY &&
     rect1TopY < rect2BottomY
          ____
        |____|

     if conditions are met, then rectangle overlap
     */
    public static boolean overlapRectangles(int obj1X, int obj1Y, int obj1Width, int obj1Height,
                                            int obj2X, int obj2Y, int obj2Width, int obj2Height){

        if(obj1X < obj2X + (obj2Width - 1) &&
                obj1X + (obj1Width - 1) > obj2X &&
                obj1Y + (obj1Height - 1) > obj2Y &&
                obj1Y < obj2Y + (obj2Height - 1)){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Calculates one of the grid's (spatial) coordinate (row or col) depending on screen event and "world offset".
     * @param screenCoord  coordinate of the screen (X or Y)
     * @param offset "world offset" (X or Y)
     * @return  grid coordinate (row or col)
     */
    public static int getSpatialGridCoord(int screenCoord, int offset, int gridSize, int cellSize){

        int gridCoord = (screenCoord + offset) / cellSize;

        if(gridCoord < 0) gridCoord = 0;
        if(gridCoord >= gridSize) gridCoord = gridSize - 1;

        return gridCoord;
    }

    /**
     * Gets row or col of the space grid (world grid). It checks for row (col)
     * couldn't go out from the grid space.
     * @param rowCol
     * @param worldSize
     * @return
     */
    public static int checkRowCol(int rowCol, int worldSize) {
        if (rowCol < 0) rowCol = 0;
        if (rowCol >= worldSize) rowCol = worldSize - 1;
        return rowCol;
    }

    /**
     * Gets float value: it checks for this value couldn't go out from the borders.
     * @param floatValue
     * @param borders
     * @return
     */
    public static float checkFloat(float floatValue, float borders) {
        if (floatValue < borders) floatValue = borders;
        if (floatValue > borders) floatValue = borders;
        return floatValue;
    }

    /**
     * Fastest method, based on Franklin Antonio's "Faster Line Segment Intersection" topic "
     * in Graphics Gems III" book (http://www.graphicsgems.org/)
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     * @return
     */
    public static boolean linesIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
        // return false if either of the lines have zero length
        if (x1 == x2 && y1 == y2 ||
                x3 == x4 && y3 == y4){
            return false;
        }

        double ax = x2-x1;
        double ay = y2-y1;
        double bx = x3-x4;
        double by = y3-y4;
        double cx = x1-x3;
        double cy = y1-y3;

        double alphaNumerator = by*cx - bx*cy;
        double commonDenominator = ay*bx - ax*by;
        if (commonDenominator > 0){
            if (alphaNumerator < 0 || alphaNumerator > commonDenominator){
                return false;
            }
        }else if (commonDenominator < 0){
            if (alphaNumerator > 0 || alphaNumerator < commonDenominator){
                return false;
            }
        }
        double betaNumerator = ax*cy - ay*cx;
        if (commonDenominator > 0){
            if (betaNumerator < 0 || betaNumerator > commonDenominator){
                return false;
            }
        }else if (commonDenominator < 0){
            if (betaNumerator > 0 || betaNumerator < commonDenominator){
                return false;
            }
        }
        if (commonDenominator == 0){
            // This code wasn't in Franklin Antonio's method. It was added by Keith Woodward.
            // The lines are parallel.
            // Check if they're collinear.
            double y3LessY1 = y3-y1;
            double collinearityTestForP3 = x1*(y2-y3) + x2*(y3LessY1) + x3*(y1-y2);   // see http://mathworld.wolfram.com/Collinear.html
            // If p3 is collinear with p1 and p2 then p4 will also be collinear, since p1-p2 is parallel with p3-p4
            if (collinearityTestForP3 == 0){
                // The lines are collinear. Now check if they overlap.
                if (x1 >= x3 && x1 <= x4 || x1 <= x3 && x1 >= x4 ||
                        x2 >= x3 && x2 <= x4 || x2 <= x3 && x2 >= x4 ||
                        x3 >= x1 && x3 <= x2 || x3 <= x1 && x3 >= x2){
                    if (y1 >= y3 && y1 <= y4 || y1 <= y3 && y1 >= y4 ||
                            y2 >= y3 && y2 <= y4 || y2 <= y3 && y2 >= y4 ||
                            y3 >= y1 && y3 <= y2 || y3 <= y1 && y3 >= y2){
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    /**
     * transformation of y canvas coordinate, which start from up-left corner to GL coords (bottom-left).
     * @param y
     * @param screenHeight
     * @return
     */
    public static int transformCanvasYCoordToGL(int y, int screenHeight, int objHeight){
        return (screenHeight - 1) - (y + (objHeight - 1));
    }
}
