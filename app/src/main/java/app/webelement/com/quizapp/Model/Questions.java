package app.webelement.com.quizapp.Model;

import java.util.ArrayList;

/**
 * Created by ADMIN on 2/6/16.
 */
public class Questions {
private String id;
    private String name;
    private ArrayList<OptionQ> oprtions;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<OptionQ> getOprtions() {
        return oprtions;
    }

    public void setOptions(ArrayList<OptionQ> options) {
        this.oprtions = options;
    }
}


