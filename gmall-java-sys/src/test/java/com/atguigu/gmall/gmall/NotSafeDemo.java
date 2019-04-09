package com.atguigu.gmall.gmall;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/*请举例说明集合类时不安全的*/
public class NotSafeDemo {
    public static void main(String[] args) {
//        List<String> list= Arrays.asList("a","b","c");
//        list.forEach(System.out::println);
        Map<String,String> list = new ConcurrentHashMap<>();
        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                list.put(Thread.currentThread().getName(),UUID.randomUUID().toString().substring(0, 4));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }

    public void set1() {
//        List<String> list= Arrays.asList("a","b","c");
//        list.forEach(System.out::println);
        Set<String> list = new CopyOnWriteArraySet<>();
        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 4));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }

    public void list1() {
//        List<String> list= Arrays.asList("a","b","c");
//        list.forEach(System.out::println);
        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 4));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }
}
