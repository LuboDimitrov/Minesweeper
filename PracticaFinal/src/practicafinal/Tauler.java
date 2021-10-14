/*
Clase Tablero del juego del buscaminas
Autores: Joan Pascual Alcaraz y Lyubomir Lyubomirov Dimitrov
https://www.youtube.com/watch?v=0BKkCj9iD9g&t=3s
 */
package practicafinal;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Tauler extends JPanel implements MouseListener, Serializable{
    //Atributos de la clase
    private final int files = 9;
    private final int columnes = 9;
    private final int dimcasella = 70; //dimensio de les imatges
    private boolean jugant; // boolean per controlar si la partida ha acabat o no
    private int banderes; //numero de banderes
    private transient Image[] IMATGES; //transient para que no se serialice
    private Casella[][] caselles = new Casella[files][columnes];
    private int nimatges = 15; //tenemos un array de imagenes y cada numero representa una imagen.
    private int nmines = 9; //  10 mines de 0 a 9
    private int minesrestants;
    
    //Getters y setters 
    public boolean isJugant() {
        return jugant;
    }

    public void setJugant(boolean jugant) {
        this.jugant = jugant;
    }

    public int getBanderes() {
        return banderes;
    }

    public void setBanderes(int banderes) {
        this.banderes = banderes;
    }

    public Image[] getIMATGES() {
        return IMATGES;
    }

    public void setIMATGES(Image[] IMATGES) {
        this.IMATGES = IMATGES;
    }

    public Casella[][] getCaselles() {
        return caselles;
    }

    public void setCaselles(Casella[][] caselles) {
        this.caselles = caselles;
    }

    public int getNimatges() {
        return nimatges;
    }

    public void setNimatges(int nimatges) {
        this.nimatges = nimatges;
    }

    public int getNmines() {
        return nmines;
    }

    public void setNmines(int nmines) {
        this.nmines = nmines;
    }

    public int getMinesrestants() {
        return minesrestants;
    }

    public void setMinesrestants(int minesrestants) {
        this.minesrestants = minesrestants;
    }
    
    //Metodo constructor de la clase
    public Tauler() {
        //Carregam les imatges a l array d imatges.
        //Cada imatge representa un numero
        IMATGES = new Image[nimatges];
        for (int i = 0; i < nimatges; i++) {
            String path = "IMATGES/" + i + ".png";
            IMATGES[i] = new ImageIcon(path).getImage();
        }
        //Utilitzam el doblebuffer per a que la imatge se mostri de forma instantànea.
        this.setDoubleBuffered(true);
        //le añadimos el mouse listener
        this.addMouseListener(this);
        this.empezar();
    }

    /*
    Con el método empezar posam el contador de banderes a 0
    i colocam les 10 mines de forma aleatoria
    */
    public void empezar(){
        banderes = 0; 
        Random random = new Random();
        jugant = true;
        this.minesrestants = nmines;
        objectescasella();
        //10 mines
        int restant = nmines;
        //Mentre no haguem posat les 10
        while (restant >= 0) {
            int x = random.nextInt(files);
            int y = random.nextInt(columnes);
            Casella casella = caselles[x][y];
            //No podem posar una mina a una casella que ja en té una
            if (!casella.isMina()) {
                casella.setMina(true);
                restant--;
            }
        }
        numerodecasella();
    }
    
    //Cream un objecte per a cada casella. 81
    public void objectescasella () {
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; ++j) {
                caselles[i][j] = new Casella();
            }
        }
    }
    
    //
    private void numerodecasella(){
        //Contador de mines que té una casella al seu voltant
        int contador;
        //Recorrem tot el tauler
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                Casella casella = caselles[i][j];
                //Comprovam si la casella té una mina
                if(casella.isMina() == false){
                    //Si la casella no te una mina el que feim es
                    //contar les mines que té al seu voltant per 
                    //posar-li un numero a la casella
                    contador = MinesCostat(i,j);
                    casella.setValor(contador);
                }
            }
        }
    }
    //Mètode per saber quantes mines té una casella al seu voltant
    private int MinesCostat(int a, int b){
        //Suposam que no te mines primerament
        int mines = 0;
        int dx;
        int dy;
        //Anam recorrent tots els veinats amb un doble for
        for(int i=-1; i<=1; i++){
            dx = a + i;
            //Comprovam que estiguem en el tauler
            if(dx<0 || dx>=files){
                continue;
            }
            for(int j=-1; j<=1; j++){
                dy = b +j;
                if(dy<0 || dy>=files){
                    continue;
                }
                if (i == 0 && j == 0) {
                    continue;
                }
                //Si a la casella hi ha una mina, augmentam el numero de mines
                if (caselles[dx][dy].isMina()) {
                    mines++;
                }
            }
        }
        //Retornam quantes mines tenia aquesta casella
        return mines;
    }
    
    public void paint(Graphics g) {  
        int x,y,imagen;
        //Recorem totes el tauler de caselles
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                Casella casella = caselles[i][j];
                if (jugant) {
                    //Comprovam que no haguem pitjat damunt una mina
                    if (casella.isMina() && !casella.isTapada()) {
                        jugant = false;
                    }     
                }
                imagen = SeleccionarImatge(casella);
                //Dimensions de la casella
                x = (j * dimcasella);
                y = (i * dimcasella);
                //pintam la imatge a la posicio x,y
                g.drawImage(IMATGES[imagen], x, y, this);
            }
        }
    }
    //Metodo para seleccionar una imagen del array de imagenes
    private int SeleccionarImatge(Casella casella) {
        //li posam un numero a les imatges 
        int imagen = casella.getValor();
        int imina = 13;
        int imtapada = 11;
        int immarcada = 10;
        int imexplotada = 14;
        //Anam comprovant cada condició per asignar una imatge o una altra
        if (!jugant) {
            if(!casella.isTapada() && casella.isMina()){
                imagen = imexplotada;
            } else if (casella.isTapada() && casella.isMina()){                
                casella.destapar();
                imagen = imina;
            } else if (casella.isMarcada()) {
                if (casella.isMina()) {
                    imagen = immarcada;
                }
            }
            } else {
                if (casella.isMarcada()) {
                    imagen = immarcada;
                } else if (casella.isTapada()) {
                    imagen = imtapada;
                }
            }
        return imagen;
    }
        
    
    //Metodo para pintar un tablero desde un archivo
    public void carregar(Tauler tauler){
        //Amb un doble for anam recorrent tot el tauler 
        //i posam a cada casella lo que hi ha al tauler que ens passen per paràmetre
        for(int i=0; i<files; i++){
             for(int j=0; j<columnes; j++){
                caselles[i][j].setMarcada(tauler.caselles[i][j].isMarcada());
                caselles[i][j].setMina(tauler.caselles[i][j].isMina());
                caselles[i][j].setTapada(tauler.caselles[i][j].isTapada());
                caselles[i][j].setValor(tauler.caselles[i][j].getValor());
                caselles[i][j].setComprovada(tauler.caselles[i][j].isComprovada());
            }           
        }
        //pintam
        repaint();
    }
    
    //Metodo para saber si una casella es buida
    private boolean comprovarbuida(Casella casella) {
        //Primer miram que no la haguem comprovat anteriorment
        if (!casella.isComprovada()) {
            if (casella.buida()) {
                return true;
            }
        }
        return false;
    }
    
    //Cercar buides
    public void trobarBuides(int x, int y, int vegades) {
        int dx,dy;
        //Anam recorrent tots els veinats amb un doble for
        for (int i = -1; i <= 1; i++) {
            dx = x + i;
            //Comprovam que estiguem dins el tauler
            if (dx < 0 || dx >= files) {
                continue;
            }
            for (int j = -1; j <= 1; ++j) {
                 dy = y + j;
                 //Comprovam que estiguem dins el tauler
                if (dy < 0 || dy >= columnes) {
                    continue;
                }
                if (!(i == 0 || j == 0)) {
                    continue;
                }
                Casella cas = caselles[dx][dy];
                //Si la casilla esta vacia la destapamos y
                //la marcamos como comprobada
                if (comprovarbuida(cas)) {
                    //Destapam la casella
                    cas.destapar();
                    //la marcam com a comprovada
                    cas.comprovada();
                    //i destapam les caselles buides al voltant
                    destaparVoltants(dx, dy);
                    trobarBuides(dx, dy, vegades + 1);
                }
            }
        }
        if (vegades == 0) {
            borrartotes();
        }
    }
    //Metode per posar totes les caselles com a no comprovades
    private void borrartotes() {
        for (int i = 0; i < files; ++i){
            for (int j = 0; j < columnes; ++j){
                caselles[i][j].borrarComprovades();
            }
        }
    }
    /*
    Mètode destapar caselles al voltant
    */
     private void destaparVoltants(int x, int y){
        int dx,dy;
        //Anam recorrent tots els veinats amb un doble for
        for (int i = -1; i <= 1; ++i) {
            dx = x+i;
            //Comprovam que estiguem dins el tauler
            if (dx < 0 || dx >= files) {
                continue;
            }
            for (int j = -1; j <= 1; ++j) {
                dy = y + j;
                //Comprovam que estiguem dins el tauler
                if (dy < 0 || dy >= columnes) {
                    continue;
                }
                if (i == 0 || j == 0) {
                    continue;
                }
                Casella cas = caselles[dx][dy];
                if (cas.isTapada() && !cas.buida()) {
                    cas.destapar();
                }
            }
        }
    }
        
    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        //posx i posy per saber on hem pitjat
        int posx = me.getX() / dimcasella;
        int posy = me.getY() / dimcasella;
        //actualitzar ens servirà per saber quan hem de repintar el tauler.
        boolean actualitzar = false;
        //Objecte casella
        Casella casellapitjada;
        //Comprovam que haguem pitjat dins el tauler
        if ((posx < 0 || posx >= columnes) || (posy < 0 || posy >= files)) {
            return;
        }

        casellapitjada = caselles[posy][posx];
        //Pitjam amb el botó dret
        if (me.getButton() == MouseEvent.BUTTON3) {
            actualitzar = true;
            //Si la casella està destapada no feim res
            if (!casellapitjada.isTapada()) {
                return;
            }
            //Comprovam que no tengui una bandera
            if (!casellapitjada.isMarcada()) {
                //Comprovam que ja no estiguin posades les 10 banderes
                if(banderes < 10){
                   casellapitjada.setMarca(true);
                   banderes++;
                }
            } else {
                //Desmarcar una casella
                casellapitjada.setMarca(false);
                banderes--;
            }
        //Pitjam amb el botó esquerre per destapar les caselles
        } else {
            //Si pitjam damunt una casella marcada o destapada no feim res
            if (casellapitjada.isMarcada() || !casellapitjada.isTapada()) {
                return;
            }
            actualitzar = true;
            //Destapam la casella que hem pitjada
            casellapitjada.destapar();
            //Si pitjam una damunt una mina hem perdut
            if (casellapitjada.isMina()) {
                JOptionPane.showMessageDialog(null,"Has perdut");
                jugant = false;
            } else if (casellapitjada.buida()) {
                trobarBuides(posy, posx, 0);
            }
        }
        //Comprovam si hem guanyat i ho notificam a l usuari
        if(guanyat()){
            JOptionPane.showMessageDialog(null,"Has guanyat");
            jugant = false;
        }
        if (actualitzar) {
            repaint();
        }
    }
    
    //Mètode que comprova si hem guanyat
    public boolean guanyat(){
        boolean victoria = false;
        int countdestapades = 0;
        //Recorrem tot el tauler
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; ++j) {
                Casella casella = caselles[i][j];
                if(!casella.isTapada()){
                    countdestapades++;
                }
            }
        }
        //Si hem destapat 71 caselles hem guanyat
        if(countdestapades == 71 && jugant){
            victoria = true;
        }
        return victoria;
        
    }

    @Override
    public void mouseReleased(MouseEvent me) {    
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
}


