package ru.sgk.simulator;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import ru.sgk.simulator.input.InputHandler;
import ru.sgk.simulator.input.mouse.MouseHandler;
import ru.sgk.simulator.input.mouse.events.Event;
import ru.sgk.simulator.input.mouse.events.types.MouseMotionEvent;
import ru.sgk.simulator.input.mouse.events.types.MousePressedEvent;
import ru.sgk.simulator.input.mouse.events.types.MouseReleasedEvent;
@SuppressWarnings("serial")
public class Main extends Canvas
{
	
	private static final int WIDTH = 900;
	private static final int HEIGHT = WIDTH / 16 * 9;
	private static final int SCALE = 1;
	private static String title = "Simulator"; 
	private JFrame frame = new JFrame();
	private boolean running = false;
	private BufferStrategy bs = null;
	private Graphics g = null;
	private Renderer renderer;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int pixels[] = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	private int x = 0;
	private int y = 0;
	private String currentTitle;
	private int speed = 1;
	private MouseHandler mouseHandler;
	
	private int px,py;

	private double mapForceX, mapForceY;
	public Main() {
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		renderer = new Renderer(WIDTH, HEIGHT, pixels);
		frame.addKeyListener(new InputHandler());

		mouseHandler = new MouseHandler();
		addMouseListener(new MouseAdapter() 
		{
			public void mousePressed(MouseEvent e)
			{
				MousePressedEvent event = new MousePressedEvent(e.getButton(), e.getX(), e.getY());
				onMouseEvent(event);
			}
			public void mouseReleased(MouseEvent e)
			{
				MouseReleasedEvent event = new MouseReleasedEvent(e.getButton(), e.getX(), e.getY());
				onMouseEvent(event);
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				MouseMotionEvent event = new MouseMotionEvent(e.getX(), e.getY(), false);
				onMouseEvent(event);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {

				MouseMotionEvent event = new MouseMotionEvent(e.getX(), e.getY(), true);
				onMouseEvent(event);
				
			}
		});

	}
	
	private void start() {
		running = true;
		init();
		
		new Thread(new Runnable() {
			public void run(){			
				long jvmLastTime = System.nanoTime();
				long time = System.currentTimeMillis(); 
				double jvmPartTime = 1_000_000_000.0 / 60.0;
				double delta = 0;
				int updates = 0;
				int frames = 0;
				while (running){
					long jvmNow = System.nanoTime();
					delta += (jvmNow - jvmLastTime);
					jvmLastTime = jvmNow;
					if (delta >= jvmPartTime){
						update();
						updates++;
						delta = 0;
					}
					render();
					frames++;
					if (System.currentTimeMillis() - time > 1000) {
						time += 1000;
						frame.setTitle(title + " | "  + "Updates: " + updates + ", " + "Frames: " + frames);
						currentTitle = frame.getTitle();
						updates = 0;
						frames = 0;
						
					}
				}
			}
		}).start();
	}
	private static double sigmoidStep(double x)
	{
		return 2/(1+Math.exp(-Math.abs(x/2)+3));
	}
	private void updateForceSigmoid()
	{

		if (mapForceX != 0)
		{
			if (mapForceX > 0)
				mapForceX-=sigmoidStep(mapForceX);
			else if (mapForceX < 0)
				mapForceX+=sigmoidStep(mapForceX);
			if (mapForceX > 50*speed)
				mapForceX = 50*speed;
			else if (mapForceX < -50*speed)
				mapForceX = -50*speed;
			else if (Math.abs(mapForceX) <= 0.5)
			{
				mapForceX = 0; 
			}
		}
		if (mapForceY != 0)
		{
			if (mapForceY > 0)
				mapForceY-=sigmoidStep(mapForceY);
			else if (mapForceY < 0)
				mapForceY+=sigmoidStep(mapForceY);
			if (mapForceY > 50*speed) 
				mapForceY = 50*speed;
			else if (mapForceY < -50*speed)
				mapForceY = -50*speed;
			else if (Math.abs(mapForceY) <= 0.5)
			{
				mapForceY = 0; 
			}
		}
	}
	
	private void updateForceLinear()
	{
		if (mapForceX != 0)
		{
			if (mapForceX > 0)
				mapForceX--;
			else if (mapForceX < 0)
				mapForceX++;
			if (mapForceX > 30*speed)
				mapForceX = 30*speed;
			else if (mapForceX < -30*speed)
				mapForceX = -30*speed;
			else if (Math.abs(mapForceX) <= 0.5)
			{
				mapForceX = 0; 
			}
		}
		if (mapForceY != 0)
		{
			if (mapForceY > 0)
				mapForceY--;
			else if (mapForceY < 0)
				mapForceY++;
			if (mapForceY > 30*speed) 
				mapForceY = 30*speed;
			else if (mapForceY < -30*speed)
				mapForceY = -30*speed;
			else if (Math.abs(mapForceY) <= 0.5)
			{
				mapForceY = 0; 
			}
		}
		
	}
	private void update() {
		int dx = (mouseHandler.getX() - px) * speed;
		int dy = (mouseHandler.getY() - py) * speed;
		if (mouseHandler.isPressed()) 
		{
			mapForceX = 0;
			mapForceY = 0;
			x -= dx;
			y -= dy;
			
			
			//System.out.println("Moved: " + x + " | " + y);
		}
		else if (mouseHandler.isReleased())
		{
//			if (mapForceX == 0)
				mapForceX = dx;
//			if (mapForceY == 0)
				mapForceY = dy;
		}
		px = mouseHandler.getX();
		py = mouseHandler.getY();
		x -= mapForceX;
		y -= mapForceY;
		updateForceSigmoid();
//		updateForceLinear();
		
//		if (InputHandler.isKeyPressed(KeyEvent.VK_UP)) y -= speed;
//		if (InputHandler.isKeyPressed(KeyEvent.VK_DOWN)) y +=speed;
//		if (InputHandler.isKeyPressed(KeyEvent.VK_LEFT)) x -=speed;
//		if (InputHandler.isKeyPressed(KeyEvent.VK_RIGHT)) x += speed;
		frame.setTitle(currentTitle + " " + x + " | " + y);
		mouseHandler.setReleased(false);
	}
	private void render() {
		
		if (bs == null)
		{
			createBufferStrategy(1);
			bs = getBufferStrategy();
		}
		renderer.clear();
		renderer.render(x, y);
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bufferSwap();
	}
	
	private void bufferSwap() {
		bs.show();
	}
	
	private void init() {
		frame.setResizable(false);
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		new Main().start();
	}
	
	private void onMouseEvent(Event e)
	{
		mouseHandler.onEvent(e);
	}
}
