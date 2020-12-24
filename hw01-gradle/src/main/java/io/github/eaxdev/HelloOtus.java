package io.github.eaxdev;

import com.google.common.collect.Lists;

import java.util.List;

public class HelloOtus {

    public static void main(String[] args) {
        var list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        var chunks = Lists.partition(list, 2);
        System.out.println(chunks);
    }
}
