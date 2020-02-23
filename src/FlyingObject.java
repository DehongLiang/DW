package tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.management.RuntimeErrorException;

//�ɻ���ս�ĸ���
public abstract class FlyingObject {
	//��������״̬����
	public static final int LIFE=0; //����
	public static final int DEAD=1; //����
	public static final int REMOVE=2; //��ʧ
	//���嵱ǰ����״̬����
	protected int state=LIFE;//��ʼĬ�ϻ���
	//�������๲�е����Ժͷ���
	protected int width;
	protected int height;
	protected int x;
	protected int y;
	//����һ����λ�ƶ�����
	public abstract void step();
	
   public FlyingObject(int width,int height) {
	   this.width=width;
	   this.height=height;
	   y=-height;   //�ʼ���ǳ�������Ļ�Ϸ�
	   Random ran=new Random();
	   x=ran.nextInt(400-this.width); //x�����࿪ʼ����Ļ���-�л���ȵ������
		
	}
   //�ӵ�����գ�Ӣ�ۻ��Ĺ���
   public FlyingObject(int width,int height,int x,int y) {
	   this.width=width;
	   this.height=height;
	   this.x=x;
	   this.y=y;  
   }
   public void show() {
	   System.out.println("��:"+width+",��:"+height);
	   System.out.println("x:"+x+",y:"+y);
   }
   //��ȡͼƬ
   //����Ŀ�б����ͼƬ��ת����java�ڴ��е�ͼƬ
   public static BufferedImage readImage(String fileName) {
	   try {
		   //�����ļ���
		   //��ͼƬ��ȡ����ֵ��img
		BufferedImage img=ImageIO.read(FlyingObject.class.getResource(fileName));
	     return img;
	   } catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException();
	}
   }
   //�ж�״̬����
   //�ж��Ƿ����
   public boolean isLife() {
	   return state==LIFE;
   }
   public boolean isDead() {
	   return state==DEAD;
   }
   public boolean isRemove() {
	   return state==REMOVE;
   }
   //���ͼƬ�ĳ��󷽷�
   public abstract BufferedImage getImage();
   
   //����ͼƬ������
   public void paintObject(Graphics g) {
	   g.drawImage(getImage(),x,y,null);
   }
   //�жϳ���ķ���
   public boolean isOutOfBounds() {
	   return y>World.HEIGHT;
   }
   //�ж���ײ�ķ���
   //�жϵ���this(��ǰ����)
   //�Ƿ�Ͳ����еķ�������ײ��
   public boolean hit(FlyingObject f) {
	   //this.����Ϊ�ӵ� f����Ϊһ�ܵл�
	   int x1=f.x-this.width; //����
	   int x2=f.x+f.width; //�Ҳ��
	   
	   int y1=f.y-this.height; //�Ϸ���
	   int y2=f.y+f.height; //�·���
	   
	   return this.x>x1 && this.x<x2 && this.y>y1 && this.y<y2;
   }
   //�ı������״̬ΪDEAD�ķ���
   public void goDead() {
	   state=DEAD;
   }
}
