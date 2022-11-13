package tarea3.progra2;

import java.util.ArrayList;
import tarea3.progra2.exceptions.PagoIncorrectoException;
import tarea3.progra2.exceptions.NoHayBebidaException;
import tarea3.progra2.exceptions.PagoInsuficienteException;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
// import javax.swing.*;

public class Expendedor {
   private int precios[];
   private int capacidad;
   private DepositoMonedas vuelto;
   private DepositoMonedas monedas;
   private ArrayList<Deposito> depositos;
   private BufferedImage img;
   private Bebida bebidaComprada;
   private static final int x = 400;
   private static final int y = 0;

   public Expendedor (int capacidad, int precios[]) {
      this.precios = precios;
      this.capacidad = capacidad;
      this.depositos = new ArrayList<Deposito>();
      bebidaComprada = null;
      vuelto =  new DepositoMonedas();
      Deposito d = null;
      Bebida b = null;
      try {
         img = ImageIO.read(getClass().getResource("assets/maquina.png"));
      }
      catch (java.io.IOException e) {
         System.out.println(e);
      }
      for (int i=0;i<3;++i) {
         d = new Deposito();
         for (int j=0;j<capacidad;++j) {
            switch (i) {
               case 0 -> b = new CocaCola(i*j + j);
               case 1 -> b = new Sprite(i*j + j);
               case 2 -> b = new Fanta(i*j + j);
            }
            d.addBebida(b);
         }
         depositos.add(d);
      }
   }
   public boolean paint (Graphics g) {
      try {
         g.drawImage(this.img, x, y, null);
         for (int i=0;i<depositos.size();++i) {
            if (!depositos.get(i).paint(g, i))
               return false;
         }
         return true;
      }
      catch (Exception e) {
         System.out.println(e);
         return false;
      }
   }
   public void comprarBebida(Moneda m, int tipo) {
      try {
         if (m == null) {
            throw new PagoIncorrectoException();
         }
         else if (tipo < 0 || 2 < tipo) {
            vuelto.addMoneda(m);
            throw new NoHayBebidaException();
         }
         else if (m.getValor() >= precios[tipo]) {
            Bebida b = depositos.get(tipo).getBebida();
            if (b == null) {
               vuelto.addMoneda(m);
               throw new NoHayBebidaException();
            }
            Moneda c;
            for (int i=precios[tipo]/100; i<m.getValor()/100; i++){
               c = new Moneda100();
               vuelto.addMoneda(c);
            }
            if (bebidaComprada == null)
               bebidaComprada = b;
         }
         else if (m.getValor() < precios[tipo]) {
            vuelto.addMoneda(m);
            throw new PagoInsuficienteException();
         }
      }
      catch (PagoIncorrectoException ex) {
         System.out.println(ex.getMessage());
      }
      catch (PagoInsuficienteException ex) {
         System.out.println(ex.getMessage());
      }
      catch (NoHayBebidaException ex) {
         System.out.println(ex.getMessage());
      }
   }
   public Moneda getVuelto(){
       return vuelto.getMoneda();
   }
   public Bebida getBebida () {
      Bebida b = bebidaComprada;
      bebidaComprada = null;
      return b;
   }
}
