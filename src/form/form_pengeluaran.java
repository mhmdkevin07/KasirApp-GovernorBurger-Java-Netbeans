/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Asus
 */
public class form_pengeluaran extends javax.swing.JPanel {
    
    private int qty, harga,total;
    
    ResultSet rssupplier = null; 
    ResultSet rsstok = null; 
    NumberFormat nf = NumberFormat.getNumberInstance(new Locale("in","ID"));
    
public void otomatis(){
        
        try{
            Connection conn = (Connection)koneksi.configDB();
            Statement stt = conn.createStatement();
            ResultSet rs = stt.executeQuery("Select Max(Right(kode_input,4)) as no from pengeluaran");
            
            
            while (rs.next()) {
                if (rs.first() == false) {
                    txtkode_input.setText("P-0001");
                } else {
                    rs.last();
                    int auto_id = rs.getInt(1) + 1;
                    String no = String.valueOf(auto_id);
                    int NomorJual = no.length();
                    //MENGATUR jumlah 0
                    for (int j = 0; j < 4 - NomorJual; j++) {
                        no = "0" + no;
                    }
                    txtkode_input.setText("P-" + no);
                }
            }
            rs.close();
            stt.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ERROR: \n" + e.toString(),
                    "Kesalahan", JOptionPane.WARNING_MESSAGE);
        }
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

private void simpan_keranjang(){
                try {
                    int baris = keranjang.getRowCount();      
                    for (int i = 0; i < baris; i++) {
                        String sql = "INSERT INTO keranjang_pengeluaran(kode_input, nama_barang, qty_barang, satuan, harga) VALUES('"
                                + txtkode_input.getText() +"','"+ keranjang.getValueAt(i, 1) +"','"+ keranjang.getValueAt(i, 2) 
                                +"','"+ keranjang.getValueAt(i, 3) +"','"+ keranjang.getValueAt(i, 4)+"')" ;
                        java.sql.Connection conn = (Connection)koneksi.configDB();
                        java.sql.PreparedStatement pstm = conn.prepareStatement(sql);
                        pstm.execute();
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
                
}

private void grand_total(){
    
     int sub_total = 0;
    for(int a = 0; a< keranjang.getRowCount(); a ++){
        sub_total += Integer.parseInt((String) keranjang.getValueAt(a, 4).toString().replace(".", ""));
    }
    txttotal.setText(nf.format(sub_total));
}

    /**
     * Creates new form form_pengeluaran
     */
    public form_pengeluaran() {
        initComponents();
        otomatis();
        tampil_combo_supplier();
        tampil_pengeluaran();
        
        txtkode_input.setEnabled(false);
        txtjenis_barang.setText("");
        btntambah.setEnabled(false);
        btnhapus.setEnabled(false);
        btnsimpan.setEnabled(false);
        btncetak.setEnabled(false);
        txtjenis_barang.setEnabled(false);
        txtsatuan.setEnabled(false);
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
            
            tabelpengeluaran.setModel(model);
            }
                 
           }catch (SQLException e){
               System.out.println("Error :"+ e.getMessage());
           }
       }
    
    public void tampil_combo_supplier(){
        try{
            String sql = "Select * From supplier";
            java.sql.Connection conn = (Connection)koneksi.configDB();
            java.sql.PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
                    
            while(rs.next()){
                cbsupplier.addItem(rs.getString("nama_supplier"));
            }  
            rs.last();
            int jumlahdata = rs.getRow();
            rs.first();
            
        }catch(Exception e){    
        }
    }
    
    private void tampil_jenis(){
        try{
            Connection conn=(Connection)koneksi.configDB();
            Statement stt=conn.createStatement();
            rssupplier=stt.executeQuery("SELECT * from supplier WHERE nama_supplier LIKE '%"+cbsupplier.getSelectedItem()+"%'");  
            
            if(rssupplier.next()){
                String jenis = rssupplier.getString("jenis_barang");
                txtjenis_barang.setText(jenis);
            }
            
        }catch (Exception ex) {
            System.err.println(ex.getMessage());
        }  
    }
    
    public void tampil_combo_nama(){
        cbnama.removeAllItems();
        
        try{
            String sql = "Select * From stok WHERE jenis_barang LIKE '%"+txtjenis_barang.getText()+"%'";
            java.sql.Connection conn = (Connection)koneksi.configDB();
            java.sql.PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
                    
            while(rs.next()){
                cbnama.addItem(rs.getString("nama_barang")); 
            }  
            
            rs.last();
            int jumlahdata = rs.getRow();
            rs.first();
            
        }catch(Exception e){    
        }   
    } 
    
    private void tampil_satuan(){
        try{
            Connection conn=(Connection)koneksi.configDB();
            Statement stt=conn.createStatement();
            rsstok=stt.executeQuery("SELECT * from stok WHERE nama_barang LIKE '%"+cbnama.getSelectedItem()+"%'");  
            if(rsstok.next()){
                String satuan = rsstok.getString("satuan");
                txtsatuan.setText(satuan);
            }
            
        }catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private void tambah_keranjang(){
        harga = Integer.parseInt(txtharga.getText());
        
        if(txtharga.getText().equals("")){
           JOptionPane.showMessageDialog(null, "Periksa Kembali !!!");
           
       }else{
           DefaultTableModel model = (DefaultTableModel) keranjang.getModel();
           
           Object obj [] = new Object[5];
           obj [1] = cbnama.getSelectedItem();
           obj [2] = txtqty.getText();
           obj [3] = txtsatuan.getText();
           obj [4] = harga;
           
           model.addRow(obj);
           
           int baris = model.getRowCount();
           
           for(int a = 0; a < baris; a ++){
               String no = String.valueOf(a + 1);
               model.setValueAt(no + ".", a, 0);
           }
            keranjang.setRowHeight(30);
            grand_total();
            txtqty.setText("");
            txtharga.setText("");
       }
        btnsimpan.setEnabled(true);
        cbnama.grabFocus();
        btntambah.setEnabled(false);
    }
    
    private void cari(){
        DefaultTableModel model = new DefaultTableModel();
           model.addColumn("No.");
           model.addColumn("TGL BELI ");
           model.addColumn("KODE INPUT ");
           model.addColumn("SUPPLIER ");
           model.addColumn("JENIS");
           model.addColumn("TOTAL PENGELUARAN");   
        try{
            int no = 1;
            Connection TugasVisual = DriverManager.getConnection("jdbc:mysql://localhost/burger","root","");
            ResultSet rs = TugasVisual.createStatement().executeQuery("SELECT * FROM pengeluaran WHERE tanggal_beli like '%" + txtcari.getText() + "%'" +
                "or supplier like '%" + txtcari.getText() + "%'" );
            while (rs.next()) {
                model.addRow(new Object[]{no++,
                    rs.getString(2),
                    rs.getString(1),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                });

            }
            tabelpengeluaran.setModel(model);
            
        }catch (Exception e){

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

        kGradientPanel2 = new keeptoo.KGradientPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        keranjang = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        txtkode_input = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        cbsupplier = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        txtjenis_barang = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        cbnama = new javax.swing.JComboBox<>();
        txtqty = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txttanggal = new com.toedter.calendar.JDateChooser();
        txtharga = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        txtsatuan = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        btntambah = new javax.swing.JButton();
        btnhapus = new javax.swing.JButton();
        btnsimpan = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelpengeluaran = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        txttotal = new javax.swing.JTextField();
        txtcari = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btncetak = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        kGradientPanel2.setkEndColor(new java.awt.Color(153, 255, 204));
        kGradientPanel2.setkGradientFocus(3000);
        kGradientPanel2.setkStartColor(new java.awt.Color(255, 255, 153));
        kGradientPanel2.setPreferredSize(new java.awt.Dimension(1276, 45));
        kGradientPanel2.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("     Pembelian Bahan ");
        kGradientPanel2.add(jLabel1, java.awt.BorderLayout.CENTER);

        add(kGradientPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        keranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Nama", "Qty", "Satuan", "Harga"
            }
        ));
        keranjang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                keranjangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(keranjang);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Kode Input");

        txtkode_input.setBorder(null);
        txtkode_input.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtkode_inputActionPerformed(evt);
            }
        });
        txtkode_input.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtkode_inputKeyPressed(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(255, 204, 0));
        jSeparator1.setForeground(new java.awt.Color(255, 204, 0));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("Supplier");

        cbsupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbsupplierActionPerformed(evt);
            }
        });
        cbsupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbsupplierKeyPressed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("Jenis Barang");

        txtjenis_barang.setBorder(null);
        txtjenis_barang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtjenis_barangActionPerformed(evt);
            }
        });
        txtjenis_barang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtjenis_barangKeyPressed(evt);
            }
        });

        jSeparator2.setBackground(new java.awt.Color(255, 204, 0));
        jSeparator2.setForeground(new java.awt.Color(255, 204, 0));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("Nama Barang");

        cbnama.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih ~" }));
        cbnama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbnamaActionPerformed(evt);
            }
        });

        txtqty.setBorder(null);
        txtqty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtqtyActionPerformed(evt);
            }
        });
        txtqty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtqtyKeyPressed(evt);
            }
        });

        jSeparator4.setBackground(new java.awt.Color(255, 204, 0));
        jSeparator4.setForeground(new java.awt.Color(255, 204, 0));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setText("Qty");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setText("Satuan");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setText("Tanggal Pembelian");

        txtharga.setBorder(null);
        txtharga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthargaActionPerformed(evt);
            }
        });
        txtharga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txthargaKeyPressed(evt);
            }
        });

        jSeparator3.setBackground(new java.awt.Color(255, 204, 0));
        jSeparator3.setForeground(new java.awt.Color(255, 204, 0));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setText("Harga Beli");

        txtsatuan.setBorder(null);
        txtsatuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsatuanActionPerformed(evt);
            }
        });
        txtsatuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtsatuanKeyPressed(evt);
            }
        });

        jSeparator6.setBackground(new java.awt.Color(255, 204, 0));
        jSeparator6.setForeground(new java.awt.Color(255, 204, 0));

        btntambah.setText("Tambah");
        btntambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntambahActionPerformed(evt);
            }
        });

        btnhapus.setText("Hapus");
        btnhapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhapusActionPerformed(evt);
            }
        });

        btnsimpan.setText("Simpan");
        btnsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpanActionPerformed(evt);
            }
        });

        tabelpengeluaran.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelpengeluaran.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelpengeluaranMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelpengeluaran);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setText("Grand Total   :");

        jSeparator7.setBackground(new java.awt.Color(255, 204, 0));
        jSeparator7.setForeground(new java.awt.Color(255, 204, 0));

        txttotal.setBorder(null);
        txttotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotalActionPerformed(evt);
            }
        });
        txttotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txttotalKeyPressed(evt);
            }
        });

        txtcari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcariActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cari24.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btncetak.setText("CETAK");
        btncetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncetakActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(jLabel12)
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtkode_input, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addComponent(jLabel18)
                        .addGap(18, 18, 18)
                        .addComponent(txttanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(256, 256, 256)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(211, 211, 211)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel13)
                                            .addComponent(jLabel14)
                                            .addComponent(jLabel15))
                                        .addGap(36, 36, 36)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cbsupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtjenis_barang, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cbnama, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel16)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtqty, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel17)
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtsatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addGap(58, 58, 58)
                                        .addComponent(txtharga, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(44, 44, 44)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btntambah, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnhapus, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(246, 246, 246)
                                        .addComponent(jLabel20)
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jSeparator7)
                                            .addComponent(txttotal)))))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 862, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btncetak)))))
                .addGap(346, 346, 346))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtkode_input, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txttanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cbsupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(txtjenis_barang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17)
                                .addComponent(cbnama, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(txtqty, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(15, 15, 15)
                                        .addComponent(txtsatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, 0)
                                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtharga, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btntambah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnhapus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, 0)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtcari)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btncetak)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txtkode_inputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtkode_inputActionPerformed
    
    }//GEN-LAST:event_txtkode_inputActionPerformed

    private void txtkode_inputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtkode_inputKeyPressed
      
    }//GEN-LAST:event_txtkode_inputKeyPressed

    private void txtjenis_barangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtjenis_barangActionPerformed
        
    }//GEN-LAST:event_txtjenis_barangActionPerformed

    private void txtjenis_barangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtjenis_barangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtjenis_barangKeyPressed

    private void txtqtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtqtyActionPerformed
        txtharga.grabFocus();
        btntambah.setEnabled(true);
    }//GEN-LAST:event_txtqtyActionPerformed

    private void txtqtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtqtyKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtqtyKeyPressed

    private void txthargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthargaActionPerformed
       tambah_keranjang();
       cbsupplier.setEnabled(false);
    }//GEN-LAST:event_txthargaActionPerformed

    private void txthargaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txthargaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthargaKeyPressed

    private void txtsatuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsatuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtsatuanActionPerformed

    private void txtsatuanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsatuanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtsatuanKeyPressed

    private void btntambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntambahActionPerformed
        tambah_keranjang();
    }//GEN-LAST:event_btntambahActionPerformed

    private void cbsupplierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbsupplierKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbsupplierKeyPressed

    private void cbsupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbsupplierActionPerformed
        tampil_jenis();
        tampil_combo_nama();
    }//GEN-LAST:event_cbsupplierActionPerformed

    private void cbnamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbnamaActionPerformed
        tampil_satuan();
        txtqty.grabFocus();
        
    }//GEN-LAST:event_cbnamaActionPerformed

    private void btnhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhapusActionPerformed
        hapus_baris_tabel();
        btnhapus.setEnabled(false);
        grand_total();
    }//GEN-LAST:event_btnhapusActionPerformed

    private void keranjangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_keranjangMouseClicked
        btnhapus.setEnabled(true);
    }//GEN-LAST:event_keranjangMouseClicked

    private void btnsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpanActionPerformed
        
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = String.valueOf(fm.format(txttanggal.getDate()));
    
        total = Integer.parseInt(txttotal.getText().replace(".", ""));
                 try {
                        String sql = "INSERT INTO pengeluaran(kode_input, tanggal_beli, supplier, jenis, total_pengeluaran) VALUES('"
                                + txtkode_input.getText() +"','"+ tanggal+"','"+ cbsupplier.getSelectedItem()
                                +"','"+ txtjenis_barang.getText() +"','"+ total +"')" ;
                        java.sql.Connection conn = (Connection)koneksi.configDB();
                        java.sql.PreparedStatement pstm = conn.prepareStatement(sql);
                        pstm.execute();
                    JOptionPane.showMessageDialog(null, "Simpan Data Berhasil...");
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }    
        simpan_keranjang();
        kosong_tabel();
        otomatis();
        tampil_pengeluaran();
        
        cbsupplier.setEnabled(true);
        btnsimpan.setEnabled(false);
        txttanggal.setDate(null);
        txtjenis_barang.setText("");
        txttotal.setText("");
    }//GEN-LAST:event_btnsimpanActionPerformed

    private void txttotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotalActionPerformed

    private void txttotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttotalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotalKeyPressed

    private void btncetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncetakActionPerformed
            try{
                String path="src/struk/struk_pengeluaran.jasper";
                HashMap parameter = new HashMap();
                java.sql.Connection conn = (Connection)koneksi.configDB();
                parameter.put("tanggal", txttanggal.getDate());
                JasperPrint print = JasperFillManager.fillReport(path, parameter, conn);
                JasperViewer.viewReport(print, false);
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,"dokumen tidak ada"+e);
            }
        tampil_pengeluaran();
        btncetak.setEnabled(false);
        txtcari.setText("");
        txttanggal.setDate(null);
    }//GEN-LAST:event_btncetakActionPerformed

    private java.util.Date getTanggalFromTableAnggota(JTable tabelpengeluaran, int i) {
        JTable tabel = tabelpengeluaran;
        String str_tgl = String.valueOf(tabel.getValueAt(tabel.getSelectedRow(), i));
        java.util.Date tanggal = null;
        try {
            tanggal = new SimpleDateFormat("yyy-MM-dd").parse(str_tgl);
        } catch (ParseException ex) {
            Logger.getLogger(form_pengeluaran.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tanggal;
    }
    
    private void tabelpengeluaranMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelpengeluaranMouseClicked
        btncetak.setEnabled(true);
        int baris = tabelpengeluaran.rowAtPoint(evt.getPoint());
        txttanggal.setDate(getTanggalFromTableAnggota(tabelpengeluaran,1));
        
    }//GEN-LAST:event_tabelpengeluaranMouseClicked

    private void txtcariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcariActionPerformed
        cari();
    }//GEN-LAST:event_txtcariActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        cari();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btncetak;
    private javax.swing.JButton btnhapus;
    private javax.swing.JButton btnsimpan;
    private javax.swing.JButton btntambah;
    private javax.swing.JComboBox<String> cbnama;
    private javax.swing.JComboBox<String> cbsupplier;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private keeptoo.KGradientPanel kGradientPanel2;
    private javax.swing.JTable keranjang;
    private javax.swing.JTable tabelpengeluaran;
    private javax.swing.JTextField txtcari;
    private javax.swing.JTextField txtharga;
    private javax.swing.JTextField txtjenis_barang;
    private javax.swing.JTextField txtkode_input;
    private javax.swing.JTextField txtqty;
    private javax.swing.JTextField txtsatuan;
    private com.toedter.calendar.JDateChooser txttanggal;
    private javax.swing.JTextField txttotal;
    // End of variables declaration//GEN-END:variables
}
