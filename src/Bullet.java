package tedu.shoot;

import java.awt.image.BufferedImage;

public class Bullet extends FlyingObject{
	
	private static BufferedImage image;
	static {
		image=readImage("bullet.png");
	}

	private int step;
	//�ӵ�����Ӣ�ۻ�����λ�þ���������ֻ���ò���
	public Bullet(int x,int y) {
		super(8, 20,x,y);
		step=6;
	}
	public void show() {
		super.show();
		System.out.println("�ٶ�:"+step);
	}
	public void step() {
		//�ӵ�������
		y-=step;
	}
	public BufferedImage getImage() {
		BufferedImage img=null;
		if(isLife()) {
			img=image;
		}else if(isDead()) {
			state=REMOVE;  //�ӵ����ᱬը����ֱ����ʧ
		}
		return img;
	}
	//�ӵ����ϳ���,��д���෽��
	public boolean isOutOfBounds() {
		
		return y<-this.height;
	}

}
