package aStar;

public class WeightNode<T extends Node<T>> implements Comparable<WeightNode<T>>{
	public T node;
	double weight;
	
	public WeightNode(T node, double weight)
	{
		this.node = node;
		this.weight = weight;
	}

	@Override
	public int compareTo(WeightNode<T> o) {
		if(this.weight < o.weight) return -1;
		if(this.weight > o.weight) return 1;
		if(this.node.hval(node) < o.node.hval(node)) return -1;
		if(this.node.hval(node) > o.node.hval(node)) return 1;
		return this.node.compareTo(o.node);
		
	}
}
