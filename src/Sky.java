package tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sky extends FlyingObject {
	  
	private static BufferedImage image;
	  static {
		  image=readImage("background0.png");
	  }
	private int y1;
	private int step; //速度
		public Sky() {
			super(400, 700, 0, 0);
			y1=-700;
			step=1;
		}
		public void show() {
			super.show();
			System.out.println("y1"+y1);
			System.out.println("速度:"+step);
		}
		public void step() {
			//两张图都要移动
			y+=step;
			y1+=step;
			//当任何一张图移动出窗体时都要重置
			if(y>=World.HEIGHT) {
				y=-World.HEIGHT;
			}
			if(y1>=World.HEIGHT) {
				y1=-World.HEIGHT;
			}
		}
		
		public BufferedImage getImage() {
			return image;
		}
		//重写父类绘制图片到窗体的方法
		public void paintObject(Graphics g) {
			g.drawImage(getImage(),x,y,null);
			g.drawImage(getImage(),x,y1,null);
			
		}

}
