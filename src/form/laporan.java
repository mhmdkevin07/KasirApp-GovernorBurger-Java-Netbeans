/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import form.koneksi;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import report.koneksilaporan;
import report.koneksilaporan;

/**
 *
 * @author Asus
 */
public class laporan extends javax.swing.JPanel {
private String laporan;

private void laporan_menu(){
        try {
        String file = "/report/report_menu.jasper";
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        HashMap param= new HashMap();
        java.sql.Connection conn = (Connection)koneksi.configDB();
        
        JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream(file), param,conn);
        JasperViewer.viewReport(print,false);
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
}

private void laporan_supplier(){
        try {
        String file = "/report/report_supplier.jasper";
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        HashMap param= new HashMap();
        java.sql.Connection conn = (Connection)koneksi.configDB();
        
        JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream(file), param,conn);
        JasperViewer.viewReport(print,false);
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
}

private void laporan_stok(){
        try {
        String file = "/report/report_stok.jasper";
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        HashMap param= new HashMap();
        java.sql.Connection conn = (Connection)koneksi.configDB();
        
        JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream(file), param,conn);
        JasperViewer.viewReport(print,false);
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
}

private void laporan_pengeluaran(){
   
    try {
                java.sql.Connection conn = (Connection)koneksi.configDB();
                HashMap parameter = new HashMap();
                String file = "/report/report_pengeluaran.jasper";
                
                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
                String tgl1 = String.valueOf(fm.format(tanggal1.getDate()));
                Date tanggal1 = (Date) fm.parse(tgl1);
                
                String tgl2 = String.valueOf(fm.format(tanggal2.getDate()));
                Date tanggal2 = (Date) fm.parse(tgl2);
                
                parameter.put("tanggal1",tanggal1);
                parameter.put("tanggal2",tanggal2);
                
                JasperPrint jasperPrint =JasperFillManager.fillReport(getClass().getResourceAsStream(file), parameter ,conn);
                JasperViewer.viewReport(jasperPrint, false);
                
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,e.getMessage());

            }  
}

private void laporan_transaksi(){
   
    try {
                java.sql.Connection conn = (Connection)koneksi.configDB();
                HashMap parameter = new HashMap();
                String file = "/report/report_transaksi.jasper";
                
                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
                String tgl1 = String.valueOf(fm.format(tanggal1.getDate()));
                
                String tgl2 = String.valueOf(fm.format(tanggal2.getDate()));
                
                parameter.put("tanggal1",tgl1);
                parameter.put("tanggal2",tgl2);
                
                JasperPrint jasperPrint =JasperFillManager.fillReport(getClass().getResourceAsStream(file), parameter ,conn);
                JasperViewer.viewReport(jasperPrint, false);
                
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,e.getMessage());

            }  
}


    /**
     * Creates new form laporan
     */
    public laporan() {
        initComponents();
        tampil_menu();
        btnlaporan.setEnabled(false);
        tanggal1.setEnabled(false);
        tanggal2.setEnabled(false);
    }

    private void tampil_menu(){
          
           DefaultTableModel model = new DefaultTableModel();
           model.addColumn("No.");
           model.addColumn("KODE ");
           model.addColumn("MENU ");
           model.addColumn("KATEGORI ");
           model.addColumn("HARGA");
                   
           try{
            int no = 1;
            String sql = "SELECT * FROM menu";
            java.sql.Connection conn = (Connection)koneksi.configDB();
            java.sql.Statement stm = conn.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            
            while(res.next()){
                model.addRow(new Object[]{no++,res.getString(1),res.getString(2),res.getString(3),res.getString(4)}); 
            
            tabellaporan.setModel(model);
            }
                 
           }catch (SQLException e){
               System.out.println("Error :"+ e.getMessage());
           }
       }
    
    private void tampil_supplier(){
          
           DefaultTableModel model = new DefaultTableModel();
           model.addColumn("No.");
           model.addColumn("NAMA ");
           model.addColumn("TELEPHON ");
           model.addColumn("JENIS BARANG ");
           model.addColumn("ALAMAT");
           
        
           try{
            int no = 1;
            String sql = "SELECT * FROM supplier";
            java.sql.Connection conn = (Connection)koneksi.configDB();
            java.sql.Statement stm = conn.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            
            while(res.next()){
                model.addRow(new Object[]{no++,res.getString(1),res.getString(2),res.getString(3),res.getString(4)}); 
            
            tabellaporan.setModel(model);
            }
                 
           }catch (SQLException e){
               System.out.println("Error :"+ e.getMessage());
           }
       }
    
     private void tampil_stok(){
          
           DefaultTableModel model = new DefaultTableModel();
           model.addColumn("No.");
           model.addColumn("KODE ");
           model.addColumn("NAMA ");
           model.addColumn("JENIS BARANG ");
           model.addColumn("QTY ");
           model.addColumn("SATUAN");
           
        
           try{
            int no = 1;
            String sql = "SELECT * FROM stok";
            java.sql.Connection conn = (Connection)koneksi.configDB();
            java.sql.Statement stm = conn.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            
            while(res.next()){
                model.addRow(new Object[]{no++,res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5)}); 
            
            tabellaporan.setModel(model);
            }
                 
           }catch (SQLException e){
               System.out.println("Error :"+ e.getMessage());
           }
       }
     
     private void tampil_pengeluaran(){
          
           DefaultTableModel model = new DefaultTableModel();
           model.addColumn("No.");
           model.addColumn("TGL BELI ");
           model.addColumn("KODE INPUT ");
           model.addColumn("SUPPLIER ");
           model.addColumn("JENIS");
           model.addColumn("TOTAL PENGELUARAN");       
           try{
            int no = 1;
            String sql = "SELECT * FROM pengeluaran";
            java.sql.Connection conn = (Connection)koneksi.configDB();
            java.sql.Statement stm = conn.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            
            while(res.next()){
                model.addRow(new Object[]{no++,res.getString(2),res.getString(1),res.getString(3),res.getString(4),res.getString(5)}); 
            
            tabellaporan.setModel(model);
            }
                 
           }catch (SQLException e){
               System.out.println("Error :"+ e.getMessage());
           }
       }
     
     private void tampil_transaksi(){
          
           DefaultTableModel model = new DefaultTableModel();
           model.addColumn("No.");
           model.addColumn("Nomor Pesanan ");
           model.addColumn("Tanggal ");
           model.addColumn("Total ");
           model.addColumn("Diskon");
           model.addColumn("Metode ");
           model.addColumn("Bayar ");
           model.addColumn("Kembalian");        
           try{
            int no = 1;
            String sql = "SELECT * FROM transaksi";
            java.sql.Connection conn = (Connection)koneksi.configDB();
            java.sql.Statement stm = conn.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            
            while(res.next()){
                model.addRow(new Object[]{no++,res.getString(1),res.getString(2),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8)}); 
            
            tabellaporan.setModel(model);
            }
                 
           }catch (SQLException e){
               System.out.println("Error :"+ e.getMessage());
           }
       }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabellaporan = new javax.swing.JTable();
        cblaporan = new javax.swing.JComboBox<>();
        tanggal2 = new com.toedter.calendar.JDateChooser();
        tanggal1 = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        btnlaporan = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        jLabel2.setText("s.d");
        add(jLabel2);
        jLabel2.setBounds(580, 100, 30, 27);

        jLabel3.setFont(new java.awt.Font("Tw Cen MT", 1, 24)); // NOI18N
        jLabel3.setText("Pilih Data Terlebih Dahulu :");
        add(jLabel3);
        jLabel3.setBounds(60, 50, 310, 27);

        tabellaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabellaporan);

        add(jScrollPane1);
        jScrollPane1.setBounds(60, 160, 1010, 410);

        cblaporan.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        cblaporan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laporan Data Menu", "Laporan Data Supplier", "Laporan Stok", "Laporan Pengeluaran", "Laporan Transaksi" }));
        cblaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cblaporanActionPerformed(evt);
            }
        });
        add(cblaporan);
        cblaporan.setBounds(370, 50, 190, 30);

        tanggal2.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tanggal2AncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tanggal2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tanggal2MouseClicked(evt);
            }
        });
        add(tanggal2);
        tanggal2.setBounds(620, 100, 190, 30);
        add(tanggal1);
        tanggal1.setBounds(370, 100, 190, 30);

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        jLabel4.setText("Pilih Tanggal Periode Laporan :");
        add(jLabel4);
        jLabel4.setBounds(90, 100, 260, 27);

        btnlaporan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnlaporan.setText("Cetak Laporan");
        btnlaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlaporanActionPerformed(evt);
            }
        });
        add(btnlaporan);
        btnlaporan.setBounds(840, 100, 130, 30);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/background.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1200, 800);
    }// </editor-fold>//GEN-END:initComponents

    private void cblaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cblaporanActionPerformed
        laporan = String.valueOf(cblaporan.getSelectedItem());
        
        if(laporan == "Laporan Data Menu"){
            tampil_menu();
            tanggal1.setEnabled(false);
            tanggal2.setEnabled(false);
            tanggal1.setDate(null);
            tanggal2.setDate(null);
        }else if(laporan == "Laporan Data Supplier"){
            tampil_supplier();
            tanggal1.setEnabled(false);
            tanggal2.setEnabled(false);
            tanggal1.setDate(null);
            tanggal2.setDate(null);
        }else if(laporan == "Laporan Stok"){
            tampil_stok();
            tanggal1.setEnabled(false);
            tanggal2.setEnabled(false);
            tanggal1.setDate(null);
            tanggal2.setDate(null);
        }else if(laporan == "Laporan Pengeluaran"){
            tampil_pengeluaran();
            tanggal1.setEnabled(true);
            tanggal2.setEnabled(true);
            tanggal1.setDate(null);
            tanggal2.setDate(null);
        }else if(laporan == "Laporan Transaksi"){
            tampil_transaksi();
            tanggal1.setEnabled(true);
            tanggal2.setEnabled(true);
            tanggal1.setDate(null);
            tanggal2.setDate(null);
        }
        btnlaporan.setEnabled(true);
    }//GEN-LAST:event_cblaporanActionPerformed

    private void tanggal2AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tanggal2AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tanggal2AncestorAdded

    private void tanggal2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tanggal2MouseClicked
      
    }//GEN-LAST:event_tanggal2MouseClicked

    private void btnlaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlaporanActionPerformed
        laporan = String.valueOf(cblaporan.getSelectedItem());
        if(laporan == "Laporan Data Menu"){
            laporan_menu();
        }else if(laporan == "Laporan Data Supplier"){
            laporan_supplier();
        }else if(laporan == "Laporan Stok"){
            laporan_stok();
        }else if(laporan == "Laporan Pengeluaran"){
            laporan_pengeluaran();
        }else if(laporan == "Laporan Transaksi"){
            laporan_transaksi();
        }
    }//GEN-LAST:event_btnlaporanActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnlaporan;
    private javax.swing.JComboBox<String> cblaporan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabellaporan;
    private com.toedter.calendar.JDateChooser tanggal1;
    private com.toedter.calendar.JDateChooser tanggal2;
    // End of variables declaration//GEN-END:variables
}
