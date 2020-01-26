package ru.sgk.simulator.input.mouse.events.types;

import ru.sgk.simulator.input.mouse.events.Event;

public class MouseReleasedEvent extends MouseButtonEvent
{
	public MouseReleasedEvent(int keyCode, int x, int y) {
		super(Event.Type.MOUSE_RELEASED, keyCode, x, y);
	}
}
