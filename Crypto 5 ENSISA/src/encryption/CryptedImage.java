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

	private static final String STANDARD_METADATA_FORMAT = "javax_imageio_1.0";

	public static byte[] writeMetadata(List<Rectangle> selectedAreas, BufferedImage image) throws IOException {
		ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
	    ImageWriteParam writeParam = writer.getDefaultWriteParam();
	    ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);

	    //adding metadata
	    IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);

	    IIOMetadataNode areasEntry = new IIOMetadataNode("AreasEntry");
	    StringBuilder area = new StringBuilder();
	    for(Rectangle rect : selectedAreas)
	    	area.append(rect.toString());
	    areasEntry.setAttribute("selectedAreas", area.toString());

	    IIOMetadataNode areas = new IIOMetadataNode("Areas");
	    areas.appendChild(areasEntry);

	    IIOMetadataNode root = new IIOMetadataNode(STANDARD_METADATA_FORMAT);
	    root.appendChild(areas);

	    metadata.mergeTree(STANDARD_METADATA_FORMAT, root);

	    //writing the data
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageOutputStream stream = ImageIO.createImageOutputStream(baos);
	    writer.setOutput(stream);
	    writer.write(metadata, new IIOImage(image, null, metadata), writeParam);
	    stream.close();
		return baos.toByteArray();
	}

	public static String readMetadata(byte[] imageData) throws IOException{
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
