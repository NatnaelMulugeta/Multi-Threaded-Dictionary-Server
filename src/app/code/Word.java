package app.code;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONAware;

import java.io.Serializable;
import java.util.LinkedList;

public class Word implements JSONAware, Serializable {
    private String word;
    private String first_used;
    private String origin;
    private LinkedList<String> meanings;

    public Word(){}
    public Word(String word, String first_used, String origin, LinkedList<String> meanings) {
        this.word = word;
        this.first_used = first_used;
        this.origin = origin;
        this.meanings = meanings;
    }

    @Override
    public String toJSONString() {
        ObjectMapper objectMapper=new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getfirst_used() {
        return first_used;
    }

    public void setfirst_used(String first_used) {
        this.first_used = first_used;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public LinkedList<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(LinkedList<String> meanings) {
        this.meanings = meanings;
    }

}
