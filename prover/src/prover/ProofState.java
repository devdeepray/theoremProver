package prover;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import aStar.Node;
import aStar.WeightNode;

public class ProofState implements Node<ProofState> {

	public int origin = 0;
	public TreeSet<ExprTree> hypSet;
	
	public ProofState(TreeSet<ExprTree> initHypSet)
	{
		this.hypSet = initHypSet;
	}
	
	@Override
	public double hval(ProofState goal) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<WeightNode<ProofState>> children() {
		List<WeightNode<ProofState>> res = new LinkedList<WeightNode<ProofState>>();
		
		// Insert possibilities with modus ponens
		for (ExprTree x1: hypSet)
		{
			for (ExprTree x2: hypSet)
			{
				
				
				if(x2.left != null && x1.isSame(x2.left))
				{
					//System.out.println("<Pair>");
					//System.out.println(x1);
					//System.out.println(x2);
					//System.out.println("</Pair>");
					
					TreeSet<ExprTree> newHypSet = new TreeSet<ExprTree>(hypSet);
					newHypSet.add(x2.right);
					newHypSet.add(Parser.contraPositive(x2.right));
					ProofState newState = new ProofState(newHypSet);
					newState.origin = 1;
					WeightNode<ProofState> newNode = new WeightNode<ProofState>(newState, 1);
					res.add(newNode);
				}
			}
		}
		
		// If F on right of some hypothesis
		for(ExprTree x: hypSet)
		{
			// If x is not a proposition and its RHS is F
			if(x.right != null && x.right.proposition != null &&  x.right.proposition.equals("F"))
			{
				TreeSet<ExprTree> tmpset = Parser.deduce(x.left);
				TreeSet<ExprTree> newSet = new TreeSet<ExprTree>(hypSet);
				newSet.remove(x);
				newSet.remove(Parser.contraPositive(x));
				newSet.addAll(tmpset);
				ProofState newState = new ProofState(newSet);
				newState.origin = 2;
				WeightNode<ProofState> newNode = new WeightNode<ProofState>(newState, 1);
				res.add(newNode);
			}
		}
		
		return res;
	}

	@Override
	public void display() {		
		System.out.println(this.toString());
	}
	
	@Override
	public String toString()
	{
		String res = "";
		
		for (ExprTree hyp : hypSet) {
			res += hyp.toString() + "\n";
		}
		return res;
	}

	@Override
	public boolean isSame(ProofState n2) {
		if(n2.hypSet.contains(ExprTree.falseTree)) {
			return this.hypSet.contains(ExprTree.falseTree);
		}
		
		for(ExprTree x : n2.hypSet)
		{
			if(hypSet.contains(x)) continue;
			else
			{
				return false;
			}
		}
		return hypSet.size() == n2.hypSet.size();
	}

	@Override
	public int compareTo(ProofState o) {
		return (this.toString()+origin).compareTo(o.toString()+o.origin);
	}

	@Override
	public boolean checkReachability(ProofState goal) {
		return true;
	}

}
