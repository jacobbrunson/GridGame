package me.jacobbrunson.GridGame;

import info.gridworld.actor.Actor;
import info.gridworld.grid.BoundedGrid;

public class Game {

	public static void main(String[] args) {
		GameWorld world = new GameWorld(new BoundedGrid<Actor>(25, 25));
		world.generate();
		world.show();
	}

}
