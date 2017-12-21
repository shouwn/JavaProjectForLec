package item;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import common.Point;
import common.Score;
import common.Sound;
import player.Player;

public class Items {
	private static HashMap<Integer, Item> itemType = new HashMap<Integer, Item>();

	public static final int ITEM_FORK = 0;
	public static final int COOL_PEACE_LARGE = 1;
	public static final int COOL_PEACE = 2;
	public static final int FISH_CAKE = 3;
	public static final int STEAMEDEGG = 4;
	public static final int NURUNGJI = 5;
	public static final int RICE_BALL = 6;
	
	static {
		itemType.put(0, new TypeI00());
		itemType.put(1, new TypeI01());
		itemType.put(2, new TypeI02());
		itemType.put(3, new TypeI03());
		itemType.put(4, new TypeI04());
		itemType.put(5, new TypeI05());
		itemType.put(6, new TypeI06());
	}

	public static synchronized void deletItem(List<Item> list, Score score){
		Item item;

		for(int i = 0; i < list.size(); i++){
			item = list.get(i);

			if(!item.isUseful()){
				new Sound(Sound.GOTITEM).playMusic(false);
				score.addScore(item.getScore());
				list.remove(i--);
			}
			else if(item.isOutOfScreen())
				list.remove(i--);
		}

	}

	public static synchronized void makeItem(List<Item> list, Map<Item, Integer> eachItemProbability, Point point){
		Random random = new Random();
		int r = random.nextInt(100);
		Integer probability;

		for(int i = 0; i < itemType.size(); i++){
			probability = eachItemProbability.get(itemType.get(i));

			if(probability != null)
				r -= probability;

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
				item.setOutOfScreen();
			}

		}
	}

	public static synchronized void paintItems(List<Item> list, Graphics2D g){
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).isUseful())
				list.get(i).drawSelf(g);
		}
	}

	public static synchronized void checkItemcrashedPlayer(List<Item> list, Player player){
		Item item;

		for(int i = 0; i < list.size(); i++){
			item = list.get(i);
			if(item.isCrashed(player)){
				new Sound(Sound.GOTITEM).playMusic(false);
				item.affectTo(player);
				item.setUseless();
			}
		}
	}

	public static Item getItemType(int key) {
		return itemType.get(key);
	}
}
