package org.itemlist.web.helpers;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.itemlist.domain.Item;
import org.itemlist.domain.Picture;
import org.itemlist.service.GenericDao;
import org.itemlist.service.dao.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class EntityGenerator {

	@Autowired
	private ItemDao itemDao;

	public void generateDomain() {
		List<Picture> pictures = generatePicturesList();
		Item chairTask = new Item("Chair", "Ikea", pictures, 1);
		Item tableTask = new Item("Table", "Leenbakker", pictures, 2);
		Item cushionsTask = new Item("Cushions", "Kwantum", pictures, 3);

		addAll(itemDao, chairTask, tableTask, cushionsTask);

	}

	public List<Picture> generatePicturesList() {
		List<Picture> picList = new ArrayList<Picture>();
		Picture pic = new Picture();
		try {
//			InputStream fileinputStream = new FileInputStream("images/e1.jpg");
			
			InputStream fileinputStream = this.getClass().getClassLoader().getResourceAsStream("images/e1.jpg");
			byte[] buff = new byte[8000];
			int bytesRead = 0;
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			while ((bytesRead = fileinputStream.read(buff)) != -1) {
				bao.write(buff, 0, bytesRead);
			}
			byte[] data = bao.toByteArray();
			fileinputStream.close();
			pic.setPicId(1);
			pic.setPicName("e1.jpg");
			pic.setPicData(data);
			picList.add(pic);
			pic.setPicId((2));
			pic.setPicName("err.jpg");
			pic.setPicData(data);
			picList.add(pic);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return picList;

	}

	// public List<Picture> generatePicturesList() {
	// Picture pic = null;
	// List<Picture> picList = new ArrayList<Picture>();
	// try {
	// Enumeration<URL> urls =
	// servletContext.getClass().getClassLoader().getResources("images");
	// File[] files = new File("images").listFiles();
	// for (File file : files) {
	// if (file.isFile()) {
	// pic = new Picture();
	// pic.setPicId((Math.random()));
	// pic.setPicName(file.getName());
	// pic.setPicData(Files.readAllBytes(file.toPath()));
	// }
	// picList.add(pic);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return picList;
	// }

	public void deleteDomain() {
		List<Item> items = itemDao.list();
		for (Item item : items) {
			itemDao.remove(item);
		}
	}

	private <T> void addAll(GenericDao<T, Integer> dao, T... entites) {
		for (T o : entites) {
			dao.add(o);
		}
	}

	public byte[] extractBytes(String ImageName) throws IOException {
		// open image
		File imgPath = new File(ImageName);
		BufferedImage bufferedImage = ImageIO.read(imgPath);

		// get DataBufferBytes from Raster
		WritableRaster raster = bufferedImage.getRaster();
		DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

		return (data.getData());
	}

}
