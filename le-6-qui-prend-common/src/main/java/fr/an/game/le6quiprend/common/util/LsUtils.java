package fr.an.game.le6quiprend.common.util;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * boilerplate extension methods for Collection,List,Array,Map,...
 */
public final class LsUtils {

    public static <TDest,TSrc> List<TDest> map(Collection<TSrc> src, Function<TSrc,TDest> func) {
        return src.stream().map(func).collect(Collectors.toList());
    }

    public static <TDest,TSrc> ImmutableList<TDest> mapImmutable(Collection<TSrc> src, Function<TSrc,TDest> func) {
        return ImmutableList.copyOf(map(src, func));
    }

    public static <TDest,TSrc> ImmutableList<TDest> map(ImmutableList<TSrc> src, Function<TSrc,TDest> func) {
        return ImmutableList.copyOf(map(src, func));
    }

}
