package encryption;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.soap.Node;

import org.w3c.dom.NodeList;

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
	
	public void writeMetadata(List<Rectangle> selectedAreas) throws IOException{
		ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();

	    ImageWriteParam writeParam = writer.getDefaultWriteParam();
	    ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);

	    //adding metadata
	    IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);

	    IIOMetadataNode areasEntry = new IIOMetadataNode("AreasEntry");
	    String area = ""; 
	    for(Rectangle rect : selectedAreas)
	    	area += rect.toString();
	    areasEntry.setAttribute("selectedAreas", area);

	    IIOMetadataNode areas = new IIOMetadataNode("Areas");
	    areas.appendChild(areasEntry);

	    IIOMetadataNode root = new IIOMetadataNode("javax_imageio_png_1.0");
	    root.appendChild(areas);

	    metadata.mergeTree("javax_imageio_png_1.0", root);

	    //writing the data
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageOutputStream stream = ImageIO.createImageOutputStream(baos);
	    writer.setOutput(stream);
	    writer.write(metadata, new IIOImage(this.image, null, metadata), writeParam);
	    stream.close();
		
	}
	
	public void createFile(){
		try {
			ImageIO.write(image,"Mon Image cryptée", path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String readMetadata(byte[] imageData) throws IOException{
		ImageReader imageReader = ImageIO.getImageReadersByFormatName("png").next();

	    imageReader.setInput(ImageIO.createImageInputStream(new ByteArrayInputStream(imageData)), true);

	    // read metadata of first image
	    IIOMetadata metadata = imageReader.getImageMetadata(0);
	    
	    //this cast helps getting the contents
	    String cryptedAreas = null;
	    NodeList childNodes = ((org.w3c.dom.Node) metadata).getChildNodes();
	    for (int i = 0; i < childNodes.getLength(); i++) {
	        Node node = (Node) childNodes.item(i);
	        cryptedAreas = node.getAttributes().getNamedItem("selectedAreas").getNodeValue();
	    }
	    return cryptedAreas;
	}
}
