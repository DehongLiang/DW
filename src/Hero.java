package tedu.shoot;

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject{
	
	private static BufferedImage[]images;
	static {
		images=new BufferedImage[2];
		images[0]=readImage("hero0.png");
		images[1]=readImage("hero1.png");
	}
	    
	private int life;  //����
	private int doubleFire; //����ֵ
	 public Hero() {
			super(96, 139,152,410); //152=400/2-width/2
			life=3;
			doubleFire=10;
		}
		public void show() {
			super.show();
			System.out.println("����:"+life);
			System.out.println("����ֵ:"+doubleFire);
		}
		public void step() {
		}
		int index=0;
		public BufferedImage getImage() {
			BufferedImage img=null;
			img=images[index%2]; //�κ���%2ֻ������0��1
			index++; //index++���´λ����һ��ͼ
			return img;
		}
		//Ӣ�ۻ����𷽷�
		public Bullet[] shoot() {//�е�˫��֮�� ������Ҫ����
			Bullet[] bs=null;
			//Ϊ�˷�������ʹ��Ӣ�ۻ�����ķ�֮һ
			//����һ�����������ķ�֮һ���
			int len=this.width/4;
			//Ӣ�ۻ��ֵ��ź�˫��
			if(doubleFire>0) {
				//˫
				bs=new Bullet[2];
				bs[0]=new Bullet(this.x+len, this.y-5);
				bs[1]=new Bullet(this.x+3*len, this.y-5);
				doubleFire--;
			}else {
				//��
				bs=new Bullet[1];
				bs[0]=new Bullet(2*len+this.x, this.y-5);
			}
			return bs;
		}
		//Ӣ�ۻ��ƶ�����
		public void moveTo(int x,int y) {
			this.x=x-this.width/2;//Ӣ�ۻ�����x�ᵽ���x��
			this.y=y-this.height/2;//Ӣ�ۻ�����y�ᵽ���y��
		}
		//���Ӣ�ۻ�������ֵ
		public int getLife() {
			return this.life;
		}
		
		//����Ӣ�ۻ�������ֵ
		public void addLife() {
			this.life++;
		}
		
		//����Ӣ�ۻ�����ֵ
		public void addDoubleLire() {
			this.doubleFire+=20;
		}
		//Ӣ�ۻ�����
		public void subLife() {
			this.life--;
		}
		//��ջ���ֵ
		public void clearDoubleFire() {
			this.doubleFire=0;
		}
		
}
