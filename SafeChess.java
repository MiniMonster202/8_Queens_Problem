import java.util.Scanner;

// ACTUAL ERROR: LAS REINAS OCUPAN LAS DIAGONALES DE LAS TORRES QUE NO SON VECINAS INMEDIATAS

class Pieza{
     // ATRIBUTOS
    protected int row, col;
    protected Pieza neighbor;
    protected boolean SolicitaReina;
    

    // METODOS
    public boolean next_solve(){ // SIGUIENTE
	    if(row==7){
    	    if(neighbor!=null && neighbor.next_solve()){ // Si tiene vecina y ya tiene solución, busco yo	
    			row = 0;
    			return searchfree(); 	
    		}else return false; // Si no, no existe siguiente solucion
	    }
    	else{
    		row++;
    		return searchfree();		
	    }
    }
    public boolean first_solve(){ // PRIMERA
		if(neighbor!=null && neighbor.first_solve()) return searchfree(); // Si la vecina ya tiene primera solución busco yo
	    else return true; // si no tengo vecina, entonces estoy bien colocado
	}
     public boolean searchfree(){ // PRUEBAOAVANZA    
    	if(neighbor!=null && neighbor.threat(row,col)) return next_solve(); // busco la siguiente solucion si estoy amenazado y tengo vecina
    	else return true; // si no hay amenaza tampoco busco sitio porque ya estoy bien colocada
    }
    public boolean threat(int r,int c){ // PUEDEATACAR
        int dc = col - c;
		if(r==row) return true;
		if(row+dc==r||row-dc==r) return true;
		if(neighbor!=null) return neighbor.threat(r,c);
		else return false;
    }
}

class Reina extends Pieza{
    // CONSTRUCT
    public Reina(int row,int col){
        neighbor = null;
        this.row = row;
        this.col = col;
    }
    public boolean searchfree(){ // PRUEBAOAVANZA    
    	SolicitaReina = false;
    	if(neighbor!=null && neighbor.threat(row,col)) return next_solve(); // busco la siguiente solucion si estoy amenazado y tengo vecina
    	if(neighbor instanceof Torre){ // para evitar que mi vecino ocupe la diagonal de la torre
    	    SolicitaReina = true;
    	    Torre torrevecina = (Torre) neighbor;
    	    if(torrevecina.threatReina(row,col)) return next_solve();
    	    return true;
    	}
    	else return true; // si no hay amenaza tampoco busco sitio porque ya estoy bien colocada
    }
    public boolean threat(int r,int c){ // PUEDEATACAR
        int dc = col - c;
		SolicitaReina = false;
		if(r==row) return true;
		if(row+dc==r||row-dc==r) return true;
		if(neighbor instanceof Torre){ // para evitar que los vecinos de mi vecino ocupen la diagonal de la torre
		    SolicitaReina = true;
		    Torre torrevecina = (Torre) neighbor;
		    return torrevecina.threatReina(r,c);
		}
		if(neighbor!=null) return neighbor.threat(r,c);
		else return false;
    }
}

class Torre extends Pieza{
    // CONSTRUCT
    public Torre(int row,int col){
        neighbor = null;
        this.row = row;
        this.col = col;
    }

    // METODOS
    public boolean threat(int r,int c){ // PUEDEATACAR
        int dc = col - c;
        SolicitaReina = false;
		if(r==row) return true;
		if(neighbor!=null) return neighbor.threat(r,c);
		else return false;
    }
    public boolean threatReina(int r, int c){ // distinguimos para chequear las diagonales de las reinas (no funca creo)
        int dc = col - c;
		SolicitaReina = true;
		if(r==row) return true;
		if(row+dc==r||row-dc==r) return true;
		if(neighbor instanceof Torre){ // para evitar que los vecinos de mi vecino ocupen la diagonal de la torre (No funca creo)
		    Torre torrevecina = (Torre) neighbor;
		    return torrevecina.threatReina(r,c);
		}
		if(neighbor!=null) return neighbor.threat(r,c);
		else return false;
    }
}

class tablero{
    protected static int max_cells;
    
    public static void draw(Pieza[] p){
        for (int i=0;i<max_cells;i++){
            for (int j=0;j<max_cells;j++){
                if(i==p[j].row && j==p[j].col) 
                    if(p[j] instanceof Reina) System.out.print(" R ");
                    else System.out.print(" T ");
                else System.out.print(" . ");
            }
        System.out.println();
        }
    }
}

public class SafeChess{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int counter=2;
        tablero.max_cells = 8;
        Pieza[] pieces = new Pieza[tablero.max_cells];
        System.out.println("Procede a colocar las "+tablero.max_cells+" piezas");
        for(int i=0;i<tablero.max_cells;i++){
            System.out.print("Que tipo de pieza quieres colocar R o T: ");
            String tipo = scanner.next();
            if(tipo.equalsIgnoreCase("R")) pieces[i] = new Reina(0,i);
            else pieces[i] = new Torre(0,i);
            if(i!=0) pieces[i].neighbor = pieces[i-1];
            }
        System.out.println("Solución 1");
        pieces[tablero.max_cells-1].first_solve(); // inicializamos la ultima y ejecutamos en cascada
        tablero.draw(pieces);
        System.out.println("----------------");
        while(pieces[0].row!=pieces[1].row){
            System.out.println("Solución "+counter);
            pieces[tablero.max_cells-1].next_solve();
            tablero.draw(pieces);
            System.out.println("----------------");
            counter++;
        }
        System.out.println("La última solución valida es la "+ (counter-2));
        scanner.close();
    }
}