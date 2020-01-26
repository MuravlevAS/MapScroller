package ru.sgk.simulator.input.mouse;

import ru.sgk.simulator.input.mouse.events.Dispatcher;
import ru.sgk.simulator.input.mouse.events.Event;
import ru.sgk.simulator.input.mouse.events.Layer;
import ru.sgk.simulator.input.mouse.events.types.MouseMotionEvent;
import ru.sgk.simulator.input.mouse.events.types.MousePressedEvent;
import ru.sgk.simulator.input.mouse.events.types.MouseReleasedEvent;

public class MouseHandler extends Layer
{
	private int x, y, keyCode;
	private boolean pressed, moved;
	private boolean released;
	
	public int getX() 
	{
		return x;
	}
	
	public int getY() 
	{
		return y;
	}

	public boolean isMoved() 
	{
		return moved;
	}
	
	public boolean isPressed() 
	{
		return pressed;
	}

	public boolean isReleased() 
	{
		return released;
	}
	
	public void setReleased(boolean released)
	{
		this.released = released;
	}
	
	public int getKeyCode() 
	{
		return keyCode;
	}
	
	public void onEvent(Event event) 
	{
		Dispatcher dispatcher = new Dispatcher(event);
		dispatcher.dispatch(Event.Type.MOUSE_PRESSED, (Event e) -> onPressed((MousePressedEvent) e));
		dispatcher.dispatch(Event.Type.MOUSE_RELEASED, (Event e) -> onReleased((MouseReleasedEvent) e));
		dispatcher.dispatch(Event.Type.MOUSE_MOVED, (Event e) -> onMoved((MouseMotionEvent) e));
	}
	

	private boolean onPressed(MousePressedEvent event)
	{
		pressed = true;
		keyCode = event.getKeyCode();
		System.out.println("Pressed: " + event.getKeyCode());
		return pressed;
	}

	private boolean onReleased(MouseReleasedEvent event)
	{
		pressed = false;
		released = true;
		keyCode = event.getKeyCode();
		System.out.println("Released " + event.handled + ": " + event.getKeyCode());
		return true;
	}

	private boolean onMoved(MouseMotionEvent event)
	{
		x = event.getX();
		y = event.getY();
		pressed = event.isDragged();
		System.out.println("Moved " + event.handled + ": " + x + " | " + y);
		return true;
	}
	
}