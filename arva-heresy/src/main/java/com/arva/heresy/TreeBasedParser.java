package com.arva.heresy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@FunctionalInterface
interface callbackFunction {
    void apply(int i);
}

public class TreeBasedParser {
    ParserConfig config;
    CommandMap commands;

    public TreeBasedParser(ParserConfig conf, CommandMap map) {
        this.config = conf;
        this.commands = map;
    }

    public void parse(String s, int lineNumber, TreeBasedParserState state, pNodes<? extends pNode> nodes) {
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
                nodes.finalize(j);
            }
        }

        //return new BracketResult(brackets, lineNumber);
    }

    public boolean itDoesMatch(String key, char c) {
        if (commands.get(key) != null) {
            if (commands.get(key).f.apply(c)) {
                return true;
            }
        }
        return false;
    }

}
class TreeBasedParserState{
        String previousCommandKey;
        ParserConfigNode availableCommands;  
        StringBuilder memory;
        pNodes<? extends pNode> nodes;
        boolean hasMatched;
        boolean isRedundant;
        boolean isAwake;

        public TreeBasedParserState(ParserConfig config, pNodes<? extends pNode> b) {
            previousCommandKey = null;
            availableCommands = (Node) config.toNode();
            memory = new StringBuilder();
            nodes = b;
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
            nodes.end(i, t.getType(), memory.toString());
            nodes.commit();
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
                nodes.start(i);
            }
        }

    }