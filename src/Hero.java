package tedu.shoot;

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject{
	
	private static BufferedImage[]images;
	static {
		images=new BufferedImage[2];
		images[0]=readImage("hero0.png");
		images[1]=readImage("hero1.png");
	}
	    
	private int life;  //生命
	private int doubleFire; //火力值
	 public Hero() {
			super(96, 139,152,410); //152=400/2-width/2
			life=3;
			doubleFire=10;
		}
		public void show() {
			super.show();
			System.out.println("生命:"+life);
			System.out.println("火力值:"+doubleFire);
		}
		public void step() {
		}
		int index=0;
		public BufferedImage getImage() {
			BufferedImage img=null;
			img=images[index%2]; //任何数%2只可能是0或1
			index++; //index++后下次获得另一张图
			return img;
		}
		//英雄机开火方法
		public Bullet[] shoot() {//有单双炮之分 所以需要数组
			Bullet[] bs=null;
			//为了方便我们使用英雄机宽度四分之一
			//定义一个变量保存四分之一宽度
			int len=this.width/4;
			//英雄机分单排和双排
			if(doubleFire>0) {
				//双
				bs=new Bullet[2];
				bs[0]=new Bullet(this.x+len, this.y-5);
				bs[1]=new Bullet(this.x+3*len, this.y-5);
				doubleFire--;
			}else {
				//单
				bs=new Bullet[1];
				bs[0]=new Bullet(2*len+this.x, this.y-5);
			}
			return bs;
		}
		//英雄机移动方法
		public void moveTo(int x,int y) {
			this.x=x-this.width/2;//英雄机中心x轴到鼠标x轴
			this.y=y-this.height/2;//英雄机中心y轴到鼠标y轴
		}
		//获得英雄机的生命值
		public int getLife() {
			return this.life;
		}
		
		//增加英雄机的生命值
		public void addLife() {
			this.life++;
		}
		
		//增加英雄机火力值
		public void addDoubleLire() {
			this.doubleFire+=20;
		}
		//英雄机减命
		public void subLife() {
			this.life--;
		}
		//清空火力值
		public void clearDoubleFire() {
			this.doubleFire=0;
		}
		
}
