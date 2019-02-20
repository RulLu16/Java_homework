package hw4;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class BounceThread extends Frame implements ActionListener {
	private Canvas canvas;
	public static ArrayList<Ball> b=new ArrayList<Ball>();
	public BounceThread(String title) {
			super(title);
			canvas = new Canvas();
			this.setSize(400,300);
			this.add("Center", canvas);
			Panel p = new Panel();
			Button s = new Button("Start");
			Button c = new Button("Close");
			p.add(s); 
			p.add(c);
			s.addActionListener(this);
			c.addActionListener(this);
			this.add("South", p); 
			WindowDestroyer listener = new WindowDestroyer();
			this.addWindowListener(listener);
			this.setVisible(true);
		}
	public void actionPerformed(ActionEvent evt) {
		Dimension ds=getSize();
		Insets in=getInsets();
		int x=(ds.width-in.left-in.right)/2;
		int y=(ds.height-in.top-in.bottom)/2;
			if (evt.getActionCommand() == "Start") {	
				for(int i=0;i<5;i++) {
					b.add(new Ball(canvas,20,20,i*(Math.PI/5),x,y));
					b.get(i).start();
				}
			}
			else if (evt.getActionCommand() == "Close")
				System.exit(0);
		}
class Ball extends Thread{
	private Canvas box;
	private int XSIZE = 20;
	private int YSIZE = 20;
	private double r;
	private int time=0;
	private int remove_flag=0; // 1 mean it is removed
	private boolean isRunnable=true;
	private int x;
	private int y;
	private int babe=0;
	private double dx = 2;
	private double dy = 2;
	public Ball(Canvas c, int s, int t,double r,int x, int y) {
		box = c; 
		this.XSIZE=s;
		this.YSIZE=t;
		this.x=x;
		this.y=y;
		this.r=r;
		this.dy=dx*Math.tan(this.r);
		if(this.r>=Math.PI/2 && this.r<3*Math.PI/2)
			this.dx=-this.dx;
	}   
	public void draw() {
		Graphics g = box.getGraphics();
		g.fillOval(x, y, XSIZE, YSIZE);
		g.dispose(); 
	}
	public void move() {
		Graphics g = box.getGraphics();
		g.setXORMode(box.getBackground());
		g.fillOval(x, y, XSIZE, YSIZE);
		x += dx;
		y += dy;
		time++;
		Dimension d = box.getSize();
		if (x < 0) {
			x = 0;
			dx = -dx;
		}
		if (x + XSIZE >= d.width) {
			x = d.width - XSIZE; 
			dx = -dx;
		}
		if (y < 0) {
			y = 0; 
			dy = -dy; 
		}
		if (y + YSIZE >= d.height) { 
			y = d.height - YSIZE;
			dy = -dy;
		}
		if(time>XSIZE*2.5 && this.remove_flag==0) {
			babe=1;
			crash();
		}
		g.fillOval(x, y, XSIZE, YSIZE);
		g.dispose(); 
		
	}
	public synchronized void crash() {
		int dis=0;
		int disx=0;
		int disy=0;
		for(int i=0;i<b.size();i++) {
			if(b.get(i).remove_flag==0 && b.get(i).babe==1) {
				disx=this.x-b.get(i).x;
				disy=this.y-b.get(i).y;
				dis=(int)Math.sqrt(disx*disx+disy*disy);
				if(dis<=(int)((b.get(i).XSIZE+b.get(i).XSIZE)/2) && b.indexOf(this)!=i) {
					split(i);
					break;
				}
			}
		}
	}
				
	public synchronized void split(int target) {
		int onex=this.XSIZE/2;
		int oney=this.YSIZE/2;
		double origin_rone=this.r-Math.PI/4;
		double new_rone=this.r+Math.PI/4;
		Ball t=b.get(target);
		int twox=t.XSIZE/2;
		int twoy=t.YSIZE/2;
		double origin_rtwo=t.r-Math.PI/4;
		double new_rtwo=t.r+Math.PI/4;
		if(onex<=1 || twox<=1) 
			System.exit(0);
		else {
		Ball origin_one=new Ball(canvas,onex,oney,origin_rone,this.x,this.y);
		Ball new_one=new Ball(canvas,onex,oney,new_rone,this.x,this.y);
		b.add(origin_one);
		b.add(new_one);
		Ball origin_two=new Ball(canvas,twox,twoy,origin_rtwo,t.x,t.y);
		Ball new_two=new Ball(canvas,twox,twoy,new_rtwo,t.x,t.y);
		b.add(origin_two);
		b.add(new_two);
		Graphics g = box.getGraphics();
		g.setXORMode(box.getBackground());
		g.fillOval(this.x, this.y, this.XSIZE, this.YSIZE);
		g.fillOval(t.x, t.y, t.XSIZE, t.YSIZE);
		this.isRunnable=false;
		t.isRunnable=false;
		t.remove_flag=1;
		//b.remove(target);
		this.remove_flag=1;
		//int re=b.indexOf(this);
		//b.remove(re);
		origin_one.start();
		new_one.start();
		origin_two.start();
		new_two.start();
		}
	}
	public void run() {
		draw();
		while (isRunnable) {
			move();
			try { Thread.sleep(5); }
			catch(InterruptedException e) {}
			}
		
		}
	}
}
public class s20171666hw4 {
	public static void main(String[] args) {
		BounceThread b=new BounceThread("Bounce Thread");
		 }
}