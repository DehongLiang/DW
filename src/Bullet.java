package tedu.shoot;

import java.awt.image.BufferedImage;

public class Bullet extends FlyingObject{
	
	private static BufferedImage image;
	static {
		image=readImage("bullet.png");
	}

	private int step;
	//子弹根据英雄机出现位置决定，所以只能用参数
	public Bullet(int x,int y) {
		super(8, 20,x,y);
		step=6;
	}
	public void show() {
		super.show();
		System.out.println("速度:"+step);
	}
	public void step() {
		//子弹向上走
		y-=step;
	}
	public BufferedImage getImage() {
		BufferedImage img=null;
		if(isLife()) {
			img=image;
		}else if(isDead()) {
			state=REMOVE;  //子弹不会爆炸死了直接消失
		}
		return img;
	}
	//子弹向上出界,重写父类方法
	public boolean isOutOfBounds() {
		
		return y<-this.height;
	}

}
