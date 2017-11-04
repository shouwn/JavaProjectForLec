package item;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import common.Point;
import common.Score;
import player.Player;

public class Items {
	private static HashMap<Integer, Item> itemType = new HashMap<Integer, Item>();
	private static int[] randWeight;
	
	// 수정할 요소 있음
	static {
		itemType.put(0, new TypeI01());
		itemType.put(1, new TypeI02());
		
		randWeight = new int[itemType.size()];
		randWeight[0] = 30;
		randWeight[1] = 70;
	}
	
	public static synchronized void deletItem(List<Item> list, Score score){
		Item item;
		
		for(int i = 0; i < list.size(); i++){
			item = list.get(i);
			
			if(!item.isUseful()){
				score.addScore(item.getScore());
				list.remove(i--);
			}
		}
		
	}
	
	public static synchronized void makeItem(List<Item> list, Point point){
		Random random = new Random();
		int r = random.nextInt(100);
		for(int i = 0; i < randWeight.length; i++){
			r -= randWeight[i];
			
			if(r < 0){
				list.add(itemType.get(i).makeSelf(point));
				break;
			}
		}
	}
	
	public static synchronized void moveItem(List<Item> list){
		for(int i = 0; i <list.size(); i++){
			list.get(i).moveSelf();
		}
	}
	
	public static synchronized void checkOutOfScreen(List<Item> list, int end){
		Item item;
		
		for(int i = 0; i < list.size(); i++){
			item = list.get(i);
			if(item.getPoint().getY() > end){
				item.setUseless();
			}
				
		}
	}
	
	public static synchronized void paintItems(List<Item> list, Graphics2D g){
		for(int i = 0; i < list.size(); i++){
			list.get(i).drawSelf(g);
		}
	}
	
	public static synchronized void checkItemcrashedPlayer(List<Item> list, Player player){
		Item item;
		
		for(int i = 0; i < list.size(); i++){
			item = list.get(i);
			if(item.isCrashed(player)){
				item.affectTo(player);
				item.setUseless();
			}
		}
	}
}
