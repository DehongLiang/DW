package tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sky extends FlyingObject {
	  
	private static BufferedImage image;
	  static {
		  image=readImage("background0.png");
	  }
	private int y1;
	private int step; //�ٶ�
		public Sky() {
			super(400, 700, 0, 0);
			y1=-700;
			step=1;
		}
		public void show() {
			super.show();
			System.out.println("y1"+y1);
			System.out.println("�ٶ�:"+step);
		}
		public void step() {
			//����ͼ��Ҫ�ƶ�
			y+=step;
			y1+=step;
			//���κ�һ��ͼ�ƶ�������ʱ��Ҫ����
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
		//��д�������ͼƬ������ķ���
		public void paintObject(Graphics g) {
			g.drawImage(getImage(),x,y,null);
			g.drawImage(getImage(),x,y1,null);
			
		}

}
