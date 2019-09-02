package io.github.eirikh1996.nationcraft.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class CollectionUtils {
    public static <E> Collection<E> filter(Collection<E> collection, Collection<E> filter){
        Collection<E> returnColl = new ArrayList<>();
        ArrayList<E> filterList = new ArrayList<>(filter);
        for (E object : collection){
            if (filterList.contains(object)){
                continue;
            }
            returnColl.add(object);
        }
        return returnColl;
    }

    public static <E> ArrayList<E> filter(ArrayList<E> collection, ArrayList<E> filter){
        ArrayList<E> returnColl = new ArrayList<>();
        ArrayList<E> filterList = new ArrayList<>(filter);
        for (E object : collection){
            if (filterList.contains(object)){
                continue;
            }
            returnColl.add(object);
        }
        return returnColl;
    }
}
