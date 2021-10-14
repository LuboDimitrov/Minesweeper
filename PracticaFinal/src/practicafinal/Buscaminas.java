/*
Clase Principal del juego del buscaminas
Autores: Joan Pascual Alcaraz y Lyubomir Lyubomirov Dimitrov
https://www.youtube.com/watch?v=0BKkCj9iD9g&t=3s
 */
package practicafinal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Buscaminas extends JFrame implements Serializable {
    //Atributos
    private final int WIDTH = 635;
    private final int HEIGHT = 687;
    private final JMenu menu;
    private final JMenuBar menubar;
    private final JMenuItem Obrir;
    private final JMenuItem Desar;
    private final JMenuItem Reiniciar;
    private final JMenuItem Sortir;
    private Tauler tauler;
    
    public Buscaminas(){
        //Declaramos un objeto tablero
        tauler = new Tauler();
        menubar = new JMenuBar();
        menu = new JMenu("Opcions");
        //Declaramos el item "Obrir" con el que podremos abrir una partida guardada.
        Obrir = new JMenuItem("Obrir");
        //Le añadimos el listener y llamamos al metodo ActionPerformed
        Obrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    //Metodo ActionPerformed
                    ObrirActionPerformed(evt);
                } catch (IOException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        //Con "desar" podremos guardar una partida
        Desar = new JMenuItem("Desar");
        //Le añadimos el listender
        Desar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    GuardarActionPerformed(evt);
                } catch (IOException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        //Reiniciar nos permite empezar una partida de cero
        Reiniciar = new JMenuItem("Reiniciar");
        Reiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                reiniciarActionPerformed(evt);
            }
        });
        //Salir nos permite poner fin a la ejecución del programa
        Sortir = new JMenuItem("Sortir");
        Sortir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                SortirActionPerformed(evt);
            }
        });
        //Añadimos los items al menu
        menu.add(Obrir);
        menu.add(Desar);
        menu.add(Reiniciar);
        menu.add(Sortir);
        //Añadimos el menu en la barra
        menubar.add(menu);
        this.setJMenuBar(menubar);
        this.pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //A la ventana le ponemos el título y
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle("BuscaMinas");
        add(tauler);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args){
        new Buscaminas();
    }
    //Metodo para salir del programa
    public void SortirActionPerformed(ActionEvent evt){
        System.exit(0);
    }
    
    //Para reiniciar el tablero primero creamos los objetos casilla
    //después colocamos las minas y vamos llamando a los sucesivos métodos.
    //Finalmente pintamos el tablero.
    private void reiniciarActionPerformed(ActionEvent evt){
        tauler.objectescasella();
        tauler.empezar();
        tauler.repaint();   
    }
    
    //Método para guardar una partida ( guardamos el objeto tablero )
    private void GuardarActionPerformed(ActionEvent evt) throws FileNotFoundException, IOException{
        //Declaraciones
        JFileChooser fc = new JFileChooser();
        fc.showSaveDialog(null);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        File partida = fc.getSelectedFile();
        FileOutputStream fos = new FileOutputStream(partida);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        //Escribimos el objeto y vaciamos el buffer.
        oos.writeObject(tauler);
        oos.flush();
        //Indicamos con un mensaje que la partida se ha guardado de forma correcta
        JOptionPane.showMessageDialog(null,"Partida guardada correctamente.");
        //Finalmente cerramos el OOS y el FOS.
        oos.close();
        fos.close();
    }
    
    //Método para recuperar una partida a partir de un fichero
    private void ObrirActionPerformed(ActionEvent evt) throws FileNotFoundException, IOException, ClassNotFoundException{
        //Declaramos un objeto file choose en modo archivos y directorios para pode elegir el archivo.dat 
        JFileChooser fc = new JFileChooser();
        int valor = fc.showSaveDialog(null);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if(valor == JFileChooser.APPROVE_OPTION){  
            File partida = fc.getSelectedFile();
            FileInputStream fos = new FileInputStream(partida);
            ObjectInputStream ois = new ObjectInputStream(fos);
            //Hacemos el casting
            Tauler t = (Tauler) ois.readObject();
            //Cargamos la partida
            tauler.carregar(t);          
        }
    }
}