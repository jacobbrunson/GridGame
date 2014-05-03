package me.jacobbrunson.GridGame;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Location;

import java.util.HashSet;
import java.util.PriorityQueue;

public class PathfinderActor extends Actor {

	private PriorityQueue<Node> open;
	private HashSet<Node> closed = new HashSet<Node>();
	private Node[][] nodes;

	private Actor target;

	public PathfinderActor(Actor target) {
		this.target = target;
		setColor(null);
	}

	public void act() {
		if (target == null) {
			return;
		}
		nodes = new Node[getGrid().getNumCols()][getGrid().getNumRows()];
		for (int row = 0; row < getGrid().getNumRows(); row++) {
			for (int column = 0; column < getGrid().getNumCols(); column++) {
				nodes[row][column] = new Node(new Location(row, column));
			}
		}
		Node destination = nodes[target.getLocation().getRow()][target.getLocation().getCol()];
		Node currentNode = nodes[getLocation().getRow()][getLocation().getCol()];
		open = new PriorityQueue<Node>(11, new NodeComparator(destination));
		closed.clear();
		open.add(currentNode); //1
		boolean foundPath = false;
		while ((currentNode = open.poll()) != null) {
			if (currentNode.equals(destination)) {
				foundPath = true;
				break;
			}
			closed.add(currentNode);
			for (Location adjacentLocation : getGrid().getValidAdjacentLocations(currentNode.location)) { //2c
				Node adjacentNode = nodes[adjacentLocation.getRow()][adjacentLocation.getCol()];
				if (!closed.contains(adjacentNode) && currentNode.location.getDirectionToward(adjacentLocation) % 90 == 0 && !(getGrid().get(adjacentLocation) instanceof Rock)) {
					int tempG = currentNode.getG() + 1;
					if (!open.contains(adjacentNode) || tempG < adjacentNode.getG()) {
						adjacentNode.parent = currentNode;
						open.add(adjacentNode);
					}
				}
			}
		}
		if (foundPath) {
			foundPath = false;
			Node path = destination;
			while (path.parent != null && path.parent.parent != null) {
				path = path.parent;
			}
			moveTo(path.location);
		} else {
			setDirection(getDirection() + Location.HALF_RIGHT);
		}
	}

}
