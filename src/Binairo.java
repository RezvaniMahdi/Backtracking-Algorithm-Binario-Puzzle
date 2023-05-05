import java.util.*;

public class Binairo {
    private final ArrayList<ArrayList<String>> board;
    private final ArrayList<ArrayList<ArrayList<String>>> domain;
    private final int n;
    private State final_State;
    private int[][] siz;
    public Binairo(ArrayList<ArrayList<String>> board,
                   ArrayList<ArrayList<ArrayList<String>>> domain,
                   int n,int[][] size) {
        siz = new int[n][n];
        this.board = board;
        this.domain = domain;
        this.n = n;
        siz = size;
        //final_State;// = new State(board,domain,siz);
    }

    public void start() {
        long tStart = System.nanoTime();

        State state = new State(board, domain,siz);

        System.out.println("Initial Board: \n");
        state.printBoard();
        drawLine();
        if (backtrack(state)){
            System.out.println("Binairo is completed:)");
            final_State.printBoard();
        }else{
            System.out.println("its Impassable!!");
        }

        drawLine();
        long tEnd = System.nanoTime();
        System.out.println("Total time: " + (tEnd - tStart)/1000000000.000000000);
    }

    private boolean backtrack(State state){     /** recursive backtracking algorithm **/

        if (isFinished(state)){
            final_State = state;
            return true;
        }

      /*  if (ac3(state)){
            return false;
        }*/

        int[] ver = mrv(state);

        if (lcv(state,ver)){
                state.change_order_Domain(ver[0],ver[1]);

        }

        for (int i = 0; i < state.size_domain[ver[0]][ver[1]]; i++){

            state.setIndexBoard(ver[0],ver[1],state.getDomain().get(ver[0]).get(ver[1]).get(i));
            State st = state.copy();

            if (!forward_checking(st,ver)){
                if (state.size_domain[ver[0]][ver[1]] != 2){
                    return false;
                }
            }else{
                if (isConsistent(st)){

                    if (backtrack(st)){
                        return true;
                    }//else if (st.size_domain[ver[0]][ver[1]] == 1){
                   //     return false;
                  //  }//
                }
                 }


        }
    return false;
    }
    private boolean ac3(State  state){
        boolean contradiction = false;
        Queue<Integer> coordinates_y = new LinkedList<>();
        Queue<Integer> coordinates_x = new LinkedList<>();

        for (int i = 0; i < state.getBoard().size(); i++){

            for (int j = 0; j < state.getBoard().size(); j++){
                if (state.getBoard().get(i).get(j) == "E"){
                    coordinates_y.add(i);
                    coordinates_x.add(j);
                }
            }
        }
        while (!coordinates_x.isEmpty() && !contradiction && !coordinates_y.isEmpty()){
            int x = coordinates_x.poll();
            int y = coordinates_y.poll();

             for (int i = 0; i < state.getBoard().size();i++){
                if (state.getBoard().get(i).get(x) == "E"){
                    if (remove_values(state,x,y,x,i)){
                        if (state.size_domain[i][x] == 0){
                            contradiction = true;
                        }
                        coordinates_x.add(x);
                        coordinates_y.add(i);
                    }
                }
                if (state.getBoard().get(y).get(i) == "E"){
                    if (remove_values(state,x,y,i,y)){
                        if (state.size_domain[y][i] == 0){
                            contradiction = true;
                        }
                        coordinates_x.add(i);
                        coordinates_y.add(y);
                    }
                }

            }
        }
        return contradiction;
    }
    private boolean remove_values(State state,int xx,int xy,int yx, int yy){
        boolean remove = false;

        for (int i = 0; i < state.size_domain[yy][yx];i++){

            state.setIndexBoard(yy,yx,state.getDomain().get(yy).get(yx).get(i));
            int count = 0;
            if (!Objects.equals(state.getBoard().get(yy).get(yx), " ")){

                for (int j = 0; j < state.size_domain[xy][xx];j++){

                    state.setIndexBoard(xy,xx,state.getDomain().get(xy).get(xx).get(j));
                    if (!Objects.equals(state.getBoard().get(xy).get(xx), " ")){

                        if (isConsistent(state)){
                            count++;
                        }
                    }
                    state.setIndexBoard(xy,xx,"E");
                }
                if (count == 0 ){
                    state.removeIndexDomain(yy,yx,state.getDomain().get(yy).get(yx).get(i));
                    state.size_domain[yy][yx]--;
                    remove = true;
                }
            }
            state.setIndexBoard(yy,yx,"E");
        }
        return remove;
    }


    private boolean forward_checking(State state, int[] ver){   /** if we find a variable that has no value in his domain
                                                                    that means we have to backtrack**/
        for (int i = 0; i < state.getBoard().size(); i++){

            if (state.getBoard().get(i).get(ver[1]) == "E"){

                 remove_from_domain(state,i,ver[1]);
                if (state.size_domain[i][ver[1]] == 0){
                    return false;
                }
            }
            if (state.getBoard().get(ver[0]).get(i) == "E"){

                 remove_from_domain(state,ver[0],i);

                if (state.size_domain[ver[0]][i] == 0){
                    return false;
                }
            }
        }
        return true;

    }
    private void remove_from_domain(State state,int y,int x){ /** if find a value that we can not put it in board
                                                                  we remove it from variable domain **/
        for (int j = 0; j < state.getDomain().get(y).get(x).size();j++){

            if (!Objects.equals(state.getDomain().get(y).get(x).get(j), "")){

                state.setIndexBoard(y,x,state.getDomain().get(y).get(x).get(j));

                if (!isConsistent(state)){
                    state.removeIndexDomain(y,x,state.getDomain().get(y).get(x).get(j));
                    state.size_domain[y][x]--;
                }
                state.setIndexBoard(y,x,"E");
            }

        }

    }
    private int[] mrv(State state){                 /** first find the row and colum that are full than others
                                                        second find a variable in colum or row (that choosen in first)
                                                        that have less domain from other variables in that row or colum **/
        int[] vert = new int[2];
        int count_row = 0, count_colum = 0;
        int min_row = state.getBoard().size(),min_colum = state.getBoard().size();
        int row=0,colum=0;

        for (int i = 0; i < state.getBoard().size(); i++) {
            for (int j = 0; j < state.getBoard().size(); j++) {

                if (state.getBoard().get(i).get(j) == "E") count_row++;
                if (state.getBoard().get(j).get(i) == "E") count_colum++;
            }
            if (count_row < min_row && count_row > 0){
                min_row = count_row;
                row = i;
            }
            count_row = 0;
            if (count_colum < min_colum && count_colum > 0){
                min_colum = count_colum;
                colum = i;
            }
            count_colum = 0;

        }
        int help = 5;
        if (min_row < min_colum){

            for (int i = 0; i < state.getBoard().size();i++){
                if (help > state.size_domain[row][i] && state.getBoard().get(row).get(i) == "E"){
                    colum = i;
                    help = state.getDomain().get(row).get(i).size();
                }
            }
        }else{
            for (int i = 0; i < state.getBoard().size();i++){
                if (help > state.size_domain[i][colum] && state.getBoard().get(i).get(colum) == "E"){
                    row = i;
                    help = state.getDomain().get(i).get(colum).size();
                }
            }
        }
        vert[0] = row;
        vert[1] = colum;
    return vert;
    }
    private boolean lcv(State state , int[] vertex) {    /** check all neighbors of a variable **/

        int[] consistent = new int[2];
        if (state.size_domain[vertex[0]][vertex[1]] == 2){
            if (!Objects.equals(state.getDomain().get(vertex[0]).get(vertex[1]).get(0), "") && !Objects.equals(state.getDomain().get(vertex[0]).get(vertex[1]).get(1), "")) {

                state.setIndexBoard(vertex[0], vertex[1], state.getDomain().get(vertex[0]).get(vertex[1]).get(0));

                consistent[0] = check_Neighbors(state, vertex);
                state.setIndexBoard(vertex[0], vertex[1], "E");
                state.setIndexBoard(vertex[0], vertex[1], state.getDomain().get(vertex[0]).get(vertex[1]).get(1));
                consistent[1] = check_Neighbors(state, vertex);
                state.setIndexBoard(vertex[0], vertex[1], "E");

                if (consistent[0] > consistent[1]) {
                    return true;
                } else {
                    return false;
                }
            }else{
                return false;
            }
    }else{
            return false;
        }

    }

    private int check_Neighbors(State state, int[] vertex){
        /** sum of the consistents that a value generate for neighbors **/

         int consistent = 0;
        for (int j = 0; j < state.getBoard().size();j++){

            consistent += check_consistenty_for_neighbor(vertex[0],j,state);
            consistent += check_consistenty_for_neighbor(j,vertex[1],state);
        }

        return consistent;
    }
    private int check_consistenty_for_neighbor(int y, int x, State state){  /** check consistenty a neighber for lcv **/
        int consistent = 0;
        if (state.getBoard().get(y).get(x) == "E"){
            for (int j = 0; j < state.getDomain().get(y).get(x).size();j++){
                state.setIndexBoard(y,x,state.getDomain().get(y).get(x).get(j));
                if (!isConsistent(state)){
                    consistent++;
                }
                state.setIndexBoard(y,x,"E");
            }
        }

        return consistent;
    }
    private boolean checkNumberOfCircles(State state) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();
        //row
        for (int i = 0; i < n; i++) {
            int numberOfWhites = 0;
            int numberOfBlacks = 0;
            for (int j = 0; j < n; j++) {
                String a = cBoard.get(i).get(j);
                switch (a) {
                    case "w", "W" -> numberOfWhites++;
                    case "b", "B" -> numberOfBlacks++;
                }
            }
            if (numberOfBlacks > n/2 || numberOfWhites > n/2) {
                return false;
            }
        }
        //column
        for (int i = 0; i < n; i++) {
            int numberOfWhites = 0;
            int numberOfBlacks = 0;
            for (int j = 0; j < n; j++) {
                String a = cBoard.get(j).get(i);
                switch (a) {
                    case "w", "W" -> numberOfWhites++;
                    case "b", "B" -> numberOfBlacks++;
                }
            }
            if (numberOfBlacks > n/2 || numberOfWhites > n/2) {
                return false;
            }
        }
        return true;
    }

    private boolean checkAdjacency(State state) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();

        //Horizontal
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n-2; j++) {
                ArrayList<String> row = cBoard.get(i);
                String c1 = row.get(j).toUpperCase();
                String c2 = row.get(j+1).toUpperCase();
                String c3 = row.get(j+2).toUpperCase();
                if (c1.equals(c2) && c2.equals(c3) && !c1.equals("E")) {
                    return false;
                }
            }
        }
        //column
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n-2; i++) {
                String c1 = cBoard.get(i).get(j).toUpperCase();
                String c2 = cBoard.get(i+1).get(j).toUpperCase();
                String c3 = cBoard.get(i+2).get(j).toUpperCase();
                if (c1.equals(c2) && c2.equals(c3) && !c1.equals("E")) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean checkIfUnique (State state) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();

        //check if two rows are duplicated
        for (int i = 0; i < n-1; i++) {
            for (int j = i+1; j < n; j++) {
                int count = 0;
                for (int k = 0; k < n; k++) {
                    String a = cBoard.get(i).get(k);
                    if (a.equals(cBoard.get(j).get(k)) && !a.equals("E")) {
                        count++;
                    }
                }
                if (count == n) {
                    return false;
                }
            }
        }

        //check if two columns are duplicated

        for (int j = 0; j < n-1; j++) {
            for (int k = j+1; k < n; k++) {
                int count = 0;
                for (int i = 0; i < n; i++) {
                    String a = cBoard.get(j).get(i);
                    if (a.equals(cBoard.get(k).get(i)) && !a.equals("E")) {
                        count++;
                    }
                }
                if (count == n) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean allAssigned(State state) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String s = cBoard.get(i).get(j);
                if (s.equals("E"))
                    return false;
            }
        }
        return true;
    }


    private boolean isFinished(State state) {
        return allAssigned(state) && checkAdjacency(state) && checkNumberOfCircles(state) && checkIfUnique(state);
    }

    private boolean isConsistent(State state) {
        return checkNumberOfCircles(state) && checkAdjacency(state) && checkIfUnique(state);
    }

    private void drawLine() {
        for (int i = 0; i < n*2; i++) {
            System.out.print("\u23E4\u23E4");
        }
        System.out.println();
    }
}