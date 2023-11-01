package com.arva.heresy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class HornResult extends TreeParserResult<HornNode>{
    List<FormatNode> formats = new ArrayList<>();
    List<BracketNode> brackets = new ArrayList<>();
    public HornResult(HornNodes n, int i) {
        super(n, i);
    }

    public HornNode getNode() {
        if (nodes.size() <= 0) {
            return null;
        }
        return nodes.get(0);
    }
    public void fillBracketsAndFormats(BracketParser b, FormatParser f, String s, int ln){
        // There is always only one HornNode.
        HornNode node = getNode();
        if (node == null) {
            return;
        }
        List<List<Integer>> lims = toBeFormatted();
        // ok
        List<Integer> lim = lims.get(0);
        String toBracket = s.substring(lim.get(0), lim.get(1));
        BracketResult bRes = b.parseBrackets(toBracket, ln);
        bRes.fillFormats(toBracket, f, lim.get(0));

        List<? extends ComparableNode> x = bRes.nodes.nodes;
        List<? extends ComparableNode> y = bRes.getFormats();
        // y.forEach(a -> System.out.println(a.start + " " + a.content));
        List<? extends ComparableNode> mergedList = Stream.concat(x.stream(), y.stream())
                            .sorted((n1,n2)-> Integer.compare(n1.start, n2.start))
                            .collect(Collectors.toList());
        node.lesserElements = mergedList;
    }

    public void describe() {
        nodes.nodes.forEach((n)->{
            n.describe();
            n.lesserElements.forEach(l->l.describe());
                    });
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
            // nodes.start(0);
            // nodes.end(0, "paragraph", "");
            // nodes.commit();
            return true;
        }

        public void stop(int len) {
            nodes.noNodes(len);
        }

    }
}
