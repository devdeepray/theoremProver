package prover;

import java.util.HashSet;

/*
 * TheoremState class. Has the set of hypotheses and the expression tree for the theorem to be proved
 */
public class TheoremState {
	HashSet<ExprTree> hypSet; // Hypothesis set
	ExprTree result; // Result expression tree
	
	TheoremState(String theorem) {
		hypSet = new HashSet<ExprTree> ();
		result = Parser.parse(Parser.preprocess(theorem)); // Initialize result with the theorem tree
	}
	
	void deduce() {
		ExprTree current = result;
		while(current.proposition == null) {
			hypSet.add(current.left);
			current =  current.right;
		}
		
		hypSet.add(current.negation());
		result = ExprTree.falseTree;
	}
	
	@Override
	public String toString() {
		String res = "";
		
		for (ExprTree hyp : hypSet) {
			res += hyp.toString() + "\n";
		}
		
		res += "-----------------------------------\n" + result.toString();
		
		return res;
	}
}
