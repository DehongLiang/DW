package tedu.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Bee extends FlyingObject implements Award{
	
	private static BufferedImage[] images;
	static {
		images=new BufferedImage[5];
		images[0]=readImage("bee0.png");
		for(int i=1;i<images.length;i++) {
			images[i]=readImage("bom"+i+".png");
		}
	}
	//移动方向
	private int xStep;
	private int yStep;
	private int awardType; //奖励类型
	
	public Bee() {
		super(60, 51);
		xStep=2;
		yStep=2;
		Random ran=new Random();
		awardType=ran.nextInt(2);  //随机生成0-1存入奖励类型  击中敌机时用于奖励
	}
	public void show() {
		super.show();
		System.out.println("x速度"+xStep+",y速度"+yStep);
		System.out.println("奖励:"+awardType);
	}
	public void step() {
		x+=xStep;
		y+=yStep;
		//如果奖励机碰撞了两侧的边界
		if(x<=0 || x>=World.WIDTH-this.width) {
			//修改它的移动方向
			xStep*=-1;
		}
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

	public int awardType() {
		// 返回奖励类型  当前的
		return this.awardType;
	}
}
