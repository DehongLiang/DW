package tedu.shoot;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.AbstractDocument.BranchElement;

//继承JPanel表示World类变成一个窗口
public class World extends JPanel {
	// 声明两个常量，游戏界面的长和宽
	public static final int WIDTH = 400;
	public static final int HEIGHT = 700;
	//定义游戏的分数
	private int score;
	//定义游戏状态常量
	public static final int START=0; //开始
	public static final int RUNNING=1; //运行
	public static final int PAUSE=2; //暂停
	public static final int GAME_OVER=3; //结束
	//设置当前的游戏状态
	private int state=START;
	//声明三个状态的三个图片
	private static BufferedImage startImg;
	private static BufferedImage pauseImg;
	private static BufferedImage gameoverImg;
	static {
		startImg=FlyingObject.readImage("start.png");
		pauseImg=FlyingObject.readImage("pause.png");
		gameoverImg=FlyingObject.readImage("gameover.png");
	}
	// 声明飞机大战中出现的对象
	// 将组建声明为成员变量
	// 所有方法均能访问
	Hero hero = new Hero();
	Sky sky = new Sky();
	FlyingObject[] enemy = { new Airplane(), new Bigairplane(), new Bee() };
	Bullet[] bull = {};

	public void start() {
		//鼠标监听器
		MouseAdapter l=new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				if(state==RUNNING)
					hero.moveTo(e.getX(), e.getY());
			}
			//鼠标点击时的状态切换
			public void mouseClicked(MouseEvent e) {
				switch (state) {
				case START:
					state=RUNNING; //如果是开始进入运行
					break;
				case GAME_OVER: //如果结束进入开始
					state=START;
					//重置游戏
					score=0;
					hero=new Hero();
					sky=new Sky();
					bull=new Bullet[0];
					enemy=new FlyingObject[0];
					break;
				}
			}
			//鼠标移入
			public void mouseEntered(MouseEvent e) {
				if(state==PAUSE)  //如果是暂停
					state=RUNNING; //进入运行
			}
			//鼠标移出
			public void mouseExited(MouseEvent e) {
				if(state==RUNNING) //如果是运行
					state=PAUSE; //进入暂停
			}
		};
		//绑定鼠标移动和滑动事件
		this.addMouseListener(l);
		this.addMouseMotionListener(l);

		// 定义计时器的时间间隔
		int interval = 30;
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			// 匿名类不类
			public void run() {
				if(state==RUNNING) {
					// 周期运行的内容
					// 调用飞行物移动方向
					moveAction();
					enterAction();// 调用新出现的敌机
					outOfBoundsAction();
					//System.out.println(bull.length);
					bulletHitAction(); //调用击中爆炸方法
					heroHitAction(); //调用英雄机和敌机相撞方法
					gameOvaerAction(); //调用游戏结束方法
					shootAction();  //英雄机开炮方法
				}
				repaint(); //重绘所有对象
			}
		};
		// 启动计时器周期运行
		timer.schedule(task, interval, interval);
	}
	//判断游戏结束的方法
	public void gameOvaerAction() {
		if(hero.getLife()<=0) { //如果生命值小于等于0
			//游戏进入结束状态
			state=GAME_OVER;
		}
	}
	//判断英雄机和敌机碰撞的方法
	public void heroHitAction() {
		//遍历所有敌机
		for(int i=0;i<enemy.length;i++) {
			FlyingObject f=enemy[i];
			//判断是否和当前敌机相撞
			if(f.isLife() && hero.hit(f)) {
				//撞死敌机
				f.goDead();
				//英雄机减命，清火力
				hero.subLife();
				hero.clearDoubleFire();
			}
		}
	}
	//判断子弹和敌机碰撞的方法
	public void bulletHitAction() {
		//遍历所有子弹
		for(int i=0;i<bull.length;i++) {
			Bullet b=bull[i]; //提取子弹
			//遍历所有敌机
			for(int j=0;j<enemy.length;j++) {
				FlyingObject f=enemy[j]; //提取敌机
				//判断击中敌机
				if(b.isLife() && f.isLife() && b.hit(f)) {
					b.goDead(); //子弹死
					f.goDead(); //敌机死
					//判断敌机书否得分的
					if(f instanceof Score) {
						Score s=(Score)f;
						score+=s.getScore();
					}
					//判断敌机是否有奖励
					if(f instanceof Award) {
						Award a=(Award)f;
						//获得这架奖励机的奖励值
						int num=a.awardType();
						switch(num) {
						case Award.LIFE:
							hero.addLife(); //如果是0 加命
							break;
						case Award.DOUBLE_FIRE: //如果是1加火力
							hero.addDoubleLire();
							break;
						}
					}
				}
			}
		}
	}
	//检测出界的方法
	public void outOfBoundsAction() {
		int index=0;//没有出界的元素下标，同时保存没出界元素数量
		FlyingObject[] fs=new FlyingObject[enemy.length];
		//遍历所有敌机，检测是否出界
		for(int i=0;i<enemy.length;i++) {
			//提取当前元素
			FlyingObject f=enemy[i];
			//判断是否没出界并且没移除的,
			if(!f.isOutOfBounds()&&!f.isRemove()) {
				fs[index]=f;
				index++;//下标加一
			}
		}
		//数组缩容
		enemy=Arrays.copyOf(fs, index);
		index=0; //子弹的出界
		Bullet[] bs=new Bullet[bull.length];
		for(int i=0;i<bull.length;i++) {
			Bullet b=bull[i];
			if(!b.isOutOfBounds()&&!b.isRemove()) {
				bs[index]=b;
				index++;
			}
		}
		bull=Arrays.copyOf(bs, index);
	}

	// 子弹进场
	int shootIndex = 1;

	public void shootAction() {
		// 调用英雄机开炮方法
		if (shootIndex % 2 == 0) {
			Bullet[] bs = hero.shoot();
			// 对bull数组进行扩容
			bull = Arrays.copyOf(bull, bull.length + bs.length);
			// 将英雄机发射的新炮弹
			// 放到扩容后数组的最后位置
			System.arraycopy(bs, 0, bull, bull.length - bs.length, bs.length);
		}
		shootIndex++;
	}

	// 敌机进场方法
	int enterIndex = 1;

	public void enterAction() {
		if (enterIndex % 2 == 0) {
			// 生成一架敌机
			FlyingObject fo = nextEnemy();
			enemy = Arrays.copyOf(enemy, enemy.length + 1);// 扩容数组
			enemy[enemy.length - 1] = fo;// 将生成的敌机放入扩容后的数组最后
		}
		enterIndex++;
	}

	// 飞行物移动的方法
	public void moveAction() {
		sky.step();// 天空移动
		// 敌机移动
		for (int i = 0; i < enemy.length; i++) {
			enemy[i].step();
		}
		for (int i = 0; i < bull.length; i++) {
			bull[i].step();
		}
	}

	// 随机产生敌机方法
	public FlyingObject nextEnemy() {
		Random ran = new Random();
		FlyingObject fo = null;
		int num = ran.nextInt(100);
		if (num < 40) {// 40%几率出现小敌机
			fo = new Airplane();
		} else if (num < 80) {// 40%几率出现大敌机
			fo = new Bigairplane();
		} else {// 20%生成奖励机
			fo = new Bee();
		}
		return fo;
	}

	public void paint(Graphics g) { // 重写了JPanel中的方法 方法名必须叫paint
		// 先画背景，再画其他的
		sky.paintObject(g);
		hero.paintObject(g);
		for (int i = 0; i < enemy.length; i++) {
			enemy[i].paintObject(g);
		}
		for (int i = 0; i < bull.length; i++) {
			bull[i].paintObject(g);
		}
		//显示分数和生命值
		g.drawString("SCORE"+score, 10, 10);
		g.drawString("LIFE"+hero.getLife(), 10, 30);
		//根据游戏状态绘制图片
		switch (state) {
		case START:
			g.drawImage(startImg,0,0,null);
			break;
		case PAUSE:
			g.drawImage(pauseImg,0,0,null);
			break;
		case GAME_OVER:
			g.drawImage(gameoverImg,0,0,null);
			break;
		}

	}

	public static void main(String[] args) {
		World w = new World();
		// 实例化一个java窗口
		JFrame f = new JFrame("飞机大战");
		// 将World类设置到窗口中
		f.add(w);
		// 设置窗口的宽和高
		f.setSize(400, 700);
		// 设置窗口关闭时程序结束
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 设置窗口居中
		f.setLocationRelativeTo(null);
		// 显示窗口,自动调用上面的paint方法
		f.setVisible(true);

		w.start();
	}

}
