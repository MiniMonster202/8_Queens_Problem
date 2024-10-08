class Reina{
    // ATRIBUTOS
    protected int row, col;
    protected Reina neighbor;

    // CONSTRUCT
    public Reina(int row,int col){
        neighbor = null;
        this.row = row;
        this.col = col;
    }

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
    		// System.out.println("siguiente Soy la reina: " + col);
    		// System.out.println("estoy en fila " + row);
    		return searchfree();		
	    }
    }
    public boolean first_solve(){ // PRIMERA
	    // System.out.println("primera Soy la reina: " + col);	
		if(neighbor!=null && neighbor.first_solve()) return searchfree(); // Si la vecina ya tiene primera solución busco yo
	    else return true; // si no tengo vecina, entonces estoy bien colocado
	}
     public boolean searchfree(){ // PRUEBAOAVANZA    
    	if(neighbor!=null && neighbor.threat(row,col)) return next_solve(); // busco la siguiente solucion si estoy amenazado y tengo vecina
    	else{
            // System.out.println("Encontré sitio"); // hasta aqui viene de primera nextsolve true 
            return true; // si no hay amenaza tampoco busco sitio porque ya estoy bien colocada
    	} 
    }
    public boolean threat(int r,int c){ // PUEDEATACAR
       // System.out.println("Reina "+c+" buscando amenazas");
        int dc = col - c;          	
		if(r==row) return true;
		if(row+dc==r||row-dc==r) return true;
		if(neighbor!=null) return neighbor.threat(r,c);
		else return false;
    }
}

class tablero{
    protected static int max_cells;
    
    public static void draw(Reina[] q){
        for (int i=0;i<max_cells;i++){
            for (int j=0;j<max_cells;j++){
                if(i==q[j].row && j==q[j].col) 
                    System.out.print(" R ");                
                else System.out.print(" . ");
            }
        System.out.println();
        }
    }
}

public class ReinasApp{
    public static void main(String[] args){
        int counter=2;
        tablero.max_cells = 8;
        Reina[] queens = new Reina[tablero.max_cells];
        queens[0] = new Reina(0,0);
        for(int i=1;i<tablero.max_cells;i++){
            queens[i] = new Reina(0,i);
            queens[i].neighbor = queens[i-1];
            }
        System.out.println("Solución 1");
        queens[tablero.max_cells-1].first_solve(); // inicializamos la ultima y ejecutamos en cascada
        tablero.draw(queens);
        System.out.println("----------------");
        while(queens[0].row!=queens[1].row){
            System.out.println("Solución "+counter);
            queens[tablero.max_cells-1].next_solve();
            tablero.draw(queens);
            System.out.println("----------------");
            counter++;
        }
        System.out.println("La última solución valida es la "+ (counter-2));
    }
}
