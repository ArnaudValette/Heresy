package com.arva.heresy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Parser {
    /* We should operate from there to spawn Threads
       Each step should give us a new kind of data for the next one
       HornParser should give the substring that is located after e.g. ** , - , and etc
       this substring is then the base of work for BracketParser.
       BracketParser gives us textDelimitations from which we
       want to reconstruct substring of the base work,
       in order for us to Format them.

       lines = File.readByLines();
       forEach line -> {
         * SomeThread
            line = lines[someIndex]
            const [type, text] = HornNode.parse(line);
            const [textDelimitations, brackets] = BracketParser.parse(line);
            const [textFragments] = reconstruct(line, textDelimitations)

            forEach textFragment -> {
              ** ChildrenThread
                textFragment = textFragments(subIndex);
                const [properFormattedTextNode] = FormatParser.parse(textFragment, line);
            }
       }
    */
    public Parser() {
    }
}

class BracketParser extends Parser {
    ParserConfig config;
    CommandMap commands;

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

    public BracketResult parse(String s, int lineNumber) {
        BracketNodes brackets = new BracketNodes();
        ParserState state = new ParserState(config, brackets);
        state.reset(config);
        for (int i = 0, j = s.length(); i < j; i = i + 1) {
            char c = s.charAt(i);
            state.handleRedundant(c,i,commands);
            if (!state.hasMatched) {
                List<String> currentCommandKeys = state.getCurrentCommandKeys();
                Iterator<String> iterator = currentCommandKeys.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (itDoesMatch(key, c)) {
                        state.handleMatch(key, c, i, commands);
                        if (state.availableCommands instanceof Tail) {
                            Tail t = (Tail) state.availableCommands;
                            state.handleTail(i, t);
                            state.reset(config);
                        }
                    }
                }
            }
            if (!state.hasMatched) {
                state.reset(config);
            }
            if (i == j - 1) {
                brackets.finalize(j);
            }
        }

        return new BracketResult(brackets, lineNumber);
    }

    public boolean itDoesMatch(String key, char c) {
        if (commands.get(key) != null) {
            if (commands.get(key).f.apply(c)) {
                return true;
            }
        }
        return false;
    }
    private static class ParserState{
        String previousCommandKey;
        ParserConfigNode availableCommands;  
        StringBuilder memory;
        BracketNodes brackets;
        boolean hasMatched;
        boolean isRedundant;
        boolean isAwake;

        public ParserState(ParserConfig config, BracketNodes b) {
            previousCommandKey = null;
            availableCommands = (Node) config.toNode();
            memory = new StringBuilder();
            brackets = b;
        }

        public void reset(ParserConfig config){
            availableCommands = (Node) config.toNode();
            memoryInit();
            hasMatched=false;
            isRedundant= false;
            isAwake = false;
            previousCommandKey= null;
        }

        public List<String> getCurrentCommandKeys() {
            Set<String> set = availableCommands.keySet();
            List<String> keys = new ArrayList<>(set);
            return keys;
        }

        public void handleMatch(String key, char c, int i, CommandMap commands) {
            isRedundant = commands.get(key).isRedundant;
            previousCommandKey = isRedundant ? key : null;
            hasMatched = true;
            memoryAppend(c);
            availableCommands = availableCommands.get(key);
            handleAwakening(i);
        }

        public void handleRedundant(char c,int i, CommandMap commands) {
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
        public void handleTail(int i, Tail t) {
            brackets.end(i, t.getType(), memory.toString());
            brackets.commit();
        }
        public void memoryInit(){
            memory = new StringBuilder();
        }

        public void memoryAppend(char c){
            memory.append(c);
        }
        public void handleAwakening(int i) {
            if (!isAwake) {
                isAwake = true;
                brackets.start(i);
            }
        }
    }

}
