/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chaudiere;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nabot
 */
public class Chauffage
{

  private static final double EPAISSEUR_ACIER = 0.00001; // 0.01 mm en contact avec l'eau
  private static final double KELVIN = 273.15; // temperature en Kelvin 273.15K = 0°C
  // unité en KJ.Kg-1.K-1
  private static final double CAPACITE_CALORIFIQUE_EAU = 4.185; // eau comprise entre 0 et 100°C à 10hPa
  private static final double CAPACITE_CALORIFIQUE_AIR = 1.015; // air à 20°C pour 30 à 40 % d'humidité
  private static final double CAPACITE_CALORIFIQUE_PLATRE = 0.830; // Platre
  private static final double CAPACITE_CALORIFIQUE_AUTRES = 2.5; // Canapé, meubles livre, lit
  // unité en Kg.m-3
  private static final double MASSE_VOLUMIQUE_AIR = 1.204;
  private static final double MASSE_VOLUMIQUE_EAU = 1000;
  private static final double MASSE_VOLUMIQUE_PLATRE = 1600;
  private static final double MASSE_VOLUMIQUE_AUTRES = 3; // Valeur empirique
  //
  private double puissanceChaudiere; // Puissance de la chaudière en KW
  private double temperatureMin; // Température minimum en °C avant déclenchement du chauffage
  private double temperatureMax; // Température maximun en °C avant extinction du chauffage
  private double temperatureMaxChaudiere; // Température maximun en °C de la chaudière
  //
  private CapaciteCalorifique CapaciteCalorifiqueEau;
  private double pourcentageCapteParRadiateur;
  private CapaciteCalorifique CapaciteCalorifiqueAir;
  private double pourcentageEnergieAir;
  private CapaciteCalorifique CapaciteCalorifiquePlatre;
  private double pourcentageEnergiePlatre;
  private CapaciteCalorifique CapaciteCalorifiqueAutres;
  private double pourcentageEnergieAutres;
  //
  private double perteCalorifique; // en Kilo Watt

  /*
   * puissanceChaudiere : Puissance de la chaudière en KW
   * volumeEauChauffage : Volume d'eau contenu dans les radiateurs en m3
   * batiment : constituant du batiment
   * surfaceRadiateur : surface d'échange de chauffage
   * temperatureMin : Température minimum en °C avant déclenchement du chauffage
   * temperatureMax : Température maximun en °C avant extinction du chauffage
   * temperatureMaxChaudiere : Température maximun en °C de la chaudière
   */
  public Chauffage(final double puissanceChaudiere,
          final double volumeEauChauffage,
          final Batiment batiment,
          final double surfaceRadiateur,
          final double temperatureMin,
          final double temperatureMax,
          final double temperatureMaxChaudiere)
  {
    this.puissanceChaudiere = puissanceChaudiere;
    this.temperatureMin = temperatureMin + KELVIN;
    this.temperatureMax = temperatureMax + KELVIN;
    this.temperatureMaxChaudiere = temperatureMaxChaudiere + KELVIN;
    perteCalorifique = batiment.getPerteEnergie() / 1000;

    try
    {
      CapaciteCalorifiqueEau = new CapaciteCalorifique(CAPACITE_CALORIFIQUE_EAU, MASSE_VOLUMIQUE_EAU * volumeEauChauffage, temperatureMin);
      CapaciteCalorifiqueAir = new CapaciteCalorifique(CAPACITE_CALORIFIQUE_AIR, MASSE_VOLUMIQUE_AIR * batiment.getVolumeDeAir(), temperatureMin);
      CapaciteCalorifiquePlatre = new CapaciteCalorifique(CAPACITE_CALORIFIQUE_PLATRE, MASSE_VOLUMIQUE_PLATRE * batiment.getLargeur() * batiment.getLongeur() * 6 * 0.013, temperatureMin);
      CapaciteCalorifiqueAutres = new CapaciteCalorifique(CAPACITE_CALORIFIQUE_AUTRES, MASSE_VOLUMIQUE_AUTRES * batiment.getVolumeDeAir(), temperatureMin);

      pourcentageCapteParRadiateur = (surfaceRadiateur * EPAISSEUR_ACIER) / volumeEauChauffage;

      double TotaleEnergie = CapaciteCalorifiqueAir.energieAvecDeltaDeTemperature(1)
              + CapaciteCalorifiquePlatre.energieAvecDeltaDeTemperature(1)
              + CapaciteCalorifiqueAutres.energieAvecDeltaDeTemperature(1);

      pourcentageEnergieAir = CapaciteCalorifiqueAir.energieAvecDeltaDeTemperature(1) / TotaleEnergie;
      pourcentageEnergiePlatre = CapaciteCalorifiquePlatre.energieAvecDeltaDeTemperature(1) / TotaleEnergie;
      pourcentageEnergieAutres = CapaciteCalorifiqueAutres.energieAvecDeltaDeTemperature(1) / TotaleEnergie;
    }
    catch (Exception ex)
    {
      System.err.println(ex.getMessage());
    }
  }

  /*
   * dureeMin : nombre de minutes ou l'on souhaite faire la simulation
   */
  public List<Acquisition> getAcquisiton(final int dureeMin)
  {
    int echantillonnage = dureeMin * 60; // converti en seconde
    List<Acquisition> acquisition = new ArrayList<>();
    double temperatureEau = temperatureMin; // T en °K
    double temperatureAir = temperatureMin; // T en °K
    Acquisition ligne = new Acquisition(0, temperatureEau, temperatureAir);

    for (int i = 0; i < echantillonnage; i++)
    {
      acquisition.add(ligne);

      try
      {
        ligne = actionChauffage(temperatureEau, temperatureAir);
      }
      catch (Exception ex)
      {
        System.err.println(ex.getMessage());
      }
      temperatureEau = ligne.getTemperatureKelvinEau();
      temperatureAir = ligne.getTemperatureKelvinAir();
    }
    return acquisition;
  }
  private boolean declencheChauffage = false;
  private boolean tMaxChaudiereEstAtteint = false;

  private Acquisition actionChauffage(final double temperatureEauInit, final double temperatureAirInit) throws Exception
  {
    if ((temperatureAirInit < temperatureMin) || (declencheChauffage && temperatureAirInit < temperatureMax))
    {
      declencheChauffage = true;
    }
    else if ((temperatureAirInit > temperatureMax) || (!declencheChauffage && temperatureAirInit > temperatureMin))
    {
      declencheChauffage = false;
    }

    if (temperatureEauInit > temperatureMaxChaudiere)
    {
      tMaxChaudiereEstAtteint = true;
    }
    if (tMaxChaudiereEstAtteint && temperatureEauInit < (temperatureMaxChaudiere - 20))
    {
      tMaxChaudiereEstAtteint = false;
    }

    // chauffage
    double energieConsommee = 0.0;
    double temperatureEauFin = temperatureEauInit;
    if (declencheChauffage && !tMaxChaudiereEstAtteint)
    {
      energieConsommee = puissanceChaudiere;
      temperatureEauFin = CapaciteCalorifiqueEau.temperatureApresUneSeconde(puissanceChaudiere);
    }
    double energieDisponible = CapaciteCalorifiqueEau.energieAvecDeltaDeTemperature(temperatureEauFin - temperatureAirInit);
    double energiePerduParEau = -(perteCalorifique + energieDisponible) * pourcentageCapteParRadiateur;
    energieDisponible = (energieDisponible * pourcentageCapteParRadiateur) - perteCalorifique;
    double temperatureAirFin = CapaciteCalorifiqueAir.temperatureApresUneSeconde(energieDisponible * pourcentageEnergieAir);
    CapaciteCalorifiquePlatre.temperatureApresUneSeconde(energieDisponible * pourcentageEnergiePlatre);
    CapaciteCalorifiqueAutres.temperatureApresUneSeconde(energieDisponible * pourcentageEnergieAutres);
    temperatureEauFin = CapaciteCalorifiqueEau.temperatureApresUneSeconde(energiePerduParEau);

    return new Acquisition(energieConsommee, temperatureEauFin, temperatureAirFin);
  }
}
