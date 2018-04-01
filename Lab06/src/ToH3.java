
public class ToH3 {
	public enum StackChooser {s1,s2,s3};
	public static Stack<Integer> s1 = new Stack<Integer>();
	public static Stack<Integer> s2 = new Stack<Integer>();
	public static Stack<Integer> s3 = new Stack<Integer>();
	
	/*public static void doHanoi(Stack<Integer> from, Stack<Integer> to, Stack<Integer> aux,StackChooser sfrom,StackChooser sto, StackChooser saux) {
		
		if(from.size() == 1) {
			System.out.println("Move: " + from.peek() + " from Stack: " + sfrom + " to: " + sto);
			switch(sto.ordinal()) {
			case 0: s1.push(from.pop());	break;
			case 1: s2.push(from.pop());	break;
			case 2: s3.push(from.pop()); 	break;
		}
			switch(sfrom.ordinal()) {
				case 0: s1.pop();	break;
				case 1: s2.pop();	break;
				case 2: s3.pop(); 	break;
			}
			System.out.println("Stack 1\n" + s1.toString());
			System.out.println("Stack 2\n" + s2.toString());
			System.out.println("Stack 3\n" + s3.toString());
			System.out.println("===============================");
			return;
		}
		else {
			Stack<Integer> subStack = stackCopy(from);
			System.out.println("SUB Stack\n" + subStack.toString());
			doHanoi(subStack, aux, to, sfrom, saux, sto);
			doHanoi(aux, to, from, sfrom, saux, sto);
		}
	}
	
	public static Stack<Integer> stackCopy(Stack<Integer> from){
		
		Stack<Integer> temp1 = new Stack<Integer>();
		Stack<Integer> temp2 = new Stack<Integer>();
		for(int i = 1;i<from.size();i++) {
			temp1.push(from.getValueAtIndex(i));
		}
		return temp1;
	}*/
	
	static void towerOfHanoi(int n,StackChooser from_rod, StackChooser to_rod,StackChooser aux_rod)
    {
        if (n == 1)
        {
            System.out.println("Move disk 1 from rod " +  from_rod + " to rod " + to_rod);
            switch(to_rod.ordinal()) {
				case 0: {
					switch(from_rod.ordinal()) {
					case 1: s1.push(s2.pop());	break;
					case 2: s1.push(s3.pop());	break;
					}break;
				}
				case 1: {
					switch(from_rod.ordinal()) {
					case 0: s2.push(s1.pop());	break;
					case 2: s2.push(s3.pop());	break;
					}break;
				}
				case 2: {
					switch(from_rod.ordinal()) {
					case 0: s3.push(s1.pop());	break;
					case 1: s3.push(s2.pop());	break;
					}break;
				}
			
            }
            System.out.println("Stack 1\n" + s1.toString());
    		System.out.println("Stack 2\n" + s2.toString());
    		System.out.println("Stack 3\n" + s3.toString());
            return;
        }
        towerOfHanoi(n-1, from_rod, aux_rod, to_rod);
        System.out.println("Move disk " + n + " from rod " +  from_rod + " to rod " + to_rod);
        switch(to_rod.ordinal()) {
		case 0: {
			switch(from_rod.ordinal()) {
			case 1: s1.push(s2.pop());	break;
			case 2: s1.push(s3.pop());	break;
			}break;
		}
		case 1: {
			switch(from_rod.ordinal()) {
			case 0: s2.push(s1.pop());	break;
			case 2: s2.push(s3.pop());	break;
			}break;
		}
		case 2: {
			switch(from_rod.ordinal()) {
			case 0: s3.push(s1.pop());	break;
			case 1: s3.push(s2.pop());	break;
			}break;
		}
	
    }
        towerOfHanoi(n-1, aux_rod, to_rod, from_rod);
    }
	
	
	public static int steps(int disc) {
		if(disc == 1) {
			return 1;
		}
		else{
			int stepscount = 2*steps(disc-1) + 1;
			return stepscount;
		}
		
	}
	public static void main(String[] args) {
		

		s1.push(3);
		s1.push(2);
		s1.push(1);
		//doHanoi(s1, s3, s2,StackChooser.s1,StackChooser.s2,StackChooser.s3);
		towerOfHanoi(3, StackChooser.s1, StackChooser.s3, StackChooser.s2);
		System.out.println("Steps: " + steps(3));
		
		
	}

}
