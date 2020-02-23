package tedu.shoot;

//击中时的奖励
public interface Award {
	//定义奖励常量
	int LIFE=0;
	int DOUBLE_FIRE=1;
	//获得的奖励
	public int awardType();

}
