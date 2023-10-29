public class HornParserConfig{
    public HornParserConfig() {
        String[][] config = {
            {"*, ", "heading"},
            {"TAB,-, ", "unorderedList"},
            {"[,f,n,:,any,], ", "footNote"},
            {"#,+,b,e,g,i,n,_", "startDirective"},
            {"#,+,e,n,d,_", "endDirective"},
            /*
              For reference, this is the regexp HORN-ts uses 
                heading: /^\*+\s/,
                list: /^\s*-\s/,
                nList: /^\s*([A-Za-z]|[0-9]+)\.\s/,
                sTemplate: /^#\+begin_/,
                sTemplateEnd: /^#\+end_/,
                //bSrc: /^#\+begin_src/,
                //eSrc: /^#\+end_src/,
                nSrc: /^#\+name:/,
                table: /^\|([^\|]*\|)+$/,
                tableSep: /^\|(-+\+)+-+\|$/,
                empty: /^\s*$/,
                HR: /^\s*-{5,}\s*$/,
                orgCode: /^:\s/,
                footNote: /^\[fn:\d+\]\s/,
                paragraph: /.+/,
                clock: /^CLOCK:\s/,
            */
        };
    }
}
