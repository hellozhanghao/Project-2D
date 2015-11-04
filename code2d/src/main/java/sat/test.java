package sat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sat.formula.Clause;
import sat.formula.Literal;

/**
 * Created by liusu on 4/11/15.
 */
public class test {
    public static void main(String[] args){
        try{
            File file = new File("/Users/liusu/Desktop/s8Sat.cnf");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int count = 1;
            List<Clause> clauses = new ArrayList<>();
            while((tempString = reader.readLine()) != null){
                if(count > 2){
                    List<String> oneClause = new ArrayList<>();
                    if(tempString.length() > 0){
                        String temp1[] = tempString.split(" ");
                        for(int i = 0 ; i < temp1.length ; i++){
                            if(temp1[i] != "0"){
                                oneClause.add(temp1[i]);
                            }else{
                                Literal[] literals = new Literal[oneClause.size()];
                                oneClause.toArray(literals);
//                                clauses.add();
                            }
                        }
                        count++;
                        System.out.println(temp1.length);
                    }
                }else{
                    count++;
                }
            }
            System.out.println(count);
        }catch(IOException e){
            System.out.println("Error");
            e.printStackTrace();
        }

    }
}
