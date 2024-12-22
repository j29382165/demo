package com.example.demo.constant;

public class MyTest {
    public static void main(String[] args) {
        //以下工作時使用頻率高
        ProductCategory category = ProductCategory.FOOD;
        String s = category.name(); //Enum的name方法，把值轉成字串
        System.out.println(s); // 印FOOD

        String s2 = "CAR";
        ProductCategory category2=ProductCategory.valueOf(s2);
        //如果ProductCategory裡有CAR, CAR才會被存到category2
    }
}
