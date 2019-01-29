package io.github.eirikh1996.nationcraft.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.bukkit.util.ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT;

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

    public String[] getPage(int pageNumber){
        if(!isInBounds(pageNumber))
            throw new IndexOutOfBoundsException("Page number " + pageNumber + " exceeds bounds <" + 1 + "," + getPageCount() + ">");
        String[] tempLines = new String[pageNumber == getPageCount() ? (lines.size()%CLOSED_CHAT_PAGE_HEIGHT) + 1 : CLOSED_CHAT_PAGE_HEIGHT];
        tempLines[0] = ChatColor.YELLOW + "" + ChatColor.BOLD + "---" + ChatColor.RESET + " " + title + ChatColor.YELLOW + " " + ChatColor.BOLD + "--" + ChatColor.RESET + " " + ChatColor.DARK_GREEN + "page " + pageNumber + "/" + getPageCount() + ChatColor.YELLOW + " " + ChatColor.BOLD + "---";
        for(int i = 0; i< tempLines.length-1; i++)
            tempLines[i+1] = lines.get(((CLOSED_CHAT_PAGE_HEIGHT-1) * (pageNumber-1)) + i);
        return tempLines;
    }

    public int getPageCount(){
        return (int)Math.ceil(((double)lines.size())/(CLOSED_CHAT_PAGE_HEIGHT-1));
    }

    public boolean isInBounds(int pageNumber){
        return pageNumber > 0 && pageNumber <= getPageCount();
    }

    public boolean isEmpty(){
        return lines.isEmpty();
    }
}
