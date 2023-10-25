package com.arva.heresy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Parser {
    public Parser() {
    }
}

class BracketParser extends Parser {

    ParserConfig config;
    CommandMap commands;
    String previousCommandKey;
    ParserConfigNode availableCommands;  
    String memory;
    BracketNodes brackets;
    boolean hasMatched;
    boolean isRedundant;
    boolean isAwake;

    public BracketParser(ParserConfig conf) {
        config = conf;
        commands = generateCommands();
    }

    public CommandMap generateCommands() {
        CommandMap map = new CommandMap();
        map.put("digit", c -> Character.isDigit(c), true);
        map.put("Capital", c -> Character.isUpperCase(c) , false);
        map.put("low", c -> Character.isLowerCase(c) , false);
        map.put("any", c -> c != ' ' && c != '[' && c != ']', true);
        map.put("ANY", c -> c != '[' && c != ']', true);
        map.subscribeChar("%/- X[]fn:");
        return map;
    }

    public void memoryInit(){
        memory = "";
    }

    public void memoryAppend(char c){
        memory = memory + Character.toString(c);
    }

    public void parse(String s) {
        brackets = new BracketNodes();
        noMatch();
        for (int i = 0, j = s.length(); i < j; i = i + 1) {
            char c = s.charAt(i);
            handleRedundant(c,i);
            if (!hasMatched) {
                List<String> currentCommandKeys = getCurrentCommandKeys();
                Iterator<String> iterator = currentCommandKeys.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (itDoesMatch(key, c)) {
                        handleMatch(key, c, i);
                        if (availableCommands instanceof Tail) {
                            Tail t = (Tail) availableCommands;
                            handleTail(i, t);
                            noMatch();
                        }
                    }
                }
            }
            if (!hasMatched) {
                noMatch();
            }
        }
        brackets.describe();
    }

    public void handleTail(int i, Tail t) {
        brackets.end(i, t.getType(), memory);
        brackets.commit();
    }

    public void handleAwakening(int i) {
        if (!isAwake) {
            isAwake = true;
            brackets.start(i);
        }
    }
    public void handleRedundant(char c,int i) {
        if (isRedundant && previousCommandKey != null) {
            hasMatched = commands.get(previousCommandKey).f.apply(c);
            if (hasMatched) {
                handleAwakening(i);
                memoryAppend(c);
            }
        }
        else {
            hasMatched = false;
        }
    }

    public void startBracket(int i) {
        isAwake = true;
    }
    public boolean itDoesMatch(String key, char c) {
        if (commands.get(key) != null) {
            if (commands.get(key).f.apply(c)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getCurrentCommandKeys() {
        Set<String> set = availableCommands.keySet();
        List<String> keys = new ArrayList<>(set);
        return keys;
    }

    public void handleMatch(String key, char c, int i) {
        isRedundant = commands.get(key).isRedundant;
        previousCommandKey = isRedundant ? key : null;
        hasMatched = true;
        memoryAppend(c);
        availableCommands = availableCommands.get(key);
        handleAwakening(i);
    }

    public void noMatch(){
        availableCommands = (Node) config.toNode();
        memoryInit();
        hasMatched=false;
        isRedundant= false;
        isAwake = false;
        previousCommandKey= null;
    }
}

