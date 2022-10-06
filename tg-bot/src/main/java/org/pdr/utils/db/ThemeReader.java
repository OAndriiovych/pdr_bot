package org.pdr.utils.db;

import org.pdr.entity.Question;
import org.pdr.utils.json.JSONToQuestion;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ThemeReader {

   public HashMap<Integer,List<Question>> questionList = new HashMap<>();

    public static void main(String[] args) {
       ThemeReader themeReader =  new ThemeReader();
       themeReader.readJson();                  // Просто для перевірки
        System.out.println(themeReader.questionList.size());
    }

    private void readJson(){
        for (File f : Objects.requireNonNull(new File("tg-bot/src/main/resources/questions").listFiles())) {
            String name = f.getName();
            int theme = Integer.parseInt(name.substring(0, name.indexOf(".json")));
            List<Question> questions = new JSONToQuestion("tg-bot/src/main/resources/questions" + "/" + name).getQuestions();
            questionList.put(theme, questions);
        }
    }
}
