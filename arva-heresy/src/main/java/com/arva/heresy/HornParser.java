package com.arva.heresy;

import java.util.List;

class HornResult {
    HornNodes horns;
    int lineNumber;
    public HornResult(HornNodes n, int i) {
        this.horns = n;
        this.lineNumber = i;
    }

    public List<List<Integer>> toBeFormatted() {
        return horns.toBeFormatted;
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
        /* there's a semi-problem with the fact that there will always be
           only ONE HornNode per result ( i.e. per line)
           the design is consistent, it allows us to reuse most of the logic
           from TreeBasedParser and TreeBasedParserState, but the behavior
           of HornParser brings some differences and the choice of a
           pNodes<HornNode> structure is not justified when our
           result wil always be ONE HornNode. That creates a slight
           unnecessary overhead, while allowing for a lot of code to be
           reused.
        */
        horns.finalize(s.length());
        //horns.describe();
        return new HornResult((HornNodes) horns, index);
    }

    static class HornParserState extends TreeBasedParserState {
        public HornParserState(HornParserConfig config, pNodes<? extends pNode> n) {
            super(config, n);
        }

        @Override
        public boolean handleTail(int i, Tail t) {
            //System.out.println(t.getType());
            nodes.end(i, t.getType(), memory.toString());
            nodes.commit();
            return true;
        }

        @Override
        public boolean reset(ParserConfig config) {
            nodes.start(0);
            nodes.end(0, "paragraph", "");
            nodes.commit();
            return true;
        }

    }
}
