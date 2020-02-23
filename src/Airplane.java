package tedu.shoot;

import java.awt.image.BufferedImage;

public class Airplane extends FlyingObject implements Score{
	  //����С�л����ͼƬ������
	   private static BufferedImage[] images;
	   //��̬���м���С�л���ͼƬ
	   static {
		   //��ʼ��ͼƬ����
		   images=new BufferedImage[5];
		   //��һ��ͼ��С�л�
		   images[0]=readImage("airplane0.png"); //�˴���������������
		   //ѭ���������ű�ըͼ
		   for(int i=1;i<images.length;i++) {
			   images[i]=readImage("bom"+i+".png");
		   }
	   }
	   private int step; //�ٶ�
		public Airplane() {
			super(48, 50);
			step=4;
		}
		public void show() {
			super.show();
			System.out.println("�ٶ�:"+step);
		}
		public void step() {
			//С�л������ƶ�
			y+=step;
		}
		int index=1;
		public BufferedImage getImage() {
			BufferedImage img=null;
			//������ŷ��ص�һ��ͼ
			if(isLife()) {
				img=images[0];
			}else if(isDead()) {
				//������ˣ���ñ�ըͼƬ
				img=images[index];
				index++;
				//�����ը��Ͻ����С�л�����Ϊ��ʧ
				if(index==images.length) {
					state=REMOVE;
				}
			}
			return img;
		}
	
		public int getScore() {
			// ����С�л���һ��
			return 1;
		}

}
