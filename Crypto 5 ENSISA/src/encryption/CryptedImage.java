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
	 * Décrit les rectangles dans un fichier
	 * @param selectedAreas
	 * @param image
	 */
	public static void writeMetadataInFile(List<Rectangle> selectedAreas, BufferedImage image){
		try {
			File f= new File("resources/metadata.txt");
			FileOutputStream fos = new FileOutputStream(f.getAbsolutePath());
			for(Rectangle r:selectedAreas){
				String s=r.x+","+r.y+","+r.width+","+r.height+"\n";
				fos.write(s.getBytes());
			}
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Lis la description des rectangles cryptés dans le fichier
	 * @return
	 */
	public static List<Rectangle> readMetadataInFile(){
		try {
			File f= new File("resources/metadata.txt");
			FileInputStream fos = new FileInputStream(f.getAbsolutePath());
			int n;
			int indice=0;
			byte[] file= new byte[fos.available()];
			while ((n=fos.read()) != -1){
				file[indice]= (byte) n;
				indice++;
			}
			fos.close();
			
			String infos=new String(file);
			ArrayList<Rectangle> r= new ArrayList<>();
			for(String s:infos.split("\n")){
				String[] value=s.split(",");
				int x=Integer.parseInt(value[0]);
				int y=Integer.parseInt(value[1]);
				int width=Integer.parseInt(value[2]);
				int height=Integer.parseInt(value[3]);
				r.add(new Rectangle(x,y,width,height));
			}
			return r;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
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
    	
    	StringBuilder area = new StringBuilder();
	    for(Rectangle rect : selectedAreas){
	    	area.append(rect.toString());
	    }
    	textEntry.setAttribute("keyword", "rect");
    	textEntry.setAttribute("value", area.toString());

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
	    String cryptedAreas = null;
	    
	    IIOMetadataNode nodes = (IIOMetadataNode) metadata.getAsTree(STANDARD_METADATA_FORMAT);
	    for(int i=0;i<nodes.getLength();i++){
	    	System.out.println(nodes.item(i).getNodeName());
	    }
	    if (nodes.getElementsByTagName("Text").getLength()>0){
	    	IIOMetadataNode text = (IIOMetadataNode) nodes.getElementsByTagName("Text").item(0);
	    	IIOMetadataNode data = (IIOMetadataNode) text.getElementsByTagName("TextEntry").item(0);
	    	cryptedAreas=data.getAttribute("value");
	    	System.out.println("metadata lues");
	    }
	    
	    return cryptedAreas;
	}
}
