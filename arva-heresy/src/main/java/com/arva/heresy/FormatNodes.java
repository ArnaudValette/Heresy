import java.util.ArrayList;
import java.util.List;

class FormatNode {
    int start;
    int end;
    int type;
    String content;

    public FormatNode(int s, int e, int t, String c) {
        start = s;
        end = e;
        type = t;
        content = c;
    }

}

public class FormatNodes {
    final List<FormatNode> formats = new ArrayList<>();

    public void push(int start, int end, int type, String content) {
        formats.add(new FormatNode(start, end, type, content));
    }
}
