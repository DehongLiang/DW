package tedu.shoot;

import java.awt.image.BufferedImage;

public class Airplane extends FlyingObject implements Score{
	  //保存小敌机相关图片的数组
	   private static BufferedImage[] images;
	   //静态块中加载小敌机的图片
	   static {
		   //初始化图片数组
		   images=new BufferedImage[5];
		   //第一张图是小敌机
		   images[0]=readImage("airplane0.png"); //此处可以用类名调用
		   //循环加载四张爆炸图
		   for(int i=1;i<images.length;i++) {
			   images[i]=readImage("bom"+i+".png");
		   }
	   }
	   private int step; //速度
		public Airplane() {
			super(48, 50);
			step=4;
		}
		public void show() {
			super.show();
			System.out.println("速度:"+step);
		}
		public void step() {
			//小敌机向下移动
			y+=step;
		}
		int index=1;
		public BufferedImage getImage() {
			BufferedImage img=null;
			//如果活着返回第一张图
			if(isLife()) {
				img=images[0];
			}else if(isDead()) {
				//如果死了，获得爆炸图片
				img=images[index];
				index++;
				//如果爆炸完毕将这个小敌机设置为消失
				if(index==images.length) {
					state=REMOVE;
				}
			}
			return img;
		}
	
		public int getScore() {
			// 击中小敌机得一分
			return 1;
		}

}
