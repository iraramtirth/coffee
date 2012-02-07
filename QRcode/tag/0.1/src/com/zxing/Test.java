package com.zxing;

public class Test {
	public static void main(String[] args) {
        ImgUtil.write2D("test.png", "I'm a PhD candidate working at the National Laboratory of Pattern Recognition, under the supervision of Professor Tieniu Tan and Associate Professor Kaiqi Huang.");
        System.out.println(ImgUtil.read("test.png"));
          
        //ImgUtil.write1D("test1.png", "122222");  //
        //System.out.println(ImgUtil.read("test1.png"));  
	}
}
