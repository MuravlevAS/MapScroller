package ru.sgk.simulator.input.mouse.events.types;

import ru.sgk.simulator.input.mouse.events.Event;

public class MousePressedEvent extends MouseButtonEvent 
{
	public MousePressedEvent(int keyCode, int x, int y) {
		super(Event.Type.MOUSE_PRESSED, keyCode, x, y);
	}
	
}
