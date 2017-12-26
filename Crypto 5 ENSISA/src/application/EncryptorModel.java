package application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class EncryptorModel extends JPanel implements MouseListener, MouseMotionListener, ImageObserver{

    private BufferedImage image = null;
    private Point porig = null; 
    private Point pmove = null; 

    EncryptorModel() {
        super();
    }

    public BufferedImage getImage() {
        return image;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g; 
        if (image != null)
            g2.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
        if (pmove != null && porig != null) { 
            g2.setColor(Color.BLACK); 
            
            // calcul de la selection 
          final Rectangle rect = new Rectangle((pmove.x > porig.x) ? porig.x : pmove.x, 
                            (pmove.y > porig.y) ? porig.y : pmove.y, 
                            (pmove.x > porig.x) ? pmove.x - porig.x : porig.x 
                                    - pmove.x, (pmove.y > porig.y) ? pmove.y 
                                    - porig.y : porig.y - pmove.y); 
            // dessine le fond de la selection avec un effet de transparence 
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f)); 
            g2.fillRect(rect.x, rect.y, rect.width, rect.height); 
            // suppression de la transparence pour dessiner la bordure 
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); 
            g2.drawRect(rect.x, rect.y, rect.width, rect.height);  
    }
    
  }

    protected void addImage(File imageFile) {
        try {
            image = ImageIO.read(imageFile);
            porig = null; 
            pmove = null; 
            // ajout des listeners 
            addMouseListener(this); 
            addMouseMotionListener(this); 
        } catch (IOException e) {
            e.printStackTrace();
        }
        repaint();
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		 pmove = e.getPoint(); // récupération du point en déplacement 
	        // repaint du composant 
	        repaint(); 
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		 porig = e.getPoint(); // récupération du point d'origine 
	        // repaint du composant 
	        repaint(); 
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		  final Rectangle rect = new Rectangle(
				  (pmove.x > porig.x) ? porig.x : pmove.x, 
                  (pmove.y > porig.y) ? porig.y : pmove.y, 
                  (pmove.x > porig.x) ? pmove.x - porig.x : porig.x - pmove.x,
                  (pmove.y > porig.y) ? pmove.y - porig.y : porig.y - pmove.y
          );
	 
		  System.out.println(rect.getX());
		  System.out.println(rect.getY());
		  System.out.println(rect.getWidth());
		  System.out.println(rect.getHeight());

		
		
	}

}
