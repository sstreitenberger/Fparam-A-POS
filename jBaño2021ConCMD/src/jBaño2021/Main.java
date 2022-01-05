package jBaño2021;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main extends JFrame {
  private static final long serialVersionUID = 2824330405822902835L;
  
  private JButton jButton1;
  
  private JButton jButton2;
  
  private JLabel jLabel1;
  
  private JTextField jTextField1;
  
  public Main() {
    initComponents();
  }
  
  private void initComponents() {
    this.jTextField1 = new JTextField();
    this.jButton1 = new JButton();
    this.jLabel1 = new JLabel();
    this.jButton2 = new JButton();
    setDefaultCloseOperation(3);
    setLocationByPlatform(true);
    this.jButton1.setText("Guardar");
    this.jButton1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            try {
				Main.this.jButton1ActionPerformed(evt);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          }
        });
    this.jLabel1.setText("JBaño2021");
    this.jButton2.setText("Limpiar");
    this.jButton2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            Main.this.jButton2ActionPerformed(evt);
          }
        });
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
          .createSequentialGroup().addContainerGap()
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(this.jLabel1, -1, 304, 32767)
            .addComponent(this.jTextField1).addGroup(GroupLayout.Alignment.TRAILING, 
              layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.jButton2)
              .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
              .addComponent(this.jButton1)))
          .addContainerGap()));
    layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup().addContainerGap()
          .addComponent(this.jLabel1, -1, 
            -1, 32767)
          .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
          .addComponent(this.jTextField1, -2, 
            -1, -2)
          .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
            .addComponent(this.jButton1).addComponent(this.jButton2))
          .addContainerGap()));
    pack();
  }
  
  private void jButton1ActionPerformed(ActionEvent evt) throws IOException, InterruptedException {
	  try {
	      String codigo = this.jTextField1.getText();
	      System.out.println(codigo);
	      if (replace(codigo))
	        infoBox("Codigo modificado con exito.", 1); 
	    } catch (Exception ex) {
	      infoBox(ex.getMessage(), 0);
	    } 
 
    
  }
  
  public boolean replace(String codigo) throws Exception {
    String oldFileName = "D:\\MICROS\\Res\\CAL\\Win32\\Files\\Micros\\Res\\Pos\\Etc\\FIPParam.cfg";
    String tmpFileName = "D:\\MICROS\\Res\\CAL\\Win32\\Files\\Micros\\Res\\Pos\\Etc\\tmp_FIPParam.cfg";
	//String oldFileName = "C:\\fipparam\\FIPParam.cfg";
	//String tmpFileName = "C:\\fipparam\\tmp_FIPParam.cfg";
	  
    File oldFile = new File(oldFileName);
    File newFile = new File(tmpFileName);
    if (!newFile.exists())
      newFile.createNewFile(); 
    BufferedReader br = null;
    BufferedWriter bw = null;
    br = new BufferedReader(new FileReader(oldFileName));
    bw = new BufferedWriter(new FileWriter(tmpFileName));
    String line;
    while ((line = br.readLine()) != null) {
      if (line.toUpperCase().startsWith("TRAILER_3".toUpperCase()))
      {
    	 System.out.println(line);
        line = "TRAILER_3=1^    CodigoBaño: " + codigo.toUpperCase(); 
      }
      bw.write(String.valueOf(line) + System.lineSeparator());
    } 
    br.close();
    bw.close();
    
    oldFile.delete();
    newFile.renameTo(oldFile);
    
    
   //--------------------------------------------------------- 
    InetAddress ipLocal = InetAddress.getLocalHost();
    String ip = ipLocal.toString();
    String[] ipaux = ip.split("/");
    String ipaux2 = ipaux[1];
    String[] IPoctetos = ipaux2.split("\\.");
    
   
    List<String> ListaIPS=new ArrayList<String>();  
    for(int i= 0; i < 14; i++)
    {
    	ListaIPS.add(""+IPoctetos[0]+"." + IPoctetos[1] +"."+ IPoctetos[2]+"."+(i+1));
    }
    
    System.out.println("Comprobando Conexion al los POS:");
    List<String> ListaIPSconAlcance =new ArrayList<String>();
    for(int k= 0; k < ListaIPS.size(); k++){
    	try {
    	    String cmd = "cmd /C ping -n 1 " + ListaIPS.get(k) + " | find \"TTL\"";        
    	    Process myProcess = Runtime.getRuntime().exec(cmd);
    	    myProcess.waitFor();

    	    if(myProcess.exitValue() == 0) {
    	    ListaIPSconAlcance.add(ListaIPS.get(k));
    		  System.out.println("El POS "+ListaIPS.get(k)+" esta ACTIVO");
    	   
    	    }
    	    else {
    	    	System.out.println("El POS "+ListaIPS.get(k)+" esta INACTIVO");
    	    }
    	}
    	catch (Exception e) {
    		System.out.println("Ocurrio un error al Conectar al POS "+ListaIPS.get(k)+"");	
    	}
    }
    
    
    String[] command =
        {
            "cmd",
        };
        Process p = Runtime.getRuntime().exec(command);
        new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
        new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
        PrintWriter stdin = new PrintWriter(p.getOutputStream());
        
        //stdin.println("copy C:\\fipparam\\FIPParam.cfg C:\\fipparam2\\ /Y");
        //stdin.println("copy C:\\fipparam\\FIPParam.cfg C:\\fipparam2\\ /Y");
        
        
        //stdin.println("net use \\\\172.31.1.78\\c$ micros /user:administrador");
		//stdin.println("copy D:\\MICROS\\Res\\CAL\\Win32\\Files\\Micros\\Res\\Pos\\Etc\\FIPParam.cfg \\\\172.31.1.78\\C$\\MICROS\\Res\\Pos\\Etc /Y");
        
        
       for(int j= 0; j < ListaIPSconAlcance.size(); j++)
        {
        	stdin.println("net use \\\\"+ListaIPSconAlcance.get(j)+"\\c$ micros /user:administrator");
    		stdin.println("copy D:\\MICROS\\Res\\CAL\\Win32\\Files\\Micros\\Res\\Pos\\Etc\\FIPParam.cfg \\\\"+ListaIPSconAlcance.get(j)+"\\C$\\MICROS\\Res\\Pos\\Etc /Y");  	
    		//"cmd.exe /c copy D:\\MICROS\\Res\\CAL\\Win32\\Files\\Micros\\Res\\Pos\\Etc\\FIPParam.cfg \\"+ListaIPS.get(j)+"\\C$\\MICROS\\Res\\Pos\\Etc /Y");  	
        }
 
        
        stdin.close();
        int returnCode = p.waitFor();
        System.out.println("Return code = " + returnCode +"");   
        if (returnCode == 0)
        	System.out.println("El codigo ha sido modificado en todos los POS activos, puede cerrar el programa.");   
        else 
        	System.out.println("Ha ocurrido un error al modicaar en codigo en los POS, por favor contacte a Soporte.");   
    
    
    //------------------------------------------------------------------------------------------
    
    
    return true;
  }
  
  public boolean copyTo(File src, File dest) throws IOException, InterruptedException {
    InputStream in = null;
    OutputStream out = null;
    in = new FileInputStream(src);
    out = new FileOutputStream(dest);
    byte[] buffer = new byte[1024];
    int length;
    while ((length = in.read(buffer)) > 0)
      out.write(buffer, 0, length); 
    in.close();
    out.close();
    
    
   
        
    return true;
  }
  
  public static void infoBox(String infoMessage, int tipo) {
    JOptionPane.showMessageDialog(null, infoMessage, "JBano", tipo);
  }
  
  private void jButton2ActionPerformed(ActionEvent evt) {
    this.jTextField1.setText("");
  }
  
  public static void main(String[] args) {
    try {
      byte b;
      int i;
      UIManager.LookAndFeelInfo[] arrayOfLookAndFeelInfo;
      for (i = (arrayOfLookAndFeelInfo = UIManager.getInstalledLookAndFeels()).length, b = 0; b < i; ) {
        UIManager.LookAndFeelInfo info = arrayOfLookAndFeelInfo[b];
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        } 
        b++;
      } 
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, (String)null, ex);
    } catch (InstantiationException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, (String)null, ex);
    } catch (IllegalAccessException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, (String)null, ex);
    } catch (UnsupportedLookAndFeelException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    EventQueue.invokeLater(new Runnable() {
          public void run() {
            Main x = new Main();
            x.setLocationRelativeTo(null);
            x.setVisible(true);
          }
        });
  }
}
