package chaudiere;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nabot
 */
final class CapaciteCalorifique
{

  private static final double KELVIN = 273.15; // temperature en Kelvin 273.15K = 0°C
  private double capacite; // capacité calorifique en KJ.Kg-1.K-1
  private double masse; // masse en Kg
  private double temperature; // température en Kelvin

  /*
   * capacite : capacité calorifique en KJ.Kg-1.K-1
   * masse : masse en Kg
   * temperature : température en °C
   */
  public CapaciteCalorifique(final double capacite, final double masse, final double temperature) throws Exception
  {
    this.capacite = capacite;
    if (capacite < 0) // incohérence en physique
    {
      throw new Exception("Capacité négative : Le système se refroidit quand on le chauffe.");
    }
    this.masse = masse;
    if (masse < 0) // incohérence en physique
    {
      throw new Exception("Masse négative n'a pas de sens.");
    }
    this.temperature = temperature + KELVIN; // conversion en Kelvin
    if (this.temperature < 0) // incohérence en physique
    {
      throw new Exception("Une température ne peux pas être inférieur au zéro absolu.");
    }
  }

  public double getTemperature()
  {
    return temperature;
  }

  public void setTemperature(double temperature)
  {
    this.temperature = temperature;
  }

  /*
   * Variation d'anergie par apport à la température inital
   * temperatureInitial : Température avant la transformation en °K
   * retourne : Variation Etat final - Etat initial en KJ
   */
  public double variationEnergie(final double temperatureInitial)
  {
    return capacite * masse * (temperature - temperatureInitial);
  }

  /*
   * Energie gagné ou perdu avec un delta de température
   * deltaTemperature : variation de la température Delta(T) = Tfinal - Tinitial en °K
   * Retourné KJ gagné ou perdu.
   */
  public double energieAvecDeltaDeTemperature(final double deltaTemperature)
  {
    return capacite * masse * deltaTemperature;
  }

  /*
   * Température final du système aprés 1 sec d'apport ou retraitn d'énergie
   * puissance : en KWatt ou KJ.s-1
   * Température aprés transformation en °K
   */
  public double temperatureApresUneSeconde(final double puissance)
  {
    temperature += (puissance / (capacite * masse));
    return temperature;
  }
}
