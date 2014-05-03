package me.jacobbrunson.GridGame;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

	public final Node destination;

	public NodeComparator(Node destination) {
		this.destination = destination;
	}

	@Override
	public int compare(Node nodeA, Node nodeB) {
		if (nodeA.getF(destination) < nodeB.getF(destination)) {
			return -1;
		} else {
			return 1;
		}
	}

}
