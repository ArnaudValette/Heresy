package com.arva.heresy;

class BracketResult extends TreeParserResult<BracketNode> {
    public BracketResult(BracketNodes b, int i) {
        super(b, i);
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
