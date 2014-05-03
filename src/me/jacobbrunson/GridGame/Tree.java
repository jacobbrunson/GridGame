package me.jacobbrunson.GridGame;

public class Tree {

	Tree parent = null;

	public Tree getRoot() {
		return parent == null ? this : parent.getRoot();
	}

	public boolean isConnectedTo(Tree tree) {
		return getRoot().equals(tree.getRoot());
	}

	public void connectTo(Tree tree) {
		tree.getRoot().parent = this;
	}

}
