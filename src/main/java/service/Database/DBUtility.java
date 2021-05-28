package service.Database;

import java.util.HashSet;

public class DBUtility {

    public static HashSet<String> Union(HashSet<String> first, HashSet<String> second){
        HashSet<String> res = (new HashSet<String>(first)); //clone
        res.addAll(second); // Union of sets
        return res;
    }
    public static HashSet<String> intersection(HashSet<String> first, HashSet<String> second){
        HashSet<String> res = (new HashSet<String>(first)); //clone
        res.retainAll(second); // intersection of sets
        return res;
    }

    public static HashSet<String> difference(HashSet<String> first, HashSet<String> second){
        HashSet<String> res = (new HashSet<String>(first)); //clone
        res.removeAll(second); //difference between sets
        return res;
    }
}
