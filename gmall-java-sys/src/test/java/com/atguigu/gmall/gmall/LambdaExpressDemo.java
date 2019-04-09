package com.atguigu.gmall.gmall;

@FunctionalInterface
interface Foo{
    public int add(int x,int y);
    public static int mul(int x,int y){
        return x*y;
    }
    public default int div(int x,int y){
        return x/y;
    }
}


public class LambdaExpressDemo {
    public static void main(String[] args) {
        Foo foo=(x,y)->{
            return x+y;
        };
        System.out.println(foo.add(3, 6));
        System.out.println(Foo.mul(2,3));
        System.out.println(foo.div(9,3));
    }
}
