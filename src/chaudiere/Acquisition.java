/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chaudiere;

/**
 *
 * @author Nabot
 */
public class Acquisition
{

  private static final double KELVIN = 273.15; // temperature en Kelvin 273.15K = 0°C
  private double energieConsommeeCumulee; // en KJ
  private double temperatureEau; // en °C
  private double temperatureAir; // en °C

  /*
   * energieConsommeeCumulee en KJ
   * temperatureEau en °K
   * temperatureAir en °K
   */
  public Acquisition(
          final double energieConsommeeCumulee,
          final double temperatureEau,
          final double temperatureAir)
  {
    this.energieConsommeeCumulee = energieConsommeeCumulee;
    this.temperatureEau = temperatureEau - KELVIN;
    this.temperatureAir = temperatureAir - KELVIN;
  }

  public double getEnergieConsommeeCumulee()
  {
    return energieConsommeeCumulee;
  }

  public double getTemperatureCelsusEau()
  {
    return temperatureEau;
  }

  public double getTemperatureCelsusAir()
  {
    return temperatureAir;
  }

  public double getTemperatureKelvinEau()
  {
    return temperatureEau + KELVIN;
  }

  public double getTemperatureKelvinAir()
  {
    return temperatureAir + KELVIN;
  }
}
