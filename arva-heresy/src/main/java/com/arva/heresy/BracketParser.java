package com.arva.heresy;

import java.util.List;

class BracketResult {
    BracketNodes brackets;
    int lineNumber;

    public BracketResult(BracketNodes b, int i) {
        this.brackets = b;
        this.lineNumber = i;
    }

    public List<List<Integer>> toBeFormatted() {
        return brackets.toBeFormatted;
    }

}

public class BracketParser extends TreeBasedParser {
    public BracketParser() {
        super(new BracketParserConfig(), BracketParserCommands.generateCommands());
    }

    public BracketResult parseBrackets(String s, int lineNumber) {
        pNodes<BracketNode> brackets =   new BracketNodes();
        BracketParserState state =  new BracketParserState((BracketParserConfig) this.config, brackets);
        treeParse(s, lineNumber, state, brackets);
        return new BracketResult((BracketNodes) brackets, lineNumber);
    }


    static class BracketParserState extends TreeBasedParserState {

        public BracketParserState(BracketParserConfig config, pNodes<? extends pNode> b) {
            super(config, b);
        }

        @Override
        public boolean handleTail(int i, Tail t) {
            nodes.end(i, t.getType(), memory.toString());
            nodes.commit();
            return false;
        }
    }

}
