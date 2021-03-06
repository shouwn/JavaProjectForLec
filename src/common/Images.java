package common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// 한 폴더에서 이미지 여러장 불러올 때 쓸 유틸 클래스
public class Images {

	public static BufferedImage[] readImages(String path){
		BufferedImage[] images;

		File dirFile = new File(path);
		File[] fileList = dirFile.listFiles();

		if(fileList == null)
			System.out.println("잘못된 이미지 path");

		images = new BufferedImage[fileList.length];
		for(int i = 0; i < fileList.length; i++){
			try {
				images[i] = ImageIO.read(fileList[i]);
			} catch (IOException e) {
				System.err.println("FAIL READ IMAGE BY readImages()");
				e.printStackTrace();
			}
		}

		return images;
	}
}
