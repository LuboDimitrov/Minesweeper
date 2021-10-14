/*
Clase Casilla del juego del buscaminas
Autores: Joan Pascual Alcaraz y Lyubomir Lyubomirov Dimitrov
https://www.youtube.com/watch?v=0BKkCj9iD9g&t=3s
 */

package practicafinal;

import java.io.Serializable;

public class Casella  implements Serializable{
    private int valor; //número de la imatge
    private boolean mina; //és mina o no
    private boolean marcada; //té bandera o no
    private boolean tapada; //està tapada o no
    private boolean comprovada; //ha estat comprovada o no
    
    //Constructor
    public Casella(){
        this.valor = 0;
        this.tapada = true;
        this.mina = false;
        this.marcada = false;
    }

    //Getters,setters i comprovacions
    public int getValor() {
        return valor;
    }
    public void destapar(){
        tapada = false;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public boolean isMina() {
        return mina;
    }
    
    public void borrarComprovades() {
        comprovada = false;
    }

    public void setMina(boolean mina) {
        this.mina = mina;
    }

    public boolean isMarcada() {
        return marcada;
    }

    public void setMarcada(boolean marcada) {
        this.marcada = marcada;
    }

    public boolean isTapada() {
        return tapada;
    }

    public void setTapada(boolean tapada) {
        this.tapada = tapada;
    }

    public boolean isComprovada() {
        return this.comprovada;
    }

    public void setComprovada(boolean comprovada) {
        this.comprovada = comprovada;
    }
    
    public boolean minatapada(){
        return this.tapada && this.mina;
    }
    
    public boolean buida(){
        return this.valor == 0;
    }
    
    public void comprovada(){
        comprovada = true;
    }
    
    public void setMarca(boolean b){
        this.marcada = b;
    }
    
}
