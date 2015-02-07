package prover;

public class ExprTree implements Comparable<ExprTree>{
	public static final ExprTree falseTree = new ExprTree("F"); 
	String proposition = null;
	ExprTree left = null;
	ExprTree right = null;
	
	ExprTree () {
	}
	
	public ExprTree (String _prop) {
		this.proposition = _prop;
	}
	
	@Override
	public String toString() {
		if (proposition != null) return proposition;
		else {
			String lexpr, rexpr;
			if(left.proposition == null)
			{
				lexpr = "(" + left.toString() + ")";
			}
			else
			{
				lexpr = left.toString();
			}
			if(right.proposition == null)
			{
				rexpr = "(" + right.toString() + ")";
			}
			else
			{
				rexpr = right.toString();
			}
			return lexpr+ "-->" + rexpr;
		}
	}
	
	public ExprTree negation(){
		ExprTree neg = new ExprTree();
		neg.left = this;
		neg.right = falseTree;
		return neg;
	}
	
	boolean isSame(ExprTree tree) {
		//System.out.print(b)
		if ((this.proposition != null) && (tree.proposition != null)) return this.proposition.equals(tree.proposition);
		else if ((this.proposition != null) || (tree.proposition != null)) return false;
		else {
			return left.isSame(tree.left) && right.isSame(tree.right); 
		}
	}
	
	

	@Override
	public int compareTo(ExprTree o) {
		return this.toString().compareTo(o.toString());
	}
}

