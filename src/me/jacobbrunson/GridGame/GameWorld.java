package me.jacobbrunson.GridGame;

import info.gridworld.actor.Actor;
import info.gridworld.actor.ActorWorld;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class GameWorld<T> extends ActorWorld implements KeyEventDispatcher {

	PlayerActor player;
	JFrame frame;

	public void generate() {
		Tree[][] sets = new Tree[12][12];
		ArrayList<Rock> edges = new ArrayList<Rock>();
		for (int x = 0; x < 25; x++) {
			for (int y = 0; y < 25; y++) {
				if (x % 2 == 0 || y % 2 == 0) {
					Rock rock = new Rock();
					add(new Location(y, x), rock);
					if ((x % 2 == 1 || y % 2 == 1) && x != 0 && y != 0 && x != 24 && y != 24) {
						edges.add(rock);
					}
				} else {
					sets[x / 2][y / 2] = new Tree();
				}
			}
		}
		do {
			int what = (int) (Math.random() * edges.size());
			Rock selected = edges.get(what);
			Location hi = selected.getLocation();
			Tree setA;
			Tree setB;
			if (hi.getRow() % 2 == 0) {
				setA = sets[hi.getCol() / 2][hi.getRow() / 2 - 1];
				setB = sets[hi.getCol() / 2][hi.getRow() / 2];
			} else {
				setA = sets[hi.getCol() / 2 - 1][hi.getRow() / 2];
				setB = sets[hi.getCol() / 2][hi.getRow() / 2];
			}
			if (!setA.isConnectedTo(setB)) {
				selected.removeSelfFromGrid();
				setB.connectTo(setA);
			}
			edges.remove(what);
		} while (edges.size() > 0);

		player = new PlayerActor();
		add(new Location(23, 23), player);
		add(new Location(1, 1), new PathfinderActor(player));
	}

	public GameWorld(Grid<Actor> grid) {
		super(grid);
	}

	@Override
	public void show() {
		super.show();
		try {
			Field f = getClass().getSuperclass().getSuperclass().getDeclaredField("frame");
			f.setAccessible(true);
			 frame = (JFrame) f.get(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getID() == KeyEvent.KEY_PRESSED) {
			Location current = player.getLocation();
			Location target = current;
			switch (event.getKeyCode()) {
				case KeyEvent.VK_D:
					target = current.getAdjacentLocation(Location.EAST);
					break;
				case KeyEvent.VK_A:
					target = current.getAdjacentLocation(Location.WEST);
					break;
				case KeyEvent.VK_W:
					target = current.getAdjacentLocation(Location.NORTH);
					break;
				case KeyEvent.VK_S:
					target = current.getAdjacentLocation(Location.SOUTH);
					break;
			}
			if (getGrid().isValid(target) && getGrid().get(target) == null) {
				player.moveTo(target);
				step();
				frame.repaint();
			}
		}
		return true;
	}
}
