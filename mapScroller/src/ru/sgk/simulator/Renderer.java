package ru.sgk.simulator;

import java.util.Random;

public class Renderer {
	
	private int width, height;
	private int[] pixels;
	private int tileIndex;
	private static final int MAP_SIZE = 32;
	private static final int MAP_SIZE_MASK = MAP_SIZE - 1;
	private static int tileSize = 16;
	private int[] tiles = new int[MAP_SIZE*MAP_SIZE];
	private static final Random random = new Random();
	
	public Renderer(int width, int height, int[] pixels)
	{
		this.width = width;
		this.height = height;
		this.pixels = pixels;
		fill();
	}
	
	public void fill() {
		for (int i = 0; i < MAP_SIZE*MAP_SIZE; i++) {
			tiles[i] = random.nextInt(0xfffffff);
		}
//		int x = 3;
//		int y = 6;
//		tiles[(x-1) + ((y-1) * (MAP_SIZE))] = 0xFFFFFF;
		for (int x = 0; x < MAP_SIZE; x++)
		{
			tiles[(x) + ((MAP_SIZE-1)*(MAP_SIZE))] = 0xFFFFFF;
		}
		for (int y = 0; y < MAP_SIZE; y++)
		{
			tiles[(MAP_SIZE-1) + ((y) * (MAP_SIZE))] = 0xffffff;
		}
	}
	
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	public void render(int xOffset, int yOffset) 
	{
		int xx;
		int yy;
		for (int y = 0; y < height; y++){
			yy = y + yOffset;
			for (int x = 0; x < width; x++){
				xx = x + xOffset;
				tileIndex = ((xx >> 4) & MAP_SIZE_MASK) + (((yy >> 4) & MAP_SIZE_MASK) * MAP_SIZE);
//				if (yy < tileSize * MAP_SIZE && xx < tileSize * MAP_SIZE && yy >=0 && xx >= 0)
					pixels[x + (y * width)] = tiles[tileIndex];
//				else pixels[x + (y * width)] = 0xffffff;
			}
		}
	}
	
}
