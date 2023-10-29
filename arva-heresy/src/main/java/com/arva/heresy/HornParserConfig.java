public class HornParserConfig{
    public HornParserConfig() {
        String[][] config = {
            {"*, ", "heading"},
            {"TAB,-, ", "unorderedList"},
            {"[,f,n,:,any,], ", "footNote"},
            {"#,+,b,e,g,i,n,_", "startDirective"},
            {"#,+,e,n,d,_", "endDirective"},
        }
    }
}
