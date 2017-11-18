/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chaudiere;

/**
 *
 * @author Nabot
 */
public class Batiment
{

  private static final double PERTE_PAR_FENETRE = 128; // en Watt
  private static final double PERTE_PAR_PORTE = 242; // en Watt
  private static final double PERTE_POUR_PLANCHER = 18; // en W.m-2
  private static final double PERTE_POUR_MUR = 12; // en W.m-2
  private static final double PERTE_POUR_PLAFOND = 8; // en W.m-2
  private int nbPortes;
  private int nbFenetre;
  private double largeur;
  private double longeur;
  private double hauteur;

  /*
   * largeur, longeur, hauteur en m
   */
  public Batiment(
          int nbPortes,
          int nbFenetre,
          double largeur,
          double longeur,
          double hauteur)
  {
    this.nbPortes = nbPortes;
    this.nbFenetre = nbFenetre;
    this.largeur = largeur;
    this.longeur = longeur;
    this.hauteur = hauteur;
  }

  /*
   * Perte d'énergie en Watt
   */
  public double getPerteEnergie()
  {
    return PERTE_PAR_FENETRE * nbFenetre
            + PERTE_PAR_PORTE * nbPortes
            + PERTE_POUR_PLANCHER * largeur * longeur
            + PERTE_POUR_MUR * largeur * hauteur * 2 + PERTE_POUR_MUR * longeur * hauteur * 2
            + PERTE_POUR_PLAFOND * largeur * longeur;
  }

  /*
   * Volume d'air de l'ensemble en m3
   */
  public double getVolumeDeAir()
  {
    return longeur * largeur * hauteur;
  }

  public double getLongeur()
  {
    return longeur;
  }

  public double getLargeur()
  {
    return largeur;
  }
}
