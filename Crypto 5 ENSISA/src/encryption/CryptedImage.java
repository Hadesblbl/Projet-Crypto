package encryption;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

public class CryptedImage {

	private BufferedImage image;
	private File path;
	
	public CryptedImage(File image){
		this.path = image;
		try {
			this.image = ImageIO.read(this.path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeMetadata(BufferedImage buffImg, String key, String value) throws IOException{
		ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();

	    ImageWriteParam writeParam = writer.getDefaultWriteParam();
	    ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);

	    //adding metadata
	    IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);

	    IIOMetadataNode textEntry = new IIOMetadataNode("tEXtEntry");
	    textEntry.setAttribute("keyword", key);
	    textEntry.setAttribute("value", value);

	    IIOMetadataNode text = new IIOMetadataNode("tEXt");
	    text.appendChild(textEntry);

	    IIOMetadataNode root = new IIOMetadataNode("javax_imageio_png_1.0");
	    root.appendChild(text);

	    metadata.mergeTree("javax_imageio_png_1.0", root);

	    //writing the data
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageOutputStream stream = ImageIO.createImageOutputStream(baos);
	    writer.setOutput(stream);
	    writer.write(metadata, new IIOImage(buffImg, null, metadata), writeParam);
	    stream.close();
		
	}
	
	public void createFile(){
		try {
			ImageIO.write(image,"Mon Image cryptée",path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readMetadata(){
		
	}
}
