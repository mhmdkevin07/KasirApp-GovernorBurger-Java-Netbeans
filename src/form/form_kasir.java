/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import com.sun.glass.events.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.Timer;
/**
 *
 * @author Asus
 */
public class form_kasir extends javax.swing.JPanel {
int x = 0;    

private int harga, qty, total, diskon, hasil_diskon, bayar, kembalian;
private String metode, nama;
public long angka;
DefaultTableModel model;

NumberFormat nf = NumberFormat.getNumberInstance(new Locale("in","ID"));


private void metode_pembayaran(){
   /////METODE PEMBAYARAN
           metode = String.valueOf(cbmetode.getSelectedItem());
           
           if(metode == "Tunai"){
               txtbayar.setEnabled(true);
               txtbayar.setText("");
               txtbayar.grabFocus(); 
           }else if (metode == "Debit"){ 
               bayar = Integer.parseInt(txttagihan.getText().replace(".", ""));
               txtbayar.setText(String.valueOf(nf.format(bayar)));
               txtkembalian.setText("0");
               txtbayar.setEnabled(false);
           }
    
}


public void setTanggal(){
      java.util.Date tgl = new java.util.Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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

public void otomatis(){
        try{
            Connection conn = (Connection)koneksi.configDB();
            Statement stt = conn.createStatement();
            ResultSet rs = stt.executeQuery("Select Max(Right(nopes,4)) as no from transaksi");
            
            
            while (rs.next()) {
                if (rs.first() == false) {
                    txtnopes.setText("NP0001");
                } else {
                    rs.last();
                    int auto_id = rs.getInt(1) + 1;
                    String no = String.valueOf(auto_id);
                    int NomorJual = no.length();
                    //MENGATUR jumlah 0
                    for (int j = 0; j < 4 - NomorJual; j++) {
                        no = "0" + no;
                    }
                    txtnopes.setText("NP" + no);
                }
            }
            rs.close();
            stt.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ERROR: \n" + e.toString(),
                    "Kesalahan", JOptionPane.WARNING_MESSAGE);
        }
       txtkode.grabFocus();
    }


public void kosong_tabel(){
        DefaultTableModel model = (DefaultTableModel) keranjang.getModel();
        
        while (model.getRowCount()>0) {
            model.removeRow(0);
        }
    }

public void hapus_baris_tabel(){
        DefaultTableModel model = (DefaultTableModel) keranjang.getModel();       
        int index = keranjang.getSelectedRow();
        model.removeRow(index);
}

private void jmlTotalHarga() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    int sub_total = 0;
    for(int a = 0; a< keranjang.getRowCount(); a ++){
        sub_total += Integer.parseInt((String) keranjang.getValueAt(a, 5).toString().replace(".", ""));
    }
    
    txttotal.setText(nf.format(sub_total));
    
    txttagihan.setText(nf.format(sub_total));
}

private void bersih() {
   txtkode.setText("");
   txtkode.grabFocus();
   txtnama.setText("");
   txtqty.setText("");
   txtharga.setText("");
}

private void kosongkan_form(){
        //form kasir
        txttotal.setText("");
        txtdiskon.setText("");
        txthasil_diskon.setText("");
        txtbayar.setText("");
        txtkembalian.setText(""); 
        txttagihan.setText("");
    }
    
    /**
     * Creates new form form_menu
     */

    public form_kasir() {
        initComponents();
        kosongkan_form();
        otomatis();
        bayar();
        setTanggal();
        setWaktu();
       
       txtdiskon.setText("0");
       btnprint.setEnabled(false);
       btnbayar.setEnabled(false);
       btnselesai.setEnabled(false);
       btncancel.setEnabled(false);
    }
    
    private void simpan_keranjang(){
                diskon = Integer.parseInt(txtdiskon.getText());
                total = Integer.parseInt(txttagihan.getText().replace(".", ""));
                kembalian = Integer.parseInt(txtkembalian.getText().replace(".", ""));
                bayar = Integer.parseInt(txtbayar.getText().replace(".", ""));
        
                /////HITUNG DISKON     
                    if(diskon <= 0){
                     hasil_diskon = 0;
                    }else if(diskon > 0){
                     hasil_diskon = Integer.parseInt(txthasil_diskon.getText().replace(".", ""));
                    }
           
                try {
                    int baris = keranjang.getRowCount();      
                    for (int i = 0; i < baris; i++) {
                        String sql = "INSERT INTO transaksi_rinci(nopes, tanggal, waktu, kode_menu, nama_menu, harga, qty, amount, total, diskon, metode, bayar, kembalian) VALUES('"
                                + txtnopes.getText() +"','"+ tanggal.getText() +"','"+ waktu.getText() +"','"
                                + keranjang.getValueAt(i, 1) +"','"+ keranjang.getValueAt(i, 2) +"','"+ keranjang.getValueAt(i, 3) 
                                +"','"+ keranjang.getValueAt(i, 4) +"','"+ keranjang.getValueAt(i, 5)+"','"+total+"','"+hasil_diskon+"','"
                                +cbmetode.getSelectedItem()+"','"+bayar+"','"+kembalian+"')" ;
                        java.sql.Connection conn = (Connection)koneksi.configDB();
                        java.sql.PreparedStatement pstm = conn.prepareStatement(sql);
                        pstm.execute();
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
                
}
    
    private void print_tunai(){
        try{
                String path="src/struk/struk_tunai.jasper";
                HashMap parameter = new HashMap();
                java.sql.Connection conn = (Connection)koneksi.configDB();
                parameter.put("nopes", txtnopes.getText());
                JasperPrint print = net.sf.jasperreports.engine.JasperFillManager.fillReport(path, parameter, conn);
                JasperViewer.viewReport(print, false);
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
    }
    
    private void print_debit(){
        try{
                String path="src/struk/struk_debit.jasper";
                HashMap parameter = new HashMap();
                java.sql.Connection conn = (Connection)koneksi.configDB();
                parameter.put("nopes", txtnopes.getText());
                JasperPrint print = net.sf.jasperreports.engine.JasperFillManager.fillReport(path, parameter, conn);
                JasperViewer.viewReport(print, false);
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
    }

    
     private void bayar(){
        txtbayar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                
  
                total = Integer.parseInt(txttagihan.getText().replace(".", ""));
                bayar = Integer.parseInt(txtbayar.getText().replace(".", ""));
         
                int totalKembalian;
                totalKembalian = bayar - total;
                txtkembalian.setText(nf.format(totalKembalian));
                
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        kGradientPanel2 = new keeptoo.KGradientPanel();
        jLabel1 = new javax.swing.JLabel();
        kGradientPanel1 = new keeptoo.KGradientPanel();
        txttagihan = new javax.swing.JLabel();
        txtrp = new javax.swing.JLabel();
        txtqty = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtnopes = new javax.swing.JTextField();
        btncari = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnprint = new javax.swing.JButton();
        txttotal = new javax.swing.JTextField();
        txtkembalian = new javax.swing.JTextField();
        txtbayar = new javax.swing.JTextField();
        txtdiskon = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        btnselesai = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txthasil_diskon = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cbmetode = new javax.swing.JComboBox<>();
        btnbayar = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        keranjang = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        tanggal = new javax.swing.JLabel();
        waktu = new javax.swing.JLabel();
        btncancel = new javax.swing.JButton();
        btnhistory = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        kGradientPanel2.setkEndColor(new java.awt.Color(153, 255, 204));
        kGradientPanel2.setkGradientFocus(3000);
        kGradientPanel2.setkStartColor(new java.awt.Color(255, 255, 153));
        kGradientPanel2.setPreferredSize(new java.awt.Dimension(1200, 45));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("     Kasir Pintar");

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(960, Short.MAX_VALUE))
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        add(kGradientPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        kGradientPanel1.setkEndColor(new java.awt.Color(255, 153, 255));
        kGradientPanel1.setkStartColor(new java.awt.Color(255, 255, 153));

        txttagihan.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txttagihan.setForeground(new java.awt.Color(255, 0, 0));
        txttagihan.setText("0");

        txtrp.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtrp.setForeground(new java.awt.Color(255, 0, 0));
        txtrp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtrp.setText("Rp.");

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(txtrp, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txttagihan, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txttagihan, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(txtrp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        add(kGradientPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(771, 56, -1, -1));
        add(txtkasir, new org.netbeans.lib.awtextra.AbsoluteConstraints(664, 86, 103, 30));

        txtqty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtqtyActionPerformed(evt);
            }
        });
        add(txtqty, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 135, 140, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("QTY");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 116, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("HARGA");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 116, -1, -1));

        txtharga.setEnabled(false);
        add(txtharga, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 136, 140, -1));

        txtnama.setEnabled(false);
        add(txtnama, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 136, 140, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("NAMA MENU");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 116, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("KODE MENU");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 116, -1, -1));

        txtkode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtkodeActionPerformed(evt);
            }
        });
        txtkode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtkodeKeyPressed(evt);
            }
        });
        add(txtkode, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 136, 140, -1));

        txtnopes.setForeground(new java.awt.Color(255, 255, 255));
        txtnopes.setEnabled(false);
        txtnopes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnopesKeyPressed(evt);
            }
        });
        add(txtnopes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 83, 140, -1));

        btncari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cari24.png"))); // NOI18N
        btncari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncariActionPerformed(evt);
            }
        });
        add(btncari, new org.netbeans.lib.awtextra.AbsoluteConstraints(678, 122, 41, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 204, 0)));
        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 150));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("TOTAL TAGIHAN");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("DISKON");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("KEMBALIAN");

        btnprint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/print.png"))); // NOI18N
        btnprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprintActionPerformed(evt);
            }
        });

        txttotal.setEnabled(false);

        txtkembalian.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtkembalian.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtkembalian.setBorder(null);

        txtbayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtbayarActionPerformed(evt);
            }
        });

        txtdiskon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdiskonActionPerformed(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(255, 153, 0));
        jSeparator1.setForeground(new java.awt.Color(255, 153, 0));

        btnselesai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/ceklist.png"))); // NOI18N
        btnselesai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnselesaiActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("PRINT");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("SELESAI");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("%   RP.");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("METODE PEMBAYARAN");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("BAYAR");

        cbmetode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tunai", "Debit" }));
        cbmetode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbmetodeActionPerformed(evt);
            }
        });

        btnbayar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/bayar.png"))); // NOI18N
        btnbayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbayarActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("BAYAR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtdiskon, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel13)
                        .addGap(13, 13, 13)
                        .addComponent(txthasil_diskon, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbmetode, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtbayar, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtkembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbayar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnprint, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnselesai, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtkembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(6, 6, 6)
                                .addComponent(btnprint))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(6, 6, 6)
                                .addComponent(btnselesai))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(6, 6, 6)
                                .addComponent(btnbayar)))
                        .addGap(0, 0, 0)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtdiskon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txthasil_diskon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(cbmetode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(txtbayar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(13, 13, 13))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 407, 1100, 190));

        keranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Kode Menu", "Nama Menu", "Harga", "Qty", "Amount"
            }
        ));
        keranjang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                keranjangMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(keranjang);
        if (keranjang.getColumnModel().getColumnCount() > 0) {
            keranjang.getColumnModel().getColumn(5).setResizable(false);
        }

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 910, 210));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setText("NO. PESANAN");
        add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 63, 80, -1));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setText("TANGGAL TRANSAKSI  ");
        add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 140, 20));

        tanggal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tanggal.setText("Tanggal");
        add(tanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 130, 20));

        waktu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        waktu.setText("Jam");
        add(waktu, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, 120, 20));

        btncancel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btncancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cancel24.png"))); // NOI18N
        btncancel.setText("Cancel");
        btncancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelActionPerformed(evt);
            }
        });
        add(btncancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 180, 150, -1));

        btnhistory.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnhistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/history.png"))); // NOI18N
        btnhistory.setText("History");
        btnhistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhistoryActionPerformed(evt);
            }
        });
        add(btnhistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 240, 150, 35));
    }// </editor-fold>//GEN-END:initComponents
    private void tampilkodemenu(String key) {
        
        try{
        Connection conn=(Connection)koneksi.configDB();
        Statement stt=conn.createStatement();
        ResultSet rs;
        rs=stt.executeQuery("SELECT * from menu WHERE kode_menu LIKE '%"+key+"%'");  
        if(rs.next()){
       
        String nama = rs.getString("nama_menu");
        String harga = rs.getString("harga");
        txtnama.setText(nama);
        txtharga.setText(harga);
        
            }
            else{
                JOptionPane.showMessageDialog(null, "Data Tidak Ada");
            }
        }catch (Exception ex) {
        System.err.println(ex.getMessage());
        }
    }
    
    private void txtkodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtkodeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            
        String key=txtkode.getText();
        System.out.println(key);  
        
        if(key!=""){
           tampilkodemenu(key);
        }else{
            JOptionPane.showMessageDialog(null, "Data Tidak Ada");
        }
        }
       
    }//GEN-LAST:event_txtkodeKeyPressed

    private void txtqtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtqtyActionPerformed
        
        qty = Integer.parseInt(txtqty.getText());
        harga = Integer.parseInt(txtharga.getText());
        total = qty * harga;
        
       if(txtqty.getText().equals("")){
           return;
           
       }else{
           DefaultTableModel model = (DefaultTableModel) keranjang.getModel();
           
           Object obj [] = new Object[6];
           obj [1] = txtkode.getText();
           obj [2] = txtnama.getText();
           obj [3] = txtharga.getText();
           obj [4] = txtqty.getText();
           obj [5] = total;
           
           model.addRow(obj);
           
           int baris = model.getRowCount();
           
           for(int a = 0; a < baris; a ++){
               String no = String.valueOf(a + 1);
               model.setValueAt(no + ".", a, 0);
           }
            keranjang.setRowHeight(30);
            jmlTotalHarga();
            bersih();
       }
   
  btnbayar.setEnabled(true);
   
   
    }//GEN-LAST:event_txtqtyActionPerformed

    private void txtnopesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnopesKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnopesKeyPressed

    private void btncariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncariActionPerformed
          new cari().setVisible(true);
    }//GEN-LAST:event_btncariActionPerformed

    private void txtdiskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdiskonActionPerformed
                
                diskon = Integer.parseInt(txttotal.getText().replace(".", ""))* Integer.parseInt(txtdiskon.getText().replace(".", ""))/100;
                hasil_diskon = Integer.parseInt(txttotal.getText().replace(".", "")) - diskon;
                
                txthasil_diskon.setText(nf.format(diskon));
                txttagihan.setText(nf.format(hasil_diskon)); 
                
                cbmetode.grabFocus();
    }//GEN-LAST:event_txtdiskonActionPerformed

    private void txtbayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtbayarActionPerformed
        //total = Integer.parseInt(txttagihan.getText());
        //bayar = Integer.parseInt(txtbayar.getText());
        //kembalian = bayar - total;
        
        //txtkembalian.setText(String.valueOf(kembalian));
    }//GEN-LAST:event_txtbayarActionPerformed

    private void btnselesaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnselesaiActionPerformed
           otomatis();     
           setTanggal();    
           txtkode.grabFocus();
           txtdiskon.setText("0");
           txtbayar.setEnabled(true);
           btnbayar.setEnabled(false);
           btnprint.setEnabled(false);
           btnselesai.setEnabled(false);
    }//GEN-LAST:event_btnselesaiActionPerformed

    private void txtkodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtkodeActionPerformed
        // TODO add your handling code here:
        txtqty.grabFocus();
    }//GEN-LAST:event_txtkodeActionPerformed

    private void btnprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprintActionPerformed
    metode = String.valueOf(cbmetode.getSelectedItem()); 
    
    if(metode == "Tunai"){
        print_tunai();
    }else if(metode == "Debit"){
        print_debit();
    }
        btnselesai.setEnabled(true);
        
    }//GEN-LAST:event_btnprintActionPerformed

    private void cbmetodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbmetodeActionPerformed
        metode_pembayaran();
        
    }//GEN-LAST:event_cbmetodeActionPerformed

    private void btnbayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbayarActionPerformed
      
           simpan_keranjang();                
           if (kembalian < 0){
               JOptionPane.showMessageDialog(null, "Maaf Uang Kurang");
           }else if(kembalian >= 0) {     
                try {
                        String sql = "INSERT INTO transaksi(nopes, tanggal, waktu, total, diskon, metode, bayar, kembalian) VALUES('"
                                + txtnopes.getText() +"','"+ tanggal.getText() +"','"+ waktu.getText() +"','"+ total 
                                +"','"+ hasil_diskon +"','"+ cbmetode.getSelectedItem()+"','"+ bayar +"','"+ kembalian + "')" ;
                        java.sql.Connection conn = (Connection)koneksi.configDB();
                        java.sql.PreparedStatement pstm = conn.prepareStatement(sql);
                        pstm.execute();
                    JOptionPane.showMessageDialog(null, "Pembayaran Berhasil...");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
           }
        btnprint.setEnabled(true);
        btnselesai.setEnabled(true);
        kosong_tabel();
        kosongkan_form();
        
    }//GEN-LAST:event_btnbayarActionPerformed

    private void btncancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelActionPerformed
        hapus_baris_tabel();
        bersih();
        btncancel.setEnabled(false);
        jmlTotalHarga();
       
    }//GEN-LAST:event_btncancelActionPerformed

    private void keranjangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_keranjangMouseClicked
        
        int baris = keranjang.rowAtPoint(evt.getPoint());
        String kode = keranjang.getValueAt(baris, 1).toString();
        txtkode.setText(kode);

        String nama = keranjang.getValueAt(baris, 2).toString();
        txtnama.setText(nama);

        String qty = keranjang.getValueAt(baris, 4).toString();
        txtqty.setText(qty);

        String harga = keranjang.getValueAt(baris, 3).toString();
        txtharga.setText(harga);

btncancel.setEnabled(true);
    }//GEN-LAST:event_keranjangMouseClicked

    private void btnhistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhistoryActionPerformed
        new history().setVisible(true);
    }//GEN-LAST:event_btnhistoryActionPerformed
        
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnbayar;
    private javax.swing.JButton btncancel;
    private javax.swing.JButton btncari;
    private javax.swing.JButton btnhistory;
    private javax.swing.JButton btnprint;
    private javax.swing.JButton btnselesai;
    private javax.swing.JComboBox<String> cbmetode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private keeptoo.KGradientPanel kGradientPanel1;
    private keeptoo.KGradientPanel kGradientPanel2;
    private javax.swing.JTable keranjang;
    private javax.swing.JLabel tanggal;
    private javax.swing.JTextField txtbayar;
    private javax.swing.JTextField txtdiskon;
    public static final javax.swing.JTextField txtharga = new javax.swing.JTextField();
    private javax.swing.JTextField txthasil_diskon;
    public static final javax.swing.JLabel txtkasir = new javax.swing.JLabel();
    private javax.swing.JTextField txtkembalian;
    public static final javax.swing.JTextField txtkode = new javax.swing.JTextField();
    public static final javax.swing.JTextField txtnama = new javax.swing.JTextField();
    private javax.swing.JTextField txtnopes;
    private javax.swing.JTextField txtqty;
    private javax.swing.JLabel txtrp;
    private javax.swing.JLabel txttagihan;
    private javax.swing.JTextField txttotal;
    private javax.swing.JLabel waktu;
    // End of variables declaration//GEN-END:variables

  
    
    

  

   
}
