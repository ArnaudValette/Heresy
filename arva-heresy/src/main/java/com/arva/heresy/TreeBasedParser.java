package com.arva.heresy;
import java.util.Iterator;
import java.util.List;

import com.arva.heresy.HornParser.HornParserState;

class TreeParserResult<T extends pNode> {
    pNodes<T> nodes;
    int lineNumber;
    public TreeParserResult(pNodes<T> n, int i) {
        this.nodes = n;
        this.lineNumber = i;
    }

    public List<List<Integer>> toBeFormatted() {
        return nodes.toBeFormatted;
    }
}
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

    public void treeParse(String s, int lineNumber, TreeBasedParserState state, pNodes<? extends pNode> nodes) {
        /*
          the current treeParse implementation handles BracketParsing well
          the idea is to have
          A: A hashMap <Key, Function> and
          B: a recursive hashMap <Key, HashMap<Key, ...<..., Tail>>>
          C: Tails, Strings that serve to type and name the path taken through
          the branches of B.
        
          For each character X, we use each key K from the current depth in B,
          to call every function in A -> boolean A[K](X)
        
          if this is true, then we have a match and we must travel deeper in the forest
          B, accessing B[K].
        
          if this is false, then we set B back to the initial state, and continue this
          operation.
        
          Eventually, we will reach one end of the forest and from this we should
          perform operations to handle our newly found expression.
          e.g. the path to a footNote is '['->'f'->'n'->':'->'any'->']'.
          Note that some keys of B triggers some /redundant/ functions such as 'any',
          that allows for repetition ( any can lead, theorically, to n number of any).
        
          That's why state.handleRedundant(char, int, Commands) is run
          before anything in our loop. The state keeps track of isRedundant and
          previousCommandKey variables that allows us to perform such a thing easily.

          previousCommandKey is set only if the previousCommand is redundant.

          We need to rework this so it can handle both

          1. the parsing for multiple expressions in a string that matches a
          set of different expressions (that's what it does right now)

          2. the parsing of expressions only at the start of the string,
          making the line a HornNode according to the specification given
          by HornNodeConfig. This is different because in this behavior,
          any unmatched character should stop the loop and create a 
          HornNode of paragraph type. Also, at any time a Tail is reached,
          we should end parsing and commit our new HornNode of type specified
          by the Tail object reached.

         */
        state.init(config);
        for (int i = 0, j = s.length(); i < j; i = i + 1) {
            boolean stop = false;
            char c = s.charAt(i);
            state.handleRedundant(c, i, commands);
            if (!state.hasMatched) {
                List<String> currentCommandKeys = state.getCurrentCommandKeys();
                Iterator<String> iterator = currentCommandKeys.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (itDoesMatch(key, c)) {
                        state.handleMatch(key, c, i, commands);
                        if (state.availableCommands instanceof Tail) {
                            Tail t = (Tail) state.availableCommands;
                            // some subclasses may want to stop parsing the line at this point;
                                boolean shouldStop = state.handleTail(i, t);
                                if (shouldStop) {
                                    stop = true;
                                    break;
                                }
                                state.reset(config);
                        }
                        // no need to continue cycling through keys (we found a match);
                        break;
                    }
                }
                // some subclasses may want to stop parsing the line at this point;
                // consequence of the fact we found a tailNode in the line
                if (stop) {
                    break;
                }
            }
            if (!state.hasMatched) {
                // This is where HornNodeParser publishes a paragraph
                boolean shouldStop = state.reset(config);
                if (shouldStop) {
                    ((HornParserState) state).stop(s.length());
                    break;
                }
            }
            if (i == j - 1) {
                nodes.finalize(j);
            }
        }
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

        // duplication of init/reset
        // because on some Parsers, it could be different behavior
        public void init(ParserConfig config){
            availableCommands = (Node) config.toNode();
            memoryInit();
            hasMatched=false;
            isRedundant= false;
            isAwake = false;
            previousCommandKey= null;
        }

        public boolean reset(ParserConfig config){
            availableCommands = (Node) config.toNode();
            memoryInit();
            hasMatched=false;
            isRedundant= false;
            isAwake = false;
            previousCommandKey= null;
            return false;
        }

        public List<String> getCurrentCommandKeys() {
            List<String> keys = availableCommands.keySet();
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

        public boolean handleTail(int i, Tail t) {
            nodes.end(i, t.getType(), memory.toString());
            nodes.commit();
            return false;
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
