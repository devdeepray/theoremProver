package aStar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class AStarBidirectional<NodeType extends Node<NodeType>> {
	public List<NodeType> search(NodeType start, NodeType goal)
	{
		
		
		if(!start.checkReachability(goal)) return null;
		
		// Statistics
		int nNexpanded = 0, nNseen = 1;
		
		
		// Open set fwd search ordered on (fval, hval, nodeState)
		TreeSet<WeightNode<NodeType>> openNodesFwd = new TreeSet<WeightNode<NodeType>>();
		
		// Open set bwd search ordered on (fval, hval, nodeState)
		TreeSet<WeightNode<NodeType>> openNodesBwd = new TreeSet<WeightNode<NodeType>>();
		
		// Closed set fwd search ordered on nodeState
		TreeSet<NodeType> closedNodesFwd = new TreeSet<NodeType>();
		
		// Closed set bwd search ordered on nodeState
		TreeSet<NodeType> closedNodesBwd = new TreeSet<NodeType>();
		
		// Weight map: map from nodeState to (gVal in openNodes) or (min gVal).
		// Actually is a map of min gval for all nodes in openset + closed set
		TreeMap<NodeType, Double> weightMapFwd = new TreeMap<NodeType, Double>();
		TreeMap<NodeType, Double> weightMapBwd = new TreeMap<NodeType, Double>();
		
		// Parent map
		TreeMap<NodeType, NodeType> parentMapFwd = new TreeMap<NodeType, NodeType>();
		TreeMap<NodeType, NodeType> parentMapBwd = new TreeMap<NodeType, NodeType>();
		
		openNodesFwd.add(new WeightNode<NodeType>(start, start.hval(goal)));
		openNodesBwd.add(new WeightNode<NodeType>(goal, goal.hval(start)));
		weightMapFwd.put(start, 0d);
		weightMapBwd.put(goal, 0d);
		
		while(!openNodesFwd.isEmpty() && !openNodesFwd.isEmpty())
		{
			
			WeightNode<NodeType> currentNodeFwd = openNodesFwd.first();
			WeightNode<NodeType> currentNodeBwd = openNodesBwd.first();
			
			openNodesFwd.remove(currentNodeFwd);
			openNodesBwd.remove(currentNodeBwd);
			
			double cgvalfwd = currentNodeFwd.weight - currentNodeFwd.node.hval(goal);
			double cgvalbwd = currentNodeBwd.weight - currentNodeBwd.node.hval(start);
			
			@SuppressWarnings("unused")
			double chvalfwd = currentNodeFwd.weight - cgvalfwd;
			@SuppressWarnings("unused")
			double chvalbwd = currentNodeBwd.weight - cgvalbwd;
			
			@SuppressWarnings("unused")
			double cfvalfwd = currentNodeFwd.weight;
			closedNodesFwd.add(currentNodeFwd.node);
			
			@SuppressWarnings("unused")
			double cfvalbwd = currentNodeBwd.weight;
			closedNodesBwd.add(currentNodeBwd.node);
			
			//currentNode.node.display();
			
			//!!!! Check for solution
			
			NodeType commonNode = null;
			boolean flag = false;
			// Both the picked nodes are the same
			if(currentNodeFwd.node.isSame(currentNodeBwd.node))
			{
				flag = true;
				commonNode = currentNodeBwd.node;
			}
			else if(closedNodesFwd.contains(currentNodeBwd.node))
			{
				flag = true;
				commonNode = currentNodeBwd.node;
			}
			else if(closedNodesBwd.contains(currentNodeFwd.node))
			{
				flag = true;
				commonNode = currentNodeFwd.node;
			}
			
			if(flag)//Now construct the path
			{
				LinkedList<NodeType> result  = new LinkedList<NodeType>();
				NodeType curnode = commonNode;
				while(true)
				{
					//curnode.display();
					curnode = parentMapFwd.get(curnode);
					result.addFirst(curnode);
					if(curnode.isSame(start)) break;
				}
				
				curnode = commonNode;
				result.addLast(commonNode);
				while(true)
				{
					//curnode.display();
					curnode = parentMapBwd.get(curnode);
					result.addLast(curnode);
					if(curnode.isSame(goal)) break;
				}
				
				System.out.println(nNexpanded+","+(weightMapFwd.size()+weightMapBwd.size())+","+nNseen+","+(result.size()-1)+",");	
				return result;
			}
			
			
			
			nNexpanded+=2;
			
			
			
			// Get all the children
			List<WeightNode<NodeType>> children = currentNodeFwd.node.children();
			for(WeightNode<NodeType> currentChild : children)
			{
				
				// Num of ppl seen
				nNseen++;
				
				
				Double curChildCurWeight = weightMapFwd.get(currentChild.node);
				Double curChildGval = cgvalfwd + currentChild.weight;
				
				if(curChildCurWeight == null)
				{
					weightMapFwd.put(currentChild.node, curChildGval);
					openNodesFwd.add(new WeightNode<NodeType>(currentChild.node, curChildGval 
																			+ currentChild.node.hval(goal)));
					parentMapFwd.put(currentChild.node, currentNodeFwd.node);
				}
				else if(curChildGval < curChildCurWeight)
				{
					// Inside closed set case
					if(closedNodesFwd.contains(currentChild.node))
					{
						closedNodesFwd.remove(currentChild.node);
						//System.out.println("asjdasd");
					}
					else
					{
						Double oldFval = curChildCurWeight + currentChild.node.hval(goal);
						openNodesFwd.remove(new WeightNode<NodeType>(currentChild.node, oldFval));
					}
					
					weightMapFwd.put(currentChild.node, curChildGval);
					openNodesFwd.add(new WeightNode<NodeType>(currentChild.node, curChildGval 
							+ currentChild.node.hval(goal)));
					parentMapFwd.put(currentChild.node, currentNodeFwd.node);
				}
				
				
			}
			
			children = currentNodeBwd.node.children();
			for(WeightNode<NodeType> currentChild : children)
			{
				
				// Num of ppl seen
				nNseen++;
				
				
				Double curChildCurWeight = weightMapBwd.get(currentChild.node);
				Double curChildGval = cgvalbwd + currentChild.weight;
				
				if(curChildCurWeight == null)
				{
					weightMapBwd.put(currentChild.node, curChildGval);
					openNodesBwd.add(new WeightNode<NodeType>(currentChild.node, curChildGval 
																			+ currentChild.node.hval(goal)));
					parentMapBwd.put(currentChild.node, currentNodeBwd.node);
				}
				else if(curChildGval < curChildCurWeight)
				{
					// Inside closed set case
					if(closedNodesBwd.contains(currentChild.node))
					{
						closedNodesBwd.remove(currentChild.node);
						//System.out.println("asjdasd");
					}
					else
					{
						Double oldFval = curChildCurWeight + currentChild.node.hval(goal);
						openNodesBwd.remove(new WeightNode<NodeType>(currentChild.node, oldFval));
					}
					
					weightMapBwd.put(currentChild.node, curChildGval);
					openNodesBwd.add(new WeightNode<NodeType>(currentChild.node, curChildGval 
							+ currentChild.node.hval(goal)));
					parentMapBwd.put(currentChild.node, currentNodeBwd.node);
				}
				
				
			}
			
			
			
		}
		return null;
		
	}
}