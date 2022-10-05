package org.pdr.utils.db;

import org.pdr.entity.Question;
import org.pdr.utils.json.JSONToQuestion;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class ThemeReader {

   public HashMap<Integer,List<Question>> questionList = new HashMap<>();

    public static void main(String[] args) {
        new ThemeReader().readJson();
        System.out.println();
    }

    public ThemeReader(){
        readJson();
    }

    private void readJson(){
        for (File f : new File("C:\\Users\\Володимир\\pdr_bot\\tg-bot\\src\\main\\resources\\questions").listFiles()) {
            String name = f.getName();
            int theme = Integer.parseInt(name.substring(0, name.indexOf(".json")));
            List<Question> questions = new JSONToQuestion("C:\\Users\\Володимир\\pdr_bot\\tg-bot\\src\\main\\resources\\questions\\" + name).getQuestions();
            questionList.put(theme, questions);
        }



    }
}
