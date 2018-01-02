package encryption;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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

	private static final String STANDARD_METADATA_FORMAT = IIOMetadataFormatImpl.standardMetadataFormatName;

	/**
	 * Rajoute des metadonnées correspondant à selectedAreas dans image
	 * @param selectedAreas
	 * @param image
	 * @return
	 * @throws IOException
	 */
	public static byte[] writeMetadata(List<Rectangle> selectedAreas, BufferedImage image) throws IOException {
		ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
	    ImageWriteParam writeParam = writer.getDefaultWriteParam();
	    ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);

	    //adding metadata
	    IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);

    	IIOMetadataNode textEntry = new IIOMetadataNode("TextEntry");
    	
    	
    	textEntry.setAttribute("keyword", "rect");
    	textEntry.setAttribute("value", selectedAreas.toString());

	    IIOMetadataNode text = new IIOMetadataNode("Text");
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

	/**
	 * Récupère les métadonnées incluses dans l'image
	 * @param imageData
	 * @return
	 * @throws IOException
	 */
	public static String readMetadata(byte[] imageData) throws IOException{
		ImageReader imageReader = ImageIO.getImageReadersByFormatName("png").next();

	    imageReader.setInput(ImageIO.createImageInputStream(new ByteArrayInputStream(imageData)), true);

	    // read metadata of first image
	    IIOMetadata metadata = imageReader.getImageMetadata(0);
	    //this cast helps getting the contents
	    
	    IIOMetadataNode nodes = (IIOMetadataNode) metadata.getAsTree(STANDARD_METADATA_FORMAT);
	    
	    if (nodes.getElementsByTagName("Text").getLength()>0){ // On regarde si on a écrit dans Text
	    	IIOMetadataNode text = (IIOMetadataNode) nodes.getElementsByTagName("Text").item(0);
	    	NodeList data = (NodeList) text.getElementsByTagName("TextEntry");
	    	
	    	for(int j=0;j<data.getLength();j++){
	    		IIOMetadataNode reponse= (IIOMetadataNode) data.item(j);
	    		if (reponse.getAttribute("keyword").equals("rect")){ //Si c'est nos metadata
	    	    	System.out.println("metadata lues");
	    		    return reponse.getAttribute("value");
	    		}
	    	}
	    }
	    return null;//Si on a pas trouvé les metadata
	}
}