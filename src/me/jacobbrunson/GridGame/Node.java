package me.jacobbrunson.GridGame;

import info.gridworld.grid.Location;

public class Node {

	public Location location;
	public Node parent;

	public Node(Location location, Node parent) {
		this.location = location;
		this.parent = parent;
	}

	public Node(Location location) {
		this(location, null);
	}

	public int getF(Node destination) {
		return getG() + getH(destination);
	}

	public int getG() {
		if (parent == null) {
			return 0;
		}
		return parent.getG();
	}

	public int getH(Node node) {
		return Math.abs(node.location.getCol() - location.getCol()) + Math.abs(node.location.getRow() - location.getRow());
	}

	public boolean equals(Object o) {
		if (o instanceof Node) {
			Node node = (Node) o;
			return node.location.equals(location);
		}
		return false;
	}

}
