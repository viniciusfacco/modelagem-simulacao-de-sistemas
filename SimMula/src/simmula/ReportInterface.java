/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simmula;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 *
 * @author Vinicius
 */
public class ReportInterface extends javax.swing.JFrame {

    /**
     * Creates new form ReportInterface
     */
    public ReportInterface() {
        initComponents();
        setVisible(true);
        centralize_screen();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtaReport = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Report");

        jtaReport.setEditable(false);
        jtaReport.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        jtaReport.setColumns(20);
        jtaReport.setRows(5);
        jScrollPane1.setViewportView(jtaReport);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void appendText(String text){
        this.jtaReport.append(text + "\n");
    }
    
    //método para centralizar tela principal
    private void centralize_screen() {
        // Centraliza a janela de abertura no centro do desktop.  
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();  
        Rectangle r      = this.getBounds();  
        // Dimensões da janela  
        int widthSplash = r.width ;  
        int heightSplash = r.height;  
        // calculo para encontrar as cooredenadas X e Y para a centralização da janela.  
        int posX = (screen.width / 2) - ( widthSplash / 2 );  
        int posY = (screen.height / 2) - ( heightSplash / 2 );  
        this.setBounds(posX,posY,widthSplash,heightSplash);
    }    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jtaReport;
    // End of variables declaration//GEN-END:variables
}