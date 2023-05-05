import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String s = "ACVRTGJIK";
        System.out.println(s.substring(2,6));
  /*    //  File input = new File("inputs/input2.txt");
        ArrayList<ArrayList<String>> board = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<String>>> domain = new ArrayList<>();

      //  try {
            Scanner reader = new Scanner(System.in);
            int n = reader.nextInt();
            for (int i = 0; i < n; i++) {
                board.add(new ArrayList<>());
                domain.add(new ArrayList<>());
                for (int j = 0; j < n; j++) {
                    board.get(i).add("E"); //empty
                    domain.get(i).add(new ArrayList<>(Arrays.asList(
                            "w",
                            "b"
                    )));
                }
            }

            int m = reader.nextInt();
            for (int i = 0; i < m; i++) {
                int y = reader.nextInt();
                int x = reader.nextInt();
                int z = reader.nextInt();
                String a = switch (z) {
                    case 0 -> "W"; //white
                    case 1 -> "B"; //black
                    default -> null;
                };
                board.get(y).set(x, a);
                domain.get(y).set(x, new ArrayList<>(List.of(
                        "n"
                )));


            } //Board and Domain initialized

        int[][] size = new int[board.size()][board.size()];
        for (int i = 0; i < board.size();i++){
            for (int j = 0; j < board.size(); j++){
                size[i][j] = domain.get(i).get(j).size();
            }
        }
        Binairo binairo = new Binairo(board, domain, n,size);
            binairo.start();
         //   reader.close();
     //   } //catch (FileNotFoundException e) {
          //  e.printStackTrace();
       // }
*/
    }

}