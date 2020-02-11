
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.*;

import java.util.*;

public class GraphDraw extends JPanel implements Serializable{
	
	static ArrayList<Line2D> lines = null;
	
	static String[] m = {"Node","Edge"};
	static JComboBox mode;
	
	static String modeChoice = "Node";
	
	static String choice = "add"; 
	static Point start = null; 
	static Point end = null; 
	static JRadioButton add;  
	static JRadioButton del;  
	static JRadioButton mov;  
	
	
	boolean pt1 = false;
	int index;					
	Point move = new Point();	
	Point move2 = new Point();
	
	 
	static ArrayList<Point> nodes = null;
	ArrayList<Integer> lindex = new ArrayList<Integer>();
	ArrayList<Integer> plindex = new ArrayList<Integer>();
	
	int pindex;
	
	Point pmove = new Point();
	Point pmove2 = new Point();
	
	int px,py;
	
	Point tempPoint = new Point(-15,15);
	
	boolean unclick = false;	
	static Color c = Color.red;		
	
	public GraphDraw() {
		
		
		
		start = new Point();
		end = new Point();
		nodes = new ArrayList<Point>();
		lines = new ArrayList<Line2D>();
		
		ML listener = new ML();
        addMouseListener(listener);
        addMouseMotionListener(listener);
	}
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("edges"); 
		GraphDraw draw = new GraphDraw(); 
		JPanel option = new JPanel();	
		JLayeredPane panel = new JLayeredPane(); 
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setPreferredSize(new Dimension(400, 400)); 
        frame.setLayout(new BorderLayout());			
        frame.getContentPane().add(panel, BorderLayout.CENTER);	
        
        panel.setBounds(0,0,500,500); 
        draw.setBackground(Color.white); 
        option.setBounds(0,0,400,25); 
        draw.setBounds(0,100,400,375);
        
        option.setSize(400,100);
        
		panel.add(draw,0); 
		panel.add(option,1);
		
		
		add = new JRadioButton("Add"); 
		add.setActionCommand("add");
		add.setSelected(true);
		del = new JRadioButton("Delete");
		del.setActionCommand("del");
		mov = new JRadioButton("Move");
		mov.setActionCommand("mov");
		
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(add);
		bg.add(del);
		bg.add(mov);
		
		mode = new JComboBox(m);
		mode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JComboBox cb = (JComboBox)e.getSource();
				modeChoice = (String) cb.getSelectedItem();
				if(modeChoice.equals("Edge")) {
					mov.setVisible(false);
				}
				else {
					mov.setVisible(true);
				}
			}
			
		});
		option.add(mode);
		
		option.add(add);
		option.add(del);
		option.add(mov);
		
		JButton save = new JButton("Save");
		option.add(save);
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try
		        {    
		            //Saving Item into a file 
		            FileOutputStream file = new FileOutputStream("lines.ser"); 
		            ObjectOutputStream out = new ObjectOutputStream(file); 
		              
		            // Method for serializing a Item
		            out.writeObject(lines);
		            out.writeObject(nodes);
		              
		            out.close(); 
		            file.close(); 
		            
		            
		            
		            JPanel p = new JPanel();
	    			JOptionPane.showMessageDialog(p,"Graph has been saved"); 
		  
		        } 
		        catch(IOException ex) 
		        { 
		        	JPanel p = new JPanel();
	    			JOptionPane.showMessageDialog(p,"Error: Graph couldn't be saved"); 
		        } 
			}
			
		});
		
		JButton load = new JButton("Load");
		option.add(load);
		load.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				try
		        {    
		            lines.clear();
		            nodes.clear();
		            c = Color.white;
		            draw.repaint();
					
					//loading Item from file
		            FileInputStream file = new FileInputStream("lines.ser"); 
		            ObjectInputStream in = new ObjectInputStream(file); 
		              
		            //Method for deserializing Item
		            lines = (ArrayList<Line2D>)in.readObject();            
		            nodes = (ArrayList<Point>)in.readObject();
		              
		            in.close(); 
		            file.close(); 
		              
		            System.out.println("Graph has been loaded ");
		            
		            c = Color.red;
		            draw.repaint();
		            
		        } 
		          
		        catch(Exception ex) 
		        { 
		        	JPanel p = new JPanel();
	    			JOptionPane.showMessageDialog(p,"Error: Could not Load.");
		        } 
				
			}

			
			
		});
		
		
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = e.getActionCommand();
			}
		});
		del.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = e.getActionCommand();
			}
		});
		mov.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = e.getActionCommand();
			}
		});
		
		
		frame.setSize(400,400);
		frame.setVisible(true);	
	}
	
	class ML extends MouseAdapter{
		
		popup pu; 
		public ML() {
			pu = new popup(); 
		}
		
	    public void mousePressed(MouseEvent e) { 
	    	if(modeChoice.equals("Edge")) {
		    	if(choice.equals("add")) {
		    		if(nodes.size()>0) {
			    		for(Point p: nodes) {
			    			if(p.distance(e.getPoint()) <= 30){
			    				start.setLocation(p.x,p.y);
			    			}
			    		}
		    		}
		    		else {
		    			JPanel p = new JPanel();
		    			JOptionPane.showMessageDialog(p,"There are no nodes at the moment.\n Please create one before adding a edge.");
		    		}
		    	}
		    	else if(choice.equals("mov")){
		    		if(lines.size() == 0) { 
		    			JPanel p = new JPanel();
		    			JOptionPane.showMessageDialog(p,"There are no lines at the moment.\n Please create one before moving.");
		    		}
		    		else {
		    			for(Line2D l: lines) {
		    				if(l.ptLineDist(e.getPoint())<= 15) {
		    					index = lines.indexOf(l);
		    					if(l.getP1().distance(e.getPoint()) <= l.getP2().distance(e.getPoint())) {
		    						pt1 = true;
		    						move.setLocation(e.getPoint());
		    						move2.setLocation(l.getP2());
		    						start.setLocation(move2);;
		    					}
		    					else {
		    						pt1 = false;
		    						move.setLocation(l.getP1());
		    						move2.setLocation(e.getPoint());
		    						start.setLocation(move);;
		    					}
		    					break;
		    				}
		    			}
		    			repaint();
		    		}
		    	
		    	}
	    	}
	    	else {
	    		if(choice.equals("add")) {
	    			tempPoint.setLocation(e.getX(), e.getY());
	    			repaint();
	    		}
	    		else if(choice.equals("mov")) {
	    			for(Point p: nodes) {
	    				if(p.distance(e.getPoint()) <= 10) {
	    					pindex = nodes.indexOf(p);
	    					for(Line2D l : lines) {
	    						if(l.ptLineDist(p) <= 10) {
	    							lindex.add(lines.indexOf(l));
	    							if(l.getP1().distance(p)<l.getP2().distance(p)) {
	    								plindex.add(1);
	    							}
	    							else {
	    								plindex.add(2);
	    							}
	    						}
	    					}
	    					break;
	    				}
	    			}
	    		}
	    	}
	    }

	    public void mouseDragged(MouseEvent e) {
	    	if(modeChoice.equals("Edge")) {
		    	if(choice.equals("add")) { 
			    	end.setLocation(e.getX(),e.getY());
			    	for(Point p: nodes) {
			    		if(p.distance(e.getPoint())<= 30) {
			    			end.setLocation(p);
			    		}
			    	}
			        repaint();
			        repaint();
		    	}
		    	else if(choice.equals("mov")) {
		    		/*
		    		if(pt1 == true) {
		    			move.setLocation(e.getPoint());
		    			end.setLocation(move);
		    		}
		    		else {
		    			move2.setLocation(e.getPoint());
		    			end.setLocation(move2);
		    		}
		    		lines.get(index).setLine(move,move2);
		    		repaint();
		    		*/
		    	}
	    	}
	    	else {
	    		if(choice.equals("add")) {
	    			
	    			tempPoint.setLocation(e.getX(), e.getY());
	    			repaint();
	    		}
	    		else if(choice.equals("mov")) {
	    			nodes.get(pindex).setLocation(e.getPoint());
	    			for(Integer i: lindex) {
	    				int dex = lindex.indexOf(i);
	    				if(plindex.get(dex) == 1) {
	    					lines.get(i).setLine(nodes.get(pindex), lines.get(i).getP2());
	    					repaint();
	    				}
	    				else if(plindex.get(dex) == 2) {
	    					lines.get(i).setLine(lines.get(i).getP1(), nodes.get(pindex));
	    					repaint();
	    				}
	    			}
	    			repaint();
	    		}
	    	}
	    }

	    public void mouseReleased(MouseEvent e) {
	    	if(e.isPopupTrigger()) { 
	    		pu.show(e.getComponent(), e.getX(), e.getY());
	    	}
	    	if(modeChoice.equals("Edge")) {		    	
			    	if(choice.equals("add")) {
			    		for(Point p: nodes) {
				    			if(p.distance(e.getPoint()) <= 30) {
				    				repaint();
				    				lines.add(new Line2D.Double(start,p));
				    			}
				    		}
					        repaint();			    		
			    	}
			    	else if(choice.equals("del")){ 
			    		if(lines.size() == 0) { 
			    			JPanel p = new JPanel();
			    			JOptionPane.showMessageDialog(p,"There are no lines at the moment.\n Please create one before deleting.");
			    		}
			    		else {
				    		for(Line2D l: lines) { 
				    			if(l.ptLineDist(e.getPoint()) <= 15) {
				    				lines.remove(l); 
				    				repaint();
				    				break;
				    			}
				    			
				    		}
			    		}
			    	}
			    	else {
			    		/*
			    		boolean set = false;
			    		for(Point p:nodes) {
				    		if(pt1 == true) {
				    			move.setLocation(e.getPoint());
				    			if(e.getPoint().distance(p)<= 30) {
				    				set = true;
				    				move.setLocation(p);
				    				lines.get(index).setLine(move,move2);
				    			}
				    		}
				    		else {
				    			move2.setLocation(e.getPoint());
				    			if(e.getPoint().distance(p)<= 30) {
				    				set = true;
				    				move2.setLocation(p);
				    				lines.get(index).setLine(move,move2);
				    			}
				    		}
			    		}
			    		if(set == false) {
			    			lines.remove(index);
			    		}
			    		repaint();
			    		*/
			    	}		    	
	    	}
	    	else {
	    		if(choice.equals("add")) {
	    			nodes.add(new Point(e.getX(),e.getY()));
	    			tempPoint.setLocation(-50, -50);
	    			repaint();
	    		}
	    		else if(choice.equals("del")) {
	    			for(Point p: nodes) {
	    				if(p.distance(e.getPoint()) <= 15) {
	    					nodes.remove(p);
	    					break;
	    				}
	    			}
	    			ArrayList<Line2D> temp = new ArrayList<Line2D>();
	    			for(Line2D l: lines) {
    					if(l.getP1().distance(e.getPoint()) <= 15 || l.getP2().distance(e.getPoint()) <= 15) {
    						temp.add(l);
    					}
	    			}
	    			for(Line2D t: temp) {
	    				lines.remove(t);
	    			}
	    			repaint();	
	    		}
	    		else {
	    			lindex.clear();
	    			plindex.clear();
	    		}
	    	}
	    }
	    public void clear(){ 
	    	c = Color.white; 
	    	repaint(); 
	    	nodes.clear();
	    	lines.clear(); 
	    }
	    
	    class popup extends JPopupMenu{ 
			popup(){
				JMenuItem mi = new JMenuItem("Clear");
				add(mi);
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						clear();
					}
				});
			}	
		}
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g; 
		g2d.setColor(c); 
		if(choice.equals("add")&& modeChoice.equals("Node")) {
			g2d.fillOval(tempPoint.x -5, tempPoint.y-5, 10, 10);
		}
		else if(choice.equals("add")&& modeChoice.equals("Edge")) {
			drawArrowLine(g2d,start.x,start.y,end.x,end.y,10,5);			
		}
		
		for(Point p: nodes) {
			//System.out.println(p);
			g2d.fillOval(p.x -5, p.y -5, 10, 10);
		}
		
		for(Line2D l: lines) { 
			//System.out.println(l);
			drawArrowLine(g2d,(int)l.getX1(),(int)l.getY1(),(int)l.getX2(),(int)l.getY2(),10,5);
		}
		
		c = Color.red;
		
	}
	private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
	    int dx = x2 - x1, dy = y2 - y1;
	    double D = Math.sqrt(dx*dx + dy*dy);
	    double xm = D - d, xn = xm, ym = h, yn = -h, x;
	    double sin = dy / D, cos = dx / D;

	    x = xm*cos - ym*sin + x1;
	    ym = xm*sin + ym*cos + y1;
	    xm = x;

	    x = xn*cos - yn*sin + x1;
	    yn = xn*sin + yn*cos + y1;
	    xn = x;

	    int[] xpoints = {x2, (int) xm, (int) xn};
	    int[] ypoints = {y2, (int) ym, (int) yn};

	    g.drawLine(x1, y1, x2, y2);
	    g.fillPolygon(xpoints, ypoints, 3);
	}

}
