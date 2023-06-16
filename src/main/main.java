/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import form.beranda;
import form.form_kasir;
import form.form_menu;
import form.form_pengeluaran;
import form.form_persediaan_barang;
import form.form_supplier;
import form.form_user;
import form.laporan;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import menu.MenuItem;

/**
 *
 * @author Asus
 */
public class main extends javax.swing.JFrame {

    public void setTanggal(){
      java.util.Date tgl = new java.util.Date();
        SimpleDateFormat format = new SimpleDateFormat("EEEE, dd - MM - yyyy",new Locale("in","ID"));
        tanggal.setText(format.format(tgl));
    }

public void setWaktu(){
        ActionListener taskPerformer = new ActionListener() {
 
        @Override
            public void actionPerformed(ActionEvent evt) {
            String nol_jam = "", nol_menit = "",nol_detik = "";
 
            java.util.Date dateTime = new java.util.Date();
            int nilai_jam = dateTime.getHours();
            int nilai_menit = dateTime.getMinutes();
            int nilai_detik = dateTime.getSeconds();
 
            if(nilai_jam <= 9) nol_jam= "0";
            if(nilai_menit <= 9) nol_menit= "0";
            if(nilai_detik <= 9) nol_detik= "0";
 
            String jam = nol_jam + Integer.toString(nilai_jam);
            String menit = nol_menit + Integer.toString(nilai_menit);
            String detik = nol_detik + Integer.toString(nilai_detik);
 
            waktu.setText(jam+":"+menit+":"+detik+"");
            }
        };
    new Timer(1000, taskPerformer).start();
    } 
    
    /**
     * Creates new form main
     */
    public main() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        excute();
        setTanggal();
        setWaktu();
    }
    private void excute(){
        //create icon
        ImageIcon iconMaster= new ImageIcon(getClass().getResource("/icon/folder.png"));
        ImageIcon iconKasir= new ImageIcon(getClass().getResource("/icon/monitor.png"));
        ImageIcon iconStok= new ImageIcon(getClass().getResource("/icon/box.png"));
        ImageIcon iconLaporan= new ImageIcon(getClass().getResource("/icon/laporan.png"));
        ImageIcon iconSubMenu= new ImageIcon(getClass().getResource("/icon/next15.png"));
        ImageIcon iconSetting= new ImageIcon(getClass().getResource("/icon/setting.png"));
        
        //create submenu
        MenuItem menuMenu=new MenuItem(iconSubMenu,"Data Menu",new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
           panelbody.removeAll();
           panelbody.add(new form_menu());
           panelbody.repaint();
           panelbody.revalidate();
            }
        });
        
        MenuItem menuSupplier=new MenuItem(iconSubMenu,"Data Supplier",new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
           panelbody.removeAll();
           panelbody.add(new form_supplier());
           panelbody.repaint();
           panelbody.revalidate();
            }      
        });
        
        MenuItem menuUser=new MenuItem(iconSubMenu,"Tambah Akun",new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
           panelbody.removeAll();
           panelbody.add(new form_user());
           panelbody.repaint();
           panelbody.revalidate();
            }      
        });
        
        MenuItem menuBarangBaru=new MenuItem(iconSubMenu,"Persediaan Bahan Baku",new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
           panelbody.removeAll();
           panelbody.add(new form_persediaan_barang());
           panelbody.repaint();
           panelbody.revalidate();
            }      
        });
        
        MenuItem menuStokMasuk=new MenuItem(iconSubMenu,"Pembelian Bahan Baku",new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
           panelbody.removeAll();
           panelbody.add(new form_pengeluaran());
           panelbody.repaint();
           panelbody.revalidate();
            }      
        });
        
        MenuItem menuKeluar=new MenuItem(iconSubMenu,"Keluar",new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            try{
            int reply = JOptionPane.showConfirmDialog(null,
                    "Apakah Anda Yakin Ingin Keluar?",
                    "Konfirmasi",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
                    );
            if (reply==JOptionPane.YES_OPTION){
                new login().setVisible(true);
            dispose();
            }
        }
        catch(Exception DBException){
            System.err.println("Error:" +DBException);
        }
            
            }      
        });
        
       
        

        //Menu Utama
        MenuItem menuStok= new MenuItem(iconStok,"BAHAN BAKU",null,menuBarangBaru,menuStokMasuk);
        
        MenuItem menuMaster= new MenuItem(iconMaster,"MASTER",null,menuMenu, menuSupplier, menuStok); 
        
        MenuItem menuSetting= new MenuItem (iconSetting,"Setting",null,menuUser, menuKeluar );
        
        MenuItem menuKasir= new MenuItem(iconKasir,"KASIR",new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            panelbody.removeAll();
            panelbody.add(new form_kasir());
            panelbody.repaint();
            panelbody.revalidate();
            }
        });
        
        MenuItem menuLaporan= new MenuItem(iconLaporan,"Laporan",new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            panelbody.removeAll();
            panelbody.add(new laporan());
            panelbody.repaint();
            panelbody.revalidate();
            }      
        });            
        
        addMenu(menuMaster, menuKasir,menuSetting, menuLaporan);
    }
    
    private void addMenu(MenuItem... menu){
        for(int i=0;i<menu.length;i++){
            menus.add(menu[i]);
            ArrayList<MenuItem>subMenu=menu[i].getSubMenu();
            for(MenuItem m:subMenu){
                addMenu(m);
            }
        
    }
        menus.repaint();
        menus.revalidate();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelheader = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        waktu = new javax.swing.JLabel();
        tanggal = new javax.swing.JLabel();
        panelmenu = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        menus = new keeptoo.KGradientPanel();
        panelbody = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelheader.setBackground(new java.awt.Color(255, 153, 51));
        panelheader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        panelheader.setMinimumSize(new java.awt.Dimension(100, 70));
        panelheader.setPreferredSize(new java.awt.Dimension(988, 75));

        jLabel1.setFont(new java.awt.Font("Bookman Old Style", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("GOVERNOR BURGER");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/logo.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        waktu.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        waktu.setForeground(new java.awt.Color(255, 255, 255));
        waktu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        tanggal.setBackground(new java.awt.Color(255, 255, 255));
        tanggal.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        tanggal.setForeground(new java.awt.Color(255, 255, 255));
        tanggal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tanggal.setText("jLabel3");

        javax.swing.GroupLayout panelheaderLayout = new javax.swing.GroupLayout(panelheader);
        panelheader.setLayout(panelheaderLayout);
        panelheaderLayout.setHorizontalGroup(
            panelheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelheaderLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(panelheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(waktu, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 675, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addGap(22, 22, 22))
        );
        panelheaderLayout.setVerticalGroup(
            panelheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelheaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(waktu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(tanggal)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelheaderLayout.createSequentialGroup()
                .addGroup(panelheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelheaderLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jLabel1.getAccessibleContext().setAccessibleName("G O V E R N O R B U R G E R");

        getContentPane().add(panelheader, java.awt.BorderLayout.PAGE_START);

        panelmenu.setBackground(new java.awt.Color(255, 255, 255));
        panelmenu.setMinimumSize(new java.awt.Dimension(250, 100));
        panelmenu.setPreferredSize(new java.awt.Dimension(250, 100));
        panelmenu.setLayout(new java.awt.BorderLayout());

        menus.setkEndColor(new java.awt.Color(255, 153, 0));
        menus.setkGradientFocus(600);
        menus.setkStartColor(new java.awt.Color(255, 204, 204));
        menus.setLayout(new javax.swing.BoxLayout(menus, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(menus);

        panelmenu.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(panelmenu, java.awt.BorderLayout.LINE_START);

        panelbody.setBackground(new java.awt.Color(255, 255, 255));
        panelbody.setLayout(new java.awt.CardLayout());

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/background.png"))); // NOI18N
        jLabel3.setText("jLabel3");
        panelbody.add(jLabel3, "card2");

        getContentPane().add(panelbody, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(1340, 547));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        panelbody.removeAll();
        panelbody.add(new beranda());
        panelbody.repaint();
        panelbody.revalidate();
    }//GEN-LAST:event_jLabel2MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private keeptoo.KGradientPanel menus;
    private javax.swing.JPanel panelbody;
    private javax.swing.JPanel panelheader;
    private javax.swing.JPanel panelmenu;
    private javax.swing.JLabel tanggal;
    private javax.swing.JLabel waktu;
    // End of variables declaration//GEN-END:variables
}
