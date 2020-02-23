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

//�̳�JPanel��ʾWorld����һ������
public class World extends JPanel {
	// ����������������Ϸ����ĳ��Ϳ�
	public static final int WIDTH = 400;
	public static final int HEIGHT = 700;
	//������Ϸ�ķ���
	private int score;
	//������Ϸ״̬����
	public static final int START=0; //��ʼ
	public static final int RUNNING=1; //����
	public static final int PAUSE=2; //��ͣ
	public static final int GAME_OVER=3; //����
	//���õ�ǰ����Ϸ״̬
	private int state=START;
	//��������״̬������ͼƬ
	private static BufferedImage startImg;
	private static BufferedImage pauseImg;
	private static BufferedImage gameoverImg;
	static {
		startImg=FlyingObject.readImage("start.png");
		pauseImg=FlyingObject.readImage("pause.png");
		gameoverImg=FlyingObject.readImage("gameover.png");
	}
	// �����ɻ���ս�г��ֵĶ���
	// ���齨����Ϊ��Ա����
	// ���з������ܷ���
	Hero hero = new Hero();
	Sky sky = new Sky();
	FlyingObject[] enemy = { new Airplane(), new Bigairplane(), new Bee() };
	Bullet[] bull = {};

	public void start() {
		//��������
		MouseAdapter l=new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				if(state==RUNNING)
					hero.moveTo(e.getX(), e.getY());
			}
			//�����ʱ��״̬�л�
			public void mouseClicked(MouseEvent e) {
				switch (state) {
				case START:
					state=RUNNING; //����ǿ�ʼ��������
					break;
				case GAME_OVER: //����������뿪ʼ
					state=START;
					//������Ϸ
					score=0;
					hero=new Hero();
					sky=new Sky();
					bull=new Bullet[0];
					enemy=new FlyingObject[0];
					break;
				}
			}
			//�������
			public void mouseEntered(MouseEvent e) {
				if(state==PAUSE)  //�������ͣ
					state=RUNNING; //��������
			}
			//����Ƴ�
			public void mouseExited(MouseEvent e) {
				if(state==RUNNING) //���������
					state=PAUSE; //������ͣ
			}
		};
		//������ƶ��ͻ����¼�
		this.addMouseListener(l);
		this.addMouseMotionListener(l);

		// �����ʱ����ʱ����
		int interval = 30;
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			// �����಻��
			public void run() {
				if(state==RUNNING) {
					// �������е�����
					// ���÷������ƶ�����
					moveAction();
					enterAction();// �����³��ֵĵл�
					outOfBoundsAction();
					//System.out.println(bull.length);
					bulletHitAction(); //���û��б�ը����
					heroHitAction(); //����Ӣ�ۻ��͵л���ײ����
					gameOvaerAction(); //������Ϸ��������
					shootAction();  //Ӣ�ۻ����ڷ���
				}
				repaint(); //�ػ����ж���
			}
		};
		// ������ʱ����������
		timer.schedule(task, interval, interval);
	}
	//�ж���Ϸ�����ķ���
	public void gameOvaerAction() {
		if(hero.getLife()<=0) { //�������ֵС�ڵ���0
			//��Ϸ�������״̬
			state=GAME_OVER;
		}
	}
	//�ж�Ӣ�ۻ��͵л���ײ�ķ���
	public void heroHitAction() {
		//�������ел�
		for(int i=0;i<enemy.length;i++) {
			FlyingObject f=enemy[i];
			//�ж��Ƿ�͵�ǰ�л���ײ
			if(f.isLife() && hero.hit(f)) {
				//ײ���л�
				f.goDead();
				//Ӣ�ۻ������������
				hero.subLife();
				hero.clearDoubleFire();
			}
		}
	}
	//�ж��ӵ��͵л���ײ�ķ���
	public void bulletHitAction() {
		//���������ӵ�
		for(int i=0;i<bull.length;i++) {
			Bullet b=bull[i]; //��ȡ�ӵ�
			//�������ел�
			for(int j=0;j<enemy.length;j++) {
				FlyingObject f=enemy[j]; //��ȡ�л�
				//�жϻ��ел�
				if(b.isLife() && f.isLife() && b.hit(f)) {
					b.goDead(); //�ӵ���
					f.goDead(); //�л���
					//�жϵл����÷ֵ�
					if(f instanceof Score) {
						Score s=(Score)f;
						score+=s.getScore();
					}
					//�жϵл��Ƿ��н���
					if(f instanceof Award) {
						Award a=(Award)f;
						//�����ܽ������Ľ���ֵ
						int num=a.awardType();
						switch(num) {
						case Award.LIFE:
							hero.addLife(); //�����0 ����
							break;
						case Award.DOUBLE_FIRE: //�����1�ӻ���
							hero.addDoubleLire();
							break;
						}
					}
				}
			}
		}
	}
	//������ķ���
	public void outOfBoundsAction() {
		int index=0;//û�г����Ԫ���±꣬ͬʱ����û����Ԫ������
		FlyingObject[] fs=new FlyingObject[enemy.length];
		//�������ел�������Ƿ����
		for(int i=0;i<enemy.length;i++) {
			//��ȡ��ǰԪ��
			FlyingObject f=enemy[i];
			//�ж��Ƿ�û���粢��û�Ƴ���,
			if(!f.isOutOfBounds()&&!f.isRemove()) {
				fs[index]=f;
				index++;//�±��һ
			}
		}
		//��������
		enemy=Arrays.copyOf(fs, index);
		index=0; //�ӵ��ĳ���
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

	// �ӵ�����
	int shootIndex = 1;

	public void shootAction() {
		// ����Ӣ�ۻ����ڷ���
		if (shootIndex % 2 == 0) {
			Bullet[] bs = hero.shoot();
			// ��bull�����������
			bull = Arrays.copyOf(bull, bull.length + bs.length);
			// ��Ӣ�ۻ���������ڵ�
			// �ŵ����ݺ���������λ��
			System.arraycopy(bs, 0, bull, bull.length - bs.length, bs.length);
		}
		shootIndex++;
	}

	// �л���������
	int enterIndex = 1;

	public void enterAction() {
		if (enterIndex % 2 == 0) {
			// ����һ�ܵл�
			FlyingObject fo = nextEnemy();
			enemy = Arrays.copyOf(enemy, enemy.length + 1);// ��������
			enemy[enemy.length - 1] = fo;// �����ɵĵл��������ݺ���������
		}
		enterIndex++;
	}

	// �������ƶ��ķ���
	public void moveAction() {
		sky.step();// ����ƶ�
		// �л��ƶ�
		for (int i = 0; i < enemy.length; i++) {
			enemy[i].step();
		}
		for (int i = 0; i < bull.length; i++) {
			bull[i].step();
		}
	}

	// ��������л�����
	public FlyingObject nextEnemy() {
		Random ran = new Random();
		FlyingObject fo = null;
		int num = ran.nextInt(100);
		if (num < 40) {// 40%���ʳ���С�л�
			fo = new Airplane();
		} else if (num < 80) {// 40%���ʳ��ִ�л�
			fo = new Bigairplane();
		} else {// 20%���ɽ�����
			fo = new Bee();
		}
		return fo;
	}

	public void paint(Graphics g) { // ��д��JPanel�еķ��� �����������paint
		// �Ȼ��������ٻ�������
		sky.paintObject(g);
		hero.paintObject(g);
		for (int i = 0; i < enemy.length; i++) {
			enemy[i].paintObject(g);
		}
		for (int i = 0; i < bull.length; i++) {
			bull[i].paintObject(g);
		}
		//��ʾ����������ֵ
		g.drawString("SCORE"+score, 10, 10);
		g.drawString("LIFE"+hero.getLife(), 10, 30);
		//������Ϸ״̬����ͼƬ
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
		// ʵ����һ��java����
		JFrame f = new JFrame("�ɻ���ս");
		// ��World�����õ�������
		f.add(w);
		// ���ô��ڵĿ�͸�
		f.setSize(400, 700);
		// ���ô��ڹر�ʱ�������
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// ���ô��ھ���
		f.setLocationRelativeTo(null);
		// ��ʾ����,�Զ����������paint����
		f.setVisible(true);

		w.start();
	}

}
