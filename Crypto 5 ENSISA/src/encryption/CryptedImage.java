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
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.soap.Node;

import org.w3c.dom.NodeList;

public class CryptedImage {

	private static final String STANDARD_METADATA_FORMAT = "javax_imageio_1.0";
    
	public static byte[] writeMetadata(List<Rectangle> selectedAreas, BufferedImage image) throws IOException {
		ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
	    ImageWriteParam writeParam = writer.getDefaultWriteParam();
	    ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);

	    //adding metadata
	    IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);

	    IIOMetadataNode textEntry = new IIOMetadataNode("tEXTeNTRY");
	    
	    StringBuilder area = new StringBuilder();
	    for(Rectangle rect : selectedAreas){
	    	area.append(rect.x);
    		area.append(" ");
    		area.append(rect.y);
    		area.append(" ");
    		area.append(rect.width);
    		area.append(" ");
    		area.append(rect.height);
	    	area.append("\n");
	    }
	    
	    textEntry.setAttribute("value", area.toString());
	    
	    IIOMetadataNode text = new IIOMetadataNode("tEXT");
	    text.appendChild(textEntry);

	    IIOMetadataNode root = new IIOMetadataNode(STANDARD_METADATA_FORMAT);
	    root.appendChild(text);

	    metadata.mergeTree(STANDARD_METADATA_FORMAT, root);

	    //writing the data
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageOutputStream stream = ImageIO.createImageOutputStream(baos);
	    writer.setOutput(stream); // on dit qu'on écrit dans baos
	    writer.write(metadata, new IIOImage(image, null, metadata), writeParam);
	    stream.close();
		return baos.toByteArray(); //contient l'image et ses metadata
	}

	public static String readMetadata(byte[] imageData) throws IOException{
		ImageReader imageReader = ImageIO.getImageReadersByFormatName("png").next();

	    imageReader.setInput(ImageIO.createImageInputStream(new ByteArrayInputStream(imageData)), true);

	    // read metadata of first image
	    IIOMetadata metadata = imageReader.getImageMetadata(0);
	    //this cast helps getting the contents
	    String cryptedAreas = null;
	    
	    IIOMetadataNode nodes = (IIOMetadataNode) metadata.getAsTree(STANDARD_METADATA_FORMAT);
	    System.out.println(nodes.getAttributes());
	    if (nodes.getElementsByTagName("Text").getLength()>0){
	    	IIOMetadataNode text = (IIOMetadataNode) nodes.getElementsByTagName("Text").item(0);
	    	IIOMetadataNode data = (IIOMetadataNode) text.getElementsByTagName("TextEntry").item(0);
		    cryptedAreas=data.getAttribute("value");
	    }
	    return cryptedAreas;
	}
}
