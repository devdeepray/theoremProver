package aStar;

import java.util.List;

public interface Node<T extends Node<T>> extends Comparable<T>{
	
	
	
	public double hval(T goal);

	
	public List<WeightNode<T>> children();


	public void display();


	public boolean isSame(T n2);

	@Override
	public int compareTo(T o);
	
	public boolean checkReachability(T goal);

}
