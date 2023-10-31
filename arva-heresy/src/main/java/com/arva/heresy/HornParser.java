package com.arva.heresy;

class HornResult {
    HornNodes horns;
    int lineNumber;
    public HornResult(HornNodes n, int i) {
        this.horns = n;
        this.lineNumber = i;
    }
}
public class HornParser extends TreeBasedParser {
    public HornParser() {
        super(new HornParserConfig(), HornParserCommands.generateCommands());
    }


    public HornResult parseHorns(String s, int index) {
        pNodes<HornNode> horns = new HornNodes();
        HornParserState state = new HornParserState((HornParserConfig) this.config, horns);
        treeParse(s, index, state, horns);
        return new HornResult((HornNodes) horns, index);
    }

    static class HornParserState extends TreeBasedParserState {
        public HornParserState(HornParserConfig config, pNodes<? extends pNode> n) {
            super(config, n);
        }

        public void handleTail(int i, Tail t) {
            nodes.end(i, t.getType(), memory.toString());
            nodes.commit();
        }
    }
}
