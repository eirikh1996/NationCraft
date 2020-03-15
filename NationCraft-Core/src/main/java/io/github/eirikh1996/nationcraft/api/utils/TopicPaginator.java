package io.github.eirikh1996.nationcraft.api.utils;

import io.github.eirikh1996.nationcraft.api.objects.text.ChatText;
import io.github.eirikh1996.nationcraft.api.objects.text.ClickEvent;
import io.github.eirikh1996.nationcraft.api.objects.text.HoverEvent;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopicPaginator {
    private final String title;
    private final List<ChatText> lines = new ArrayList<>();
    public TopicPaginator(String title){
        this.title = title;
    }

    public boolean addLine(ChatText line){
        boolean result = lines.add(line);
        Collections.sort(lines);
        return result;
    }

    public ChatText[] getPage(int pageNumber, String paginationCommand){
        if(!isInBounds(pageNumber))
            throw new IndexOutOfBoundsException("Page number " + pageNumber + " exceeds bounds <" + 1 + "," + getPageCount() + ">");
        ChatText[] tempLines = new ChatText[pageNumber == getPageCount() ? (lines.size()%9) + 1 : 9];
        String leftArrow = "[<<]";
        String rightArrow = "[>>]";
        ChatText.Builder builder = ChatText.builder().addText(TextColor.DARK_GRAY, " ======= ")
                .addText(TextColor.DARK_AQUA, title)
                .addText(TextColor.DARK_GRAY, " ======= ")
                .addText(TextColor.DARK_AQUA, "page ")
                .addText(TextColor.AQUA, String.valueOf(pageNumber))
                .addText(TextColor.DARK_AQUA, "/")
                .addText(TextColor.AQUA, String.valueOf(getPageCount()))
                .addText(TextColor.DARK_GRAY, " ====");
        if (pageNumber == 1) {
            builder = builder.addText(TextColor.RED, leftArrow, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "§cThis is the last page"));
        } else {
            builder = builder.addText(TextColor.GREEN, leftArrow, new ClickEvent(ClickEvent.Action.RUN_COMMAND, paginationCommand + (pageNumber - 1)), new HoverEvent(HoverEvent.Action.SHOW_TEXT, "Previous page"));
        }

        if (pageNumber == getPageCount()) {
            builder = builder.addText(TextColor.RED, rightArrow, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "§cThis is the last page"));
        } else {
            builder = builder.addText(TextColor.GREEN, rightArrow, new ClickEvent(ClickEvent.Action.RUN_COMMAND, paginationCommand + (pageNumber + 1)), new HoverEvent(HoverEvent.Action.SHOW_TEXT, "Previous page"));
        }
            tempLines[0] = builder.build();
                //"§8§b" + title + " §8===== page §b" + pageNumber + "§3/§b" + getPageCount() + " §8======";
        for(int i = 0; i< tempLines.length-1; i++)
            tempLines[i+1] = lines.get(((9-1) * (pageNumber-1)) + i);
        return tempLines;
    }

    public int getPageCount(){
        return (int)Math.ceil(((double)lines.size())/(9-1));
    }

    public boolean isInBounds(int pageNumber){
        return pageNumber > 0 && pageNumber <= getPageCount();
    }

    public boolean isEmpty(){
        return lines.isEmpty();
    }
}
