package io.github.eirikh1996.nationcraft.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopicPaginator {
    String title;
    List<String> lines = new ArrayList<>();
    public TopicPaginator(String title){
        this.title = title;
    }

    public boolean addLine(String line){
        boolean result = lines.add(line);
        Collections.sort(lines);
        return result;
    }
}
