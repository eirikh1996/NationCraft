package io.github.eirikh1996.nationcraft.api.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TopicPaginator {
    private final String title;
    private final List<TextComponent> lines = new ArrayList<>();
    public TopicPaginator(String title){
        this.title = title;
    }

    public boolean addLine(TextComponent line){
        boolean result = lines.add(line);
        Collections.sort(lines, Comparator.comparing(TextComponent::content));
        return result;
    }

    public TextComponent[] getPage(int pageNumber, String paginationCommand){
        if(!isInBounds(pageNumber))
            throw new IndexOutOfBoundsException("Page number " + pageNumber + " exceeds bounds <" + 1 + "," + getPageCount() + ">");
        TextComponent[] tempLines = new TextComponent[pageNumber == getPageCount() ? (lines.size()%9) + 1 : 9];
        String leftArrow = "[<<]";
        String rightArrow = "[>>]";
        TextComponent header = Component.text(" ======= ", NamedTextColor.DARK_GRAY)
                .append(Component.text(title, NamedTextColor.DARK_AQUA))
                .append(Component.text(" ======= ", NamedTextColor.DARK_GRAY))
                .append(Component.text("page ", NamedTextColor.DARK_AQUA))
                .append(Component.text(String.valueOf(pageNumber), NamedTextColor.AQUA))
                .append(Component.text("/", NamedTextColor.DARK_AQUA))
                .append(Component.text(String.valueOf(getPageCount()), NamedTextColor.AQUA))
                .append(Component.text(" ====", NamedTextColor.DARK_GRAY));
        if (pageNumber == 1) {
            header = header.append(
                Component
                    .text(leftArrow, NamedTextColor.RED)
                    .hoverEvent(
                        HoverEvent.showText(
                            Component.text("This is the first page", NamedTextColor.RED)
                        )
                    )
                );
        } else {
            header = header.append(
                Component
                    .text(leftArrow, NamedTextColor.GREEN)
                    .hoverEvent(
                        HoverEvent.showText(
                            Component.text("Previous page", NamedTextColor.GREEN)
                        )
                    )
                    .clickEvent(
                        ClickEvent.runCommand(paginationCommand + (pageNumber - 1))
                    )
            );
        }

        if (pageNumber == getPageCount()) {
            header = header.append(
                Component
                    .text(rightArrow, NamedTextColor.RED)
                    .hoverEvent(
                        HoverEvent.showText(
                            Component.text("This is the last page", NamedTextColor.RED)
                        )
                    )
            );
        } else {
            header = header.append(
                    Component
                            .text(leftArrow, NamedTextColor.GREEN)
                            .hoverEvent(
                                    HoverEvent.showText(
                                            Component.text("Previous page", NamedTextColor.GREEN)
                                    )
                            )
                            .clickEvent(
                                    ClickEvent.runCommand(paginationCommand + (pageNumber + 1))
                            )
            );
        }
            tempLines[0] = header;
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
