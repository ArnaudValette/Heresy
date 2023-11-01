package com.arva.heresy;

import java.util.ArrayList;
import java.util.List;

class BracketResult extends TreeParserResult<BracketNode> {
    FormatNodes formats;
    public BracketResult(BracketNodes b, int i) {
        super(b, i);
    }

    public void fillFormats(String s, FormatParser f, int offset){
        toBeFormatted().forEach((lim)->{
            String toFormat = s.substring(lim.get(0), lim.get(1));
            List<Integer> newLims = new ArrayList<>();
            newLims.add(lim.get(0) + offset);
            newLims.add(lim.get(1) + offset);
            FormatResult format = f.parse(toFormat, newLims );
            System.out.println(toFormat + " " + offset);
            formats = format.formats;
            });
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
