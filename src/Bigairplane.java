package tedu.shoot;

import java.awt.image.BufferedImage;

public class Bigairplane extends FlyingObject implements Score{
	
	private static BufferedImage[] images;
	static {
		images=new BufferedImage[5];
		images[0]=readImage("bigairplane0.png");
		for(int i=1;i<images.length;i++) {
			images[i]=readImage("bom"+i+".png");
		}
	}
	private int step; //速度
	public Bigairplane() {
		super(66, 89);
		step=2;
	}
	public void show() {
		super.show();
		System.out.println("速度:"+step);
	}
	public void step() {
		y+=step;
	}
	
	int index=1;
	public BufferedImage getImage() {
		BufferedImage img=null;
		if(isLife()) {
			img=images[0];
		}else if(isDead()) {
			img=images[index];
			index++;
			if(index==images.length) {
				state=REMOVE;
			}
		}
		return img;
	}
	
	public int getScore() {
		// 击中大敌机得3分
		return 3;
	}
	
}
