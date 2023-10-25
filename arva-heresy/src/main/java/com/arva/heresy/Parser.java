package com.arva.heresy;

import java.util.Iterator;
import java.util.Set;

public class Parser {
    public Parser() {
    }
}

class BracketParser extends Parser {

    ParserConfig config;
    CommandMap commands;
    String prevKey;
    Node prev;
    Node curr;  
    String memory;
    BracketNodes brackets;
    boolean isActive;
    boolean hasMatched;
    boolean isRedundant;

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
            Set<String> keys= curr.keySet();
            Iterator<String> iterator = keys.iterator();

            while(iterator.hasNext()){
                String key = iterator.next();
                handleMatch(key, c);
                if(hasMatched){
                    if(!isActive){
                        brackets.start(i);
                    }
                    if(curr.get(key) instanceof Node){
                        handleNode(curr, key, c);
                        break;
                    }
                    if(curr.get(key) instanceof Tail){
                        handleTail((Tail) curr.get(key), c, i);
                        break;
                    }
                }
            }

            if(!hasMatched){
                if(isRedundant){
                    handleMatch(prevKey, c);
                    if(hasMatched){
                        handleNode(prev, prevKey, c);
                    }
                    else{
                        noMatch();
                    }
                }
                else{
                    noMatch();
                }
            }
        }
    }

    public void handleNode(Node newPrev, String key, char c){
        prev = isRedundant ? newPrev : null;
        curr= (Node) curr.get(key);
        prevKey = isRedundant ? key : null;
        memoryAppend(c);
        isActive = true;
        hasMatched = true;
    }

    public void handleMatch(String key, char c){
        Command comm = commands.get(key);
        hasMatched = comm.f.apply(c);
        isRedundant = comm.isRedundant;
    }

    public void handlePrevMatch(String key, char c){
        Command comm = commands.get(key);
        hasMatched= comm.f.apply(c);
        isRedundant= comm.isRedundant;
        prev=isRedundant ? prev : null; 
        prevKey = isRedundant ? key : null;
    }

    public void handleTail(Tail t, char c, int i){
        memoryAppend(c);
        brackets.end(i, t.type, memory);
        brackets.commit();
        noMatch();
    }

    public void noMatch(){
        curr=config;
        memoryInit();
        isActive=false;
        hasMatched=false;
        isRedundant= false;
        prev= null;
    }
}

