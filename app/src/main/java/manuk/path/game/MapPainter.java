package manuk.path.game;

import android.graphics.Color;

class MapPainter {
	private static Painter painter;
	static final int LEFT = 0, RIGHT = 1, BACK = 2, FRONT = 3, BOTTOM = 4, TOP = 5;
	static final int[] MAP_COLOR = new int[6];
	private static final double VIEW_STRETCH_Z = Engine.BLOCK_WIDTH / 10;
	
	// local variables, defined here to avoid allocation
	private static double[] bottomCoord, topCoord;
	private static double leftBottomX, leftTopX, rightBottomX, rightTopX, backBottomY, backTopY, frontBottomY, frontTopY;
	private static double[] leftX = null, rightX = null, sideX = null, backY = null, frontY = null, sideY = null, topX = null, topY = null; // todo: check if this is any benefit
	private static double[] coord = new double[4];
	
	static void init(Painter painter) {
		MapPainter.painter = painter;
		int red = 6, green = 12, blue = 6;
		int i = 1;
		MAP_COLOR[FRONT] = Color.rgb(red * i, green * i, blue * i);
		i++;
		MAP_COLOR[TOP] = Color.rgb(red * i, green * i, blue * i);
		i++;
		MAP_COLOR[LEFT] = Color.rgb(red * i, green * i, blue * i);
		i++;
		MAP_COLOR[RIGHT] = Color.rgb(red * i, green * i, blue * i);
		i++;
		MAP_COLOR[BACK] = Color.rgb(red * i, green * i, blue * i);
		i++;
		MAP_COLOR[BOTTOM] = Color.rgb(red * i, green * i, blue * i);
	}
	
	static void drawFlat(double x, double y, double z, int color) {
		bottomCoord = toPaintCoord(x, y, z, 1, 1);
		painter.drawRect(bottomCoord[0], bottomCoord[1], bottomCoord[2], bottomCoord[3], color);
	}
	
	static void drawBlock(double x, double y, double z, boolean[] side, int color) {
		bottomCoord = toPaintCoord(x, y, z, 1, 1);
		topCoord = toPaintCoord(x, y, z + 1, 1, 1);
		
		// x
		leftBottomX = bottomCoord[0];
		leftTopX = topCoord[0];
		rightBottomX = leftBottomX + bottomCoord[2];
		rightTopX = leftTopX + topCoord[2];
		
		// y
		backBottomY = bottomCoord[1];
		backTopY = topCoord[1];
		frontBottomY = backBottomY + bottomCoord[3];
		frontTopY = backTopY + topCoord[3];
		
		// fill
		
		if (side[LEFT]) {
			leftX = new double[] {leftTopX, leftBottomX, leftBottomX, leftTopX};
			sideY = new double[] {backTopY, backBottomY, frontBottomY, frontTopY};
			painter.drawPolygon(leftX, sideY, MAP_COLOR[LEFT], false);
		}
		
		if (side[RIGHT]) {
			rightX = new double[] {rightTopX, rightBottomX, rightBottomX, rightTopX};
			if (!side[LEFT])
				sideY = new double[] {backTopY, backBottomY, frontBottomY, frontTopY};
			painter.drawPolygon(rightX, sideY, MAP_COLOR[RIGHT], false);
		}
		
		if (side[BACK]) {
			sideX = new double[] {leftTopX, rightTopX, rightBottomX, leftBottomX};
			backY = new double[] {backTopY, backTopY, backBottomY, backBottomY};
			painter.drawPolygon(sideX, backY, MAP_COLOR[BACK], false);
		}
		
		if (side[FRONT]) {
			if (!side[BACK])
				sideX = new double[] {leftTopX, rightTopX, rightBottomX, leftBottomX};
			frontY = new double[] {frontTopY, frontTopY, frontBottomY, frontBottomY};
			painter.drawPolygon(sideX, frontY, MAP_COLOR[FRONT], false);
		}
		
		if (side[TOP]) {
			topX = new double[] {leftTopX, rightTopX, rightTopX, leftTopX};
			topY = new double[] {backTopY, backTopY, frontTopY, frontTopY};
			painter.drawPolygon(topX, topY, MAP_COLOR[TOP], false);
		}
		
		// outline
		
		if (side[LEFT])
			painter.drawPolygon(leftX, sideY, Color.WHITE, true);
		
		if (side[RIGHT])
			painter.drawPolygon(rightX, sideY, Color.WHITE, true);
		
		if (side[BACK])
			painter.drawPolygon(sideX, backY, Color.WHITE, true);
		
		if (side[FRONT])
			painter.drawPolygon(sideX, frontY, Color.WHITE, true);
		
		if (side[TOP])
			painter.drawPolygon(topX, topY, Color.WHITE, true);
	}
	
	private static double[] toPaintCoord(double x, double y, double z, double width, double height) {
		coord = new double[4];
		double stretchX = Engine.BLOCK_WIDTH + VIEW_STRETCH_Z * z;
		double stretchY = Engine.BLOCK_HEIGHT + VIEW_STRETCH_Z * z;
		coord[0] = (x - Engine.VIEW_WIDTH / 2.) * stretchX + .5;
		coord[1] = (y - Engine.VIEW_HEIGHT / 2.) * stretchY + .5;
		coord[2] = width * stretchX;
		coord[3] = height * stretchY;
		return coord;
	}
}
