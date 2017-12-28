package components;

import java.awt.Adjustable;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;



public class ScrollBar extends JScrollPane{
	
	public ScrollBar(JPanel j , String type){
		 if (type == "Horizontal"){
			 this.createHorizontalScrollBar();
			 this.createVerticalScrollBar();
	         this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	         this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	         this.setBounds(j.getWidth()-15, 0, 15, j.getHeight());
	         AdjustmentListener listener = new MyAdjustmentListener();
		     this.getHorizontalScrollBar().addAdjustmentListener(listener);
		     this.setVisible(true);
		 }
		 else if (type == "Vertical"){
			 this.createVerticalScrollBar();
			 this.createHorizontalScrollBar();
	         this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	         this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	         this.setBounds(0, j.getHeight()-15, j.getWidth(), 15);
	         AdjustmentListener listener = new MyAdjustmentListener();
		     this.getHorizontalScrollBar().addAdjustmentListener(listener);
		     this.getVerticalScrollBar().addAdjustmentListener(listener);
		     this.setVisible(true);
		 }
	}
	
	class MyAdjustmentListener implements AdjustmentListener {
		  public void adjustmentValueChanged(AdjustmentEvent evt) {
		    Adjustable source = evt.getAdjustable();
		    if (evt.getValueIsAdjusting()) {
		      return;
		    }
		    int orient = source.getOrientation();
		    if (orient == Adjustable.HORIZONTAL) {
		      System.out.println("from horizontal scrollbar"); 
		    } else {
		      System.out.println("from vertical scrollbar");
		    }
		    int type = evt.getAdjustmentType();
		    switch (type) {
		    case AdjustmentEvent.UNIT_INCREMENT:
		      System.out.println("Scrollbar was increased by one unit");
		      break;
		    case AdjustmentEvent.UNIT_DECREMENT:
		      System.out.println("Scrollbar was decreased by one unit");
		      break;
		    case AdjustmentEvent.BLOCK_INCREMENT:
		      System.out.println("Scrollbar was increased by one block");
		      break;
		    case AdjustmentEvent.BLOCK_DECREMENT:
		      System.out.println("Scrollbar was decreased by one block");
		      break;
		    case AdjustmentEvent.TRACK:
		      System.out.println("The knob on the scrollbar was dragged");
		      break;
		    }
		    //int value = evt.getValue();
		  }
	}
}
