package hw3;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

class Calculator extends Frame implements WindowListener, ActionListener{
	JTextPane ex;
	Button[] button;
	int operation_flag=0; // 1 mean there is operator
	int finish_flag=0; // 1 mean '=' action
	int num_flag=0; // 1 mean there is number
	String math;
	int exsize=0;
	int zero_flag=0; // 1 mean there is zero
	int bracket_flag=0; // non-zero mean there # of (
	int nonumber=0; // for ) operator
	int dot_flag=0; // 1 mean there is a dot
	double result=0;
	
	Calculator(){
		int i;
		ex=new JTextPane();
		StyledDocument doc = ex.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_RIGHT);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		ex.setPreferredSize(new Dimension(50,50));
		ex.setEditable(false);
		String[] buttonshape= {"7","8","9","/","C","4","5","6","*","<-","1","2","3","-","(","0",".","=","+",")"};
		button=new Button[20];
		Panel pan=new Panel();
		pan.setLayout(new GridLayout(4,5,5,5));
		
		for(i=0;i<20;i++)
		{
			button[i]=new Button(buttonshape[i]);
			pan.add(button[i]);
			button[i].addActionListener(this);
		} // insert button
		this.setSize(500,500);
		this.add("North", ex);
		this.add("Center",pan);
		this.setTitle("Calculator");
		this.setVisible(true);
		WindowDestroyer listener = new WindowDestroyer(); 
		this.addWindowListener(listener);
	} // make calculator
	public void windowActivated(WindowEvent e) { }
	public void windowClosed(WindowEvent e) { }	
	public void windowClosing(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) { }
	public void windowDeiconified(WindowEvent e) { }
	public void windowIconified(WindowEvent e) { }
	public void windowOpened(WindowEvent e) { }
	public void actionPerformed(ActionEvent e) {
		String in=e.getActionCommand();
		String temp;
		if(finish_flag==0)
		{
			if(in.equals("C"))
			{
				exsize=0;
				ex.setText("");
				num_flag=0;
				zero_flag=0;
				dot_flag=0;
				nonumber=0;
				bracket_flag=0;
				operation_flag=0;
			} // delete all
			else if(in.equals("<-"))
			{
				if(exsize>1)
				{
					exsize--;
					temp=ex.getText();
					String check=temp.substring(exsize);
					temp=temp.substring(0,exsize);
					ex.setText(temp);
					if(check.equals("."))
						dot_flag=0;
					else if(check.equals("("))
						bracket_flag--;
					else if(check.equals(")")) {
						nonumber=0;
						bracket_flag++;
					}
					if(temp.charAt(exsize-1)>='0' && temp.charAt(exsize-1)<='9')
					{
						num_flag=1;
						nonumber=0;
						operation_flag=0;
					}
					else if(temp.charAt(exsize-1)=='/' || temp.charAt(exsize-1)=='*'||temp.charAt(exsize-1)=='-'||temp.charAt(exsize-1)=='+')
					{
						num_flag=0;
						zero_flag=0;
						operation_flag=1;	
					}
					else if(temp.charAt(exsize-1)==')') {
						nonumber=1;
						num_flag=1;
						operation_flag=0;
					}
					else if(temp.charAt(exsize-1)=='(') {
						num_flag=0;
						zero_flag=0;
						operation_flag=0;
					}
				}
				else if(exsize==1)
				{
					exsize=0;
					ex.setText("");
					num_flag=0;
					operation_flag=0;
				}				
			} // delete one
			else if(in.charAt(0)>='1' && in.charAt(0)<='9')
			{
				if(nonumber ==0) {
					if(zero_flag==0) {
						ex.setText(ex.getText()+in);
						exsize++;
					}
					else
					{
						temp=ex.getText();
						temp=temp.substring(0,exsize-1);
						ex.setText(temp+in);
						zero_flag=0;
					}
					operation_flag=0;
					num_flag=1;
				}
			}
			else if(in.equals("0"))
			{
				operation_flag=0;
				if(nonumber==0) {
					if(num_flag == 1) {
						ex.setText(ex.getText()+in);
						exsize++;	
					}
					else {
						if(zero_flag==0) {
							ex.setText(ex.getText()+in);
							exsize++;
							zero_flag=1;	
						}
					}
				}
			}
			else if(in.equals("/") || in.equals("-") || in.equals("+") || in.equals("*")) {
				if(operation_flag==1)
				{
					temp=ex.getText();
					temp=temp.substring(0,exsize-1);
					ex.setText(temp+in);
				}
				else
				{
					if(num_flag==1 || zero_flag==1)
					{
						ex.setText(ex.getText()+in);
						operation_flag=1;
						exsize++;
						nonumber=0;
					}
				}
				num_flag=0;
				dot_flag=0;
				zero_flag=0;
			}
			else if(in.equals("("))
			{
				if(num_flag != 1)
				{
					ex.setText(ex.getText()+in);
					operation_flag=0;
					exsize++;
					bracket_flag++;
				}
			}
			else if(in.equals(")")){
				if(bracket_flag != 0 && operation_flag != 1) {
					ex.setText(ex.getText()+in);
					exsize++;
					bracket_flag--;
					nonumber=1;
				}
			}
			else if(in.equals("="))
			{
				if(operation_flag != 1 && bracket_flag ==0) {
					finish_flag=1;
					ex.setText(ex.getText()+in);
					exsize++;
					math=ex.getText();
					math=math.substring(0,exsize-1);
					result=pre_calcute();
					if(result==(int)result) 
						ex.setText(ex.getText()+'\n'+(int)result);
					else
						ex.setText(ex.getText()+'\n'+result);
				}
			}
			else if(in.equals("."))
			{
				if(dot_flag==0) {
					if(num_flag==1 || zero_flag==1) {
						ex.setText(ex.getText()+in);
						exsize++;
						num_flag=1;
						dot_flag=1;
						zero_flag=0;
					}
				}
			}
		}
		else {
			if(in.equals("C"))
			{
				ex.setText("");
				finish_flag=0;
				num_flag=0;
				zero_flag=0;
				dot_flag=0;
				nonumber=0;
				bracket_flag=0;
				operation_flag=0;
				exsize=0;
			}
			else if(in.equals("<-"))
			{
				exsize--;
				temp=ex.getText();
				temp=temp.substring(0,exsize);
				ex.setText(temp);
				finish_flag=0;
			}
			else if(in.equals("/") || in.equals("-") || in.equals("+") || in.equals("*")) {
				temp=ex.getText();
				temp=temp.substring(exsize+1,temp.length());
				finish_flag=0;
				exsize=temp.length();
				exsize++;
				ex.setText(temp+in);
				operation_flag=1;
				num_flag=0;
				zero_flag=0;
				bracket_flag=0;
				dot_flag=0;
				nonumber=0;				
			}
		}
	}
	public double pre_calcute() {
		int end=0;
		int start=0;
		double temp=0;
		double r=0;
		while(true) {
			end=math.indexOf(")");
			if(end == -1)
				break;
			start=math.lastIndexOf("(",end);
			temp=final_calcute(math.substring(start+1, end));
			math=math.substring(0,start)+temp+math.substring(end+1,math.length());
		}
		r=final_calcute(math);		
		return r;
	}
	public double final_calcute(String c) {
		double dresult=0;
		double operand=0;
		int i=0;
		int s=0;
		String sub;
		char ope;
		while(true)
		{
			if(i==c.length()||c.charAt(i)=='/' ||c.charAt(i)=='+'||c.charAt(i)=='*'||c.charAt(i)=='-')
				break;
			i++;
		}
		sub=c.substring(0,i);
		dresult=Double.parseDouble(sub);
		while(i<c.length())
		{
			ope=c.charAt(i);
			i++;
			s=i;
			while(true)
			{
				if(i==c.length()|| c.charAt(i)=='/' || c.charAt(i)=='+'||c.charAt(i)=='*'||c.charAt(i)=='-')
					break;
				i++;
			}
			sub=c.substring(s,i);
			operand=Double.parseDouble(sub);
			switch(ope) {
			case '/': 
				if(operand != 0)
					dresult /=operand;
				else {
					JOptionPane.showMessageDialog(null, "You can't divide by 0. The calculation divided by 0 become 0.");
					dresult=0;
				}
			break;
			case '*': dresult *=operand;
			break;
			case '-': dresult -=operand;
			break;
			case '+': dresult +=operand;
			break;
			}
		}
		return dresult;
	}
}

public class s20171666hw3{
	public static void main(String[] args) {
		Calculator c=new Calculator();
	}
}

